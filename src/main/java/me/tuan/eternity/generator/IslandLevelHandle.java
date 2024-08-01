package me.tuan.eternity.generator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

public class IslandLevelHandle {
	
	public static void load() {
		SuperiorSkyblockAPI.getModules().getModule("generators").disableModule();
	}
		
	public static void onBlockBreak(Block block) {
		Island island = SuperiorSkyblockAPI.getIslandAt(block.getLocation());
		if (island != null) island.handleBlockBreak(block);
	}
}