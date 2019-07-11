package net.cactusdev.obsidianraider;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

/**
 * All the info taken from the config and stored within this class rather than constantly searching string in the config.
 * @author CactusBurrito
 */
public class PluginInfo
{

	public static final String PLUGIN_NAME = "Obsidian-Raider";

	public static final String PLUGIN_VERSION = "1.1";

	public static final int CONFIG_VERSION = 4;

	public static final String[] SUPPORTED_BUKKIT_VERSIONS = {"1.12","1.13","1.14"};

	private static int _ExplosionRadius;
	private static float _ExplosionRadiusSquared;
	private static boolean _AllExplosionsModified;
	private static boolean _DistanceAffectDamage;
	private static double _MinimumDistanceDamage;
	private static double _LiquidDamageMultiplier;
	private static Map<String, Object> _DamageableBlocks;
	private static String _HealthCheckItem;
	private static boolean _CheckSurroundingsForLiquid;

	private static boolean _ExplosionRadiusModifierEnabled;
	private static float _ExplosionRadiusTnT;
	private static float _ExplosionRadiusCreeper;
	private static float _ExplosionRadiusChargedCreeper;
	private static float _ExplosionRadiusEndCrystal;
	private static float _ExplosionRadiusWither;
	private static float _ExplosionRadiusWitherSkull;


	public PluginInfo(FileConfiguration config)
	{
		_ExplosionRadiusModifierEnabled = config.getBoolean("Radius.Enabled");
		_ExplosionRadiusTnT = (float)config.getDouble("Radius.TnT");
		_ExplosionRadiusCreeper = (float)config.getDouble("Radius.Creeper");
		_ExplosionRadiusChargedCreeper = (float)config.getDouble("Radius.ChargedCreeper");
		_ExplosionRadiusEndCrystal = (float)config.getDouble("Radius.EndCrystal");
		_ExplosionRadiusWither = (float)config.getDouble("Radius.Wither");
		_ExplosionRadiusWitherSkull = (float)config.getDouble("Radius.WitherSkull");

		_ExplosionRadius = (int)Math.ceil(config.getDouble("Explosion Radius"));
		_ExplosionRadiusSquared = _ExplosionRadius * _ExplosionRadius;
		_AllExplosionsModified = config.getBoolean("All Explosions");
		_DistanceAffectDamage = config.getBoolean("Distance Affects Damage");
		_MinimumDistanceDamage = config.getDouble("Minimum Distance Damage");
		_LiquidDamageMultiplier = config.getDouble("Liquid Multiplier");
		_DamageableBlocks = (config.getConfigurationSection("Damageable Block Health").getValues(false));
		_HealthCheckItem = config.getString("CheckItem.Item");
		_CheckSurroundingsForLiquid = config.getBoolean("Check Block Liquid");

		Utils.LowerCaseKeys(_DamageableBlocks);
	}

	public static int GetExplosionRadius()
	{
		return _ExplosionRadius;
	}

	public static float GetExplosionRadiusSquared()
	{
		return _ExplosionRadiusSquared;
	}

	public static boolean AreAllExplosionsModified()
	{
		return _AllExplosionsModified;
	}

	public static boolean DoesDistanceAffectDamage()
	{
		return _DistanceAffectDamage;
	}

	public static double GetMinimumDistanceDamage()
	{
		return _MinimumDistanceDamage;
	}

	public static double GetLiquidDamageMultiplier()
	{
		return _LiquidDamageMultiplier;
	}

	public static String GetHealthCheckItem()
	{
		return _HealthCheckItem;
	}

	public static Map<String, Object> GetDamageableBlocks()
	{
		return _DamageableBlocks;
	}

	public static boolean CheckBlockSurroundedByLiquid()
	{
		return _CheckSurroundingsForLiquid;
	}

	public static boolean IsExplosionRadiusModifierEnabled()
	{
		return _ExplosionRadiusModifierEnabled;
	}

	public static float GetExplosionRadiusTnT()
	{
		return _ExplosionRadiusTnT;
	}

	public static float GetExplosionRadiusCreeper()
	{
		return _ExplosionRadiusCreeper;
	}

	public static float GetExplosionRadiusChargedCreeper()
	{
		return _ExplosionRadiusChargedCreeper;
	}

	public static float GetExplosionRadiusEndCrystal()
	{
		return _ExplosionRadiusEndCrystal;
	}

	public static float GetExplosionRadiusWither()
	{
		return _ExplosionRadiusWither;
	}

	public static float GetExplosionRadiusWitherSkull()
	{
		return _ExplosionRadiusWitherSkull;
	}
}
