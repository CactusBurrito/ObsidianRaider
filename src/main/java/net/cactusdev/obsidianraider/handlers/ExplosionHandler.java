package net.cactusdev.obsidianraider.handlers;

import net.cactusdev.obsidianraider.ObsidianRaiderMain;
import net.cactusdev.obsidianraider.PluginInfo;
import net.cactusdev.obsidianraider.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

/**
 * Handle Explosions events and apply damageable blocks logic. Also handles health checking.
 * @author CactusBurrito
 */
public class ExplosionHandler implements Listener
{
	/**
	 * HashMap containing the blocks that are damageable that have been placed in the world. !This is reset every restart
	 * , currently no way to save data over to new game session. If this is wanted, let me know and i can work on it.
	 */
	private static HashMap<String, Double> _BlockHealth = new HashMap<String, Double>();

	/**
	 * Create a new instance of {@link ExplosionHandler}.
	 */
	public ExplosionHandler()
	{
		getServer().getPluginManager().registerEvents(this, ObsidianRaiderMain.GetInstance());
	}

	/**
	 * Sort the list of blocks provided to contain or not contain the damageable blocks, depending on how much health
	 * they will have remaining after damage logic is run. This also depends if block is naturally explodable or not.
	 * For example, if obsidian reaches 0 health, we must add it to the list as naturally TnT doesnt record hits to obsidian
	 * blocks. However, if a dirt block doesn't reach 0 health, we remove it from the list.
	 * This method also checks blocks outside the block list as said before, obsidian is not usually detected by tnt
	 * in minecraft, so we must do a relatively expensive search across all blocks in a radius to check if tnt should be damaged.
	 * Currently this happens with any damageable blocks defined. In the very near future, I will update this to only be done
	 * for blocks that naturally are not detected by tnt to reduce the stress on the cpu from this method.
	 * @param source Location of explosion.
	 * @param blockList List of blocks from explosion.
	 */
	private void SortExplosionBlocks(Location source, List<Block> blockList)
	{
		int radius = PluginInfo.GetExplosionRadius();

		for(int x = -radius; x <= radius; x++)
		{
			for(int y = -radius; y <= radius; y++)
			{
				for(int z = -radius; z <= radius; z++)
				{
					Location checkLocation = new Location(source.getWorld(), x + source.getX(), y + source.getY(), z + source.getZ());

					double distToCheckLocationSquared = source.distanceSquared(checkLocation);

					if(distToCheckLocationSquared <= PluginInfo.GetExplosionRadiusSquared())
					{
						Block block = checkLocation.getBlock();

						if(IsBlockDamageable(block))
						{
							double blockDistToSource = Math.sqrt(distToCheckLocationSquared);

							double damage = 1;

							boolean isSurroundedByWater = false;

							if(PluginInfo.CheckBlockSurroundedByLiquid())
							{
								 isSurroundedByWater = block.getRelative(BlockFace.UP, 1).isLiquid() ||
										block.getRelative(BlockFace.DOWN, 1).isLiquid() ||
										block.getRelative(BlockFace.NORTH,1).isLiquid() ||
										block.getRelative(BlockFace.SOUTH,1).isLiquid() ||
										block.getRelative(BlockFace.EAST, 1).isLiquid() ||
										block.getRelative(BlockFace.WEST, 1).isLiquid();
							}

							if(PluginInfo.DoesDistanceAffectDamage())
							{
								damage = damage / blockDistToSource;

								if(damage < PluginInfo.GetMinimumDistanceDamage())
								{
									damage = PluginInfo.GetMinimumDistanceDamage();
								}
							}

							if(source.getBlock().isLiquid() || isSurroundedByWater)
							{
								damage = damage * PluginInfo.GetLiquidDamageMultiplier();
							}

							String id = Utils.GetBlockID(block);

							if(_BlockHealth.containsKey(id))
							{
								_BlockHealth.put(id, _BlockHealth.get(id) - damage);
							}
							else
							{
								_BlockHealth.put(id, GetBlockHealth(block) - damage);
							}

							if(_BlockHealth.get(id) <= 0)
							{
								_BlockHealth.remove(id);

								if(block.getType().equals(Material.OBSIDIAN))
								{
									blockList.add(block);
								}

								continue;
							}

							blockList.remove(block);
						}
					}
				}
			}
		}
	}

	/**
	 * Display the health of the block as percentage by searching for it in the hashmap. If it doesnt exist, it must be recreated at 100%
	 * as we do not currently store previous session data. So data is lost on server restart. But that isnt a huge problem for
	 * most servers as it probably wont be noticed.
	 * @param player Player who wants to check the health.
	 * @param block Block being checked.
	 */
	private void DisplayBlockHealth(Player player, Block block)
	{
		if(block != null)
		{
			String id = Utils.GetBlockID(block);

			if(_BlockHealth.containsKey(id))
			{
				int percent = (int) (((_BlockHealth.get(id) * 100) / GetBlockHealth(block)));

				String healthMessage = ChatColor.translateAlternateColorCodes('&', ObsidianRaiderMain.GetInstance().getConfig().getString("Message.Block health").replaceFirst("<percent>", String.valueOf(percent)));
				player.sendMessage(healthMessage);
			}
			else if(IsBlockDamageable(block))
			{
				String healthMessage = ChatColor.translateAlternateColorCodes('&', ObsidianRaiderMain.GetInstance().getConfig().getString("Message.Block health").replaceFirst("<percent>", "100"));
				player.sendMessage(healthMessage);
			}
		}
	}

	/**
	 * Get the health of the damageable block.
	 * @param block Block to get health of.
	 * @return Health of the block as double.
	 */
	private double GetBlockHealth(Block block)
	{
		Material m = block.getType();

		return ((Double)PluginInfo.GetDamageableBlocks().get(m.toString().toLowerCase())).doubleValue();
	}

	/**
	 * Is the block a damageable one, as defined in the config.
	 * @param block Block to check.
	 * @return True if damageable by explosions.
	 */
	private boolean IsBlockDamageable(Block block)
	{
		Material m = block.getType();

		return PluginInfo.GetDamageableBlocks().containsKey(m.toString().toLowerCase());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnBlockBroken(BlockBreakEvent event)
	{
		if(!event.isCancelled())
		{
			if(IsBlockDamageable(event.getBlock()))
			{
				String id = Utils.GetBlockID(event.getBlock());

				if(_BlockHealth.containsKey(id))
				{
					_BlockHealth.remove(id);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.isCancelled())
		{
			Player player = event.getPlayer();

			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getHand().equals(EquipmentSlot.OFF_HAND))
			{
				if(player.getInventory().getItemInMainHand()!= null && player.getInventory().getItemInMainHand().getType().toString().equalsIgnoreCase(PluginInfo.GetHealthCheckItem()))
				{
					Block block = event.getClickedBlock();

					DisplayBlockHealth(player, block);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void OnPrimeExplosion(ExplosionPrimeEvent event)
	{
		if(!event.isCancelled())
		{
			if(!PluginInfo.IsExplosionRadiusModifierEnabled())
			{
				if(event.getEntityType().equals(EntityType.PRIMED_TNT))
				{
					event.setRadius(PluginInfo.GetExplosionRadiusTnT());
					return;
				}

				if(event.getEntityType().equals(EntityType.WITHER_SKULL))
				{
					event.setRadius(PluginInfo.GetExplosionRadiusWitherSkull());
					return;
				}

				if(event.getEntityType().equals(EntityType.WITHER))
				{
					event.setRadius(PluginInfo.GetExplosionRadiusWither());
					return;
				}

				if(event.getEntityType().equals(EntityType.ENDER_CRYSTAL))
				{
					event.setRadius(PluginInfo.GetExplosionRadiusEndCrystal());
					return;
				}

				if(event.getEntityType().equals(EntityType.MINECART_TNT))
				{
					event.setRadius(PluginInfo.GetExplosionRadiusTnT());
					return;
				}

				if(event.getEntityType().equals(EntityType.CREEPER))
				{
					event.setRadius(((Creeper)event.getEntity()).isPowered() ? PluginInfo.GetExplosionRadiusChargedCreeper() : PluginInfo.GetExplosionRadiusCreeper());
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnBlockExplode(BlockExplodeEvent event)
	{
		if(!event.isCancelled())
		{
			SortExplosionBlocks(event.getBlock().getLocation(), event.blockList());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnEntityExplode(EntityExplodeEvent event)
	{
		if(!event.isCancelled() && event.getEntity() != null)
		{
			if(!PluginInfo.AreAllExplosionsModified() && event.getEntityType() != EntityType.PRIMED_TNT)
			{
				return;
			}

			SortExplosionBlocks(event.getLocation(), event.blockList());
		}
	}

	public void Dispose()
	{
		HandlerList.unregisterAll();
	}
}