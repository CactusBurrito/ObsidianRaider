package net.cactusdev.obsidianraider;

import net.cactusdev.obsidianraider.debug.DebugUtils;
import net.cactusdev.obsidianraider.handlers.ConfigHandler;
import net.cactusdev.obsidianraider.listeners.ExplosionHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the game. Handles plugin initialisation.
 * @author CactusBurrito
 */
public class ObsidianRaiderMain extends JavaPlugin
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
	 * Reference to instance of {@link ConfigHandler}.
	 */
	private static ConfigHandler _ConfigHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable()
	{
		if(_Instance != null) return; //If instance already exists, do not allow another one to be set up.

		_Instance = this;

		_ConfigHandler = new ConfigHandler();
		_PluginInfo = new PluginInfo(ConfigHandler.GetConfig());
		_ExplosionHandler = new ExplosionHandler();

		DebugUtils.Print("Enabled successfully.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable()
	{
		super.onDisable();

		_ExplosionHandler.Dispose();

		_Instance = null;
		_PluginInfo = null;
		_ExplosionHandler = null;
		_ConfigHandler = null;

		DebugUtils.Print("Disabled successfully.");
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
	 * @return Instance of {@link ConfigHandler}.
	 */
	public ConfigHandler GetConfigHandler()
	{
		return _ConfigHandler;
	}
}