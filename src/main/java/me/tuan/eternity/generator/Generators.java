package me.tuan.eternity.generator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.WeakHashMap;
import java.util.Set;
import java.util.HashMap;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

public class Generators {
	public static final List<Generator> CURRENT = new ArrayList<>();
	public static final Map<Block, Generator> BLOCK = new WeakHashMap<>();
	public static final Map<UUID, Generator> PLAYER = new HashMap<>();
	
	private static final Set<Material> DESTINATION = Set.of(Material.OAK_FENCE, 
		Material.BIRCH_FENCE, Material.ACACIA_FENCE, Material.BAMBOO_FENCE, 
		Material.CHERRY_FENCE, Material.JUNGLE_FENCE, Material.SPRUCE_FENCE, 
		Material.WARPED_FENCE, Material.CRIMSON_FENCE, Material.MANGROVE_FENCE);
	
	public static boolean notDestination(Material type) {
		return !DESTINATION.contains(type);
	}
	
	public static boolean notVanillaBlock(Material type) {
		return type != Material.COBBLESTONE && type != Material.BASALT;
	}
	
	public static void create(Map<?, ?> map) {
		Map<Material, Double> blocks = ((Map<?, ?>) map.get("blocks")).entrySet().stream()
			.collect(Collectors.toMap(
				e -> Material.getMaterial(e.getKey().toString()),
				e -> ((Number) e.getValue()).doubleValue()));
		
		String permission = (String) map.get("permission");
		Boolean isDefault = (Boolean) map.get("default");
		
		CURRENT.add(new Generator(blocks, permission, isDefault));
	}
	
	public static void handleIslandLevel(Block block) {
		Island island = SuperiorSkyblockAPI.getIslandAt(block.getLocation());
		if (island != null) island.handleBlockBreak(block);
	}
}