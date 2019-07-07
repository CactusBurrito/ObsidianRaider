package net.cactusdev.obsidianraider.debug;

import net.cactusdev.obsidianraider.PluginInfo;

public class DebugUtils
{

	public static void Print(String message)
	{
		System.out.println("[" + PluginInfo.PLUGIN_NAME + "]" + "[" + PluginInfo.PLUGIN_VERSION + "]: " + message);
	}

}
