package net.cactusdev.obsidianraider.handlers;

import net.cactusdev.obsidianraider.ObsidianRaiderMain;
import net.cactusdev.obsidianraider.PluginInfo;
import net.cactusdev.obsidianraider.debug.DebugUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * Handles correct initialisation or replacement of the config file if the old one is out of date.
 * Config file versions checked against the {@link PluginInfo} ConfigVersion number. Dont forget to also
 * change the config version inside the config file.
 * @author CactusBurrito
 */
public class ConfigHandler
{

	/**
	 * Reference to the plugin config file for this period of plugin activity.
	 */
	private static FileConfiguration _Config;

	/**
	 * Create an instance of {@link ConfigHandler}.
	 */
	public ConfigHandler()
	{
		CheckForOldConfig();
	}

	/**
	 * Checks and replaces the config if old one exists with incorrect version number.
	 * If a config doesnt exist, creates a new one. If config is the correct version, then leaves it as is.
	 */
	private void CheckForOldConfig()
	{
		ObsidianRaiderMain main = ObsidianRaiderMain.GetInstance();

		//Create a directory for the plugin, if one doesnt exist.
		main.getDataFolder().mkdir();

		_Config = main.getConfig();
		_Config.options().copyDefaults(true);
		_Config.addDefault("Config Version", 0);

		try
		{
			//Test config version against version in the plugin info class.
			if(main.getConfig().getInt("Config Version") < PluginInfo.CONFIG_VERSION)
			{
				//Get predefined config file.
				URL inputUrl = main.getClass().getResource("/config.yml");

				//Find the folder that must contain the config file.
				File destination = new File(main.getDataFolder() + File.separator + "config.yml");

				if(destination.exists())
				{
					//Save the old config for the user reference.
					File renameTo = new File(destination.getParent() + File.separator + "old_config.yml");

					if(renameTo.exists())
					{
						renameTo.delete();
					}

					destination.renameTo(renameTo);

					DebugUtils.Print("Replaced old config with new, please check files!");
				}

				FileUtils.copyURLToFile(inputUrl, destination);

				DebugUtils.Print("Config successfully exported.");
			}

		} catch(Exception ex)
		{
			ex.printStackTrace();
		}

		_Config = main.getConfig();
	}

	public static FileConfiguration GetConfig()
	{
		return _Config;
	}
}
