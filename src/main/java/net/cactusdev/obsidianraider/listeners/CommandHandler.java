package net.cactusdev.obsidianraider.listeners;

import net.cactusdev.obsidianraider.ObsidianRaiderMain;
import net.cactusdev.obsidianraider.PluginCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


/**
 * Class responsible for listening and handling user commands.
 */
public class CommandHandler
{

	/**
	 * Create a new instance of a {@link CommandHandler}.
	 */
	public CommandHandler()
	{

	}


	public boolean OnCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String command = cmd.getName().toLowerCase();

		if (sender.hasPermission("obsidianraider.reload"))
		{
			if(command.equals(PluginCommands.RELOAD))
			{
				ObsidianRaiderMain.GetInstance().OnReload();
			}
		}

		return false;
	}
}
