package me.tuan.eternity.generator;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Set;

public class GeneratorsListener implements Listener {
	private final Map<Block, Generator> generators = new WeakHashMap<>();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Generator generator = Generator.PLAYER.get(event.getPlayer().getUniqueId());
		if (generator != null) {
			Block block = event.getBlock();
			if (!generator.hasBlock(block.getType())) return;
			generators.put(block, generator);
			IslandLevelHandle.onBlockBreak(block);
		}
	}
	
	private final Set<Material> dest = Set.of(Material.LAVA, Material.OAK_FENCE, 
		Material.BIRCH_FENCE, Material.ACACIA_FENCE, Material.BAMBOO_FENCE, 
		Material.CHERRY_FENCE, Material.JUNGLE_FENCE, Material.SPRUCE_FENCE, 
		Material.WARPED_FENCE, Material.CRIMSON_FENCE, Material.MANGROVE_FENCE);
	
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.getBlock().getType() != Material.WATER ||
		!dest.contains(event.getToBlock().getRelative(event.getFace()).getType())) return;
		
		event.setCancelled(true);
		
		Block to = event.getToBlock();
		
		Generator generator = generators.remove(to);
		if (generator != null) to.setType(generator.generate(), false);
		else to.setType(Material.COBBLESTONE, false);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (!Generator.isGeneratorBlock(event.getNewState().getType())) return;
		
		Generator generator = generators.remove(event.getBlock());
		if (generator != null) event.getNewState().setType(generator.generate());
	}
}