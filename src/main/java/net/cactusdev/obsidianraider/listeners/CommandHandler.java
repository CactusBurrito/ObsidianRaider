package net.cactusdev.obsidianraider.listeners;

import net.cactusdev.obsidianraider.ObsidianRaiderMain;
import net.cactusdev.obsidianraider.PluginCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getServer;

/**
 * Class responsible for listening and handling user commands.
 */
public class CommandHandler implements Listener
{

	public CommandHandler()
	{
		getServer().getPluginManager().registerEvents(this, ObsidianRaiderMain.GetInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase(PluginCommands.RELOAD))
		{
			return true;
		}


		return false;
	}
}
