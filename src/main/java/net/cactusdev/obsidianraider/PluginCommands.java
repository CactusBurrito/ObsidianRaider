package net.cactusdev.obsidianraider;


import org.bukkit.command.CommandExecutor;

/**
 * All commands that this plugin can detect and handle. All commands must be lowercase.
 */
public class PluginCommands
{
	/**
	 * Fully reloads the plugin.
	 */
	public static final String RELOAD = "reload";

	public PluginCommands()
	{

	}

	/**
	 * Register a command with the provided {@link CommandExecutor}.
	 * @param commandName Name of the main command name.
	 * @param commandExecutor The command executor instance.
	 */
	public void RegisterCommand(String commandName, CommandExecutor commandExecutor)
	{
		ObsidianRaiderMain main = ObsidianRaiderMain.GetInstance();

		main.getCommand(commandName).setExecutor(commandExecutor);
	}

}
