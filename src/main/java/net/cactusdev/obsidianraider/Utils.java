/**
 * Copyright 2019, for https://github.com/CactusBurrito user, All rights reserved.
 */

package net.cactusdev.obsidianraider;

import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Utils
{

	public static String GetBlockID(Block block)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(block.getWorld().getName() + " ");
		sb.append(block.getX() + " ");
		sb.append(block.getY() + " ");
		sb.append(block.getZ());

		return sb.toString();
	}

	public static Map<String, Object> LowerCaseKeys(Map<String, Object> map)
	{
		Map<String, Object> output = new HashMap<String, Object>();

		for(Map.Entry<String, Object> e : map.entrySet())
		{
			output.put(e.getKey().toLowerCase(), e.getValue());
		}

		return output;
	}

}
