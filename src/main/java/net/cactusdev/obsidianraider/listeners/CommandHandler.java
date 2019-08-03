package net.cactusdev.obsidianraider.listeners;

import net.cactusdev.obsidianraider.ObsidianRaiderMain;
import net.cactusdev.obsidianraider.PluginCommands;
import net.cactusdev.obsidianraider.interfaces.IDisposable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


/**
 * Class responsible for listening and handling user commands.
 */
public class CommandHandler implements CommandExecutor, IDisposable
{

	private static PluginCommands _PluginCommands;


	/**
	 * Create a new instance of a {@link CommandHandler}.
	 */
	public CommandHandler()
	{
		_PluginCommands = new PluginCommands();

		_PluginCommands.RegisterCommand("obsidianr", this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String commandArg_0 = args[0].toLowerCase();

		if(commandArg_0.equals(PluginCommands.RELOAD))
		{
			if (sender.hasPermission("obsidianraider.reload"))
			{
				ObsidianRaiderMain.GetInstance().OnReload();

				sender.sendMessage(ChatColor.DARK_GREEN + "Obsidian Raider Plugin reloaded!");
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "No permission to reload plugin.");
			}
		}

		return false;
	}

	public void Dispose()
	{

	}
}
