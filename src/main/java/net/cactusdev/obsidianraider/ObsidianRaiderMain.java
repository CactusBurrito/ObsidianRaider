package net.cactusdev.obsidianraider;

import net.cactusdev.obsidianraider.debug.DebugUtils;
import net.cactusdev.obsidianraider.config.PluginConfig;
import net.cactusdev.obsidianraider.interfaces.IDisposable;
import net.cactusdev.obsidianraider.listeners.CommandHandler;
import net.cactusdev.obsidianraider.listeners.ExplosionHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the game. Handles plugin initialisation.
 * @author CactusBurrito
 */
public class ObsidianRaiderMain extends JavaPlugin implements IDisposable
{
	/**
	 * Instance of the {@link ObsidianRaiderMain} class held in this static variable for access through out the plugin
	 * class instances.
	 */
	private static ObsidianRaiderMain _Instance;

	/**
	 * Reference to instance of {@link PluginInfo}.
	 */
	private static PluginInfo _PluginInfo;

	/**
	 * Reference to instance of {@link ExplosionHandler}.
	 */
	private static ExplosionHandler _ExplosionHandler;

	/**
	 * Reference to instance of {@link PluginConfig}.
	 */
	private static PluginConfig _ConfigHandler;

	/**
	 * Reference to instance of {@link CommandHandler}.
	 */
	private static CommandHandler _CommandHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable()
	{
		if(_Instance != null) return; //If instance already exists, do not allow another one to be set up.

		_Instance = this;

		_ConfigHandler = new PluginConfig();
		_PluginInfo = new PluginInfo(PluginConfig.GetConfig());
		_ExplosionHandler = new ExplosionHandler();

		_CommandHandler = new CommandHandler();

		DebugUtils.Print("Enabled successfully.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable()
	{
		super.onDisable();

		Dispose();

		DebugUtils.Print("Disabled successfully.");
	}

	/**
	 * When reload is requested by the plugin itself.
	 */
	public void OnReload()
	{
		Dispose();
		onEnable();
	}

	/**
	 * Get instance of this class.
	 * @return Instance of {@link ObsidianRaiderMain}.
	 */
	public static ObsidianRaiderMain GetInstance()
	{
		return _Instance;
	}

	/**
	 * Get instance of the explosion handler.
	 * @return Instance of {@link ExplosionHandler}.
	 */
	public ExplosionHandler GetExplosionHandler()
	{
		return _ExplosionHandler;
	}

	/**
	 * Get instance of the config handler.
	 * @return Instance of {@link PluginConfig}.
	 */
	public PluginConfig GetConfigHandler()
	{
		return _ConfigHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public void Dispose()
	{
		if(_ExplosionHandler != null)
		{
			_ExplosionHandler.Dispose();
			_ExplosionHandler = null;
		}

		if(_PluginInfo != null)
		{
			_PluginInfo.Dispose();
			_PluginInfo = null;
		}

		if(_ConfigHandler != null)
		{
			_ConfigHandler.Dispose();
			_ConfigHandler = null;
		}

		_CommandHandler = null;
		_Instance = null;

		DebugUtils.Print("Removing old references");
	}
}