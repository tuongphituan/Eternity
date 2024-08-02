package me.tuan.eternity.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import me.tuan.eternity.generator.Generator;
import me.tuan.eternity.generator.Generators;

public class GeneratorsListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Generator generator = Generators.PLAYER.get(event.getPlayer().getUniqueId());
		if (generator != null) {
			Block block = event.getBlock();
			
			Material type = block.getType();
			if (!generator.hasBlock(type) && Generators.notVanillaBlock(type)) 
				return;
			
			Generators.BLOCK.put(block, generator);
			Generators.handleIslandLevel(block);
		}
	}
	
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.getBlock().getType() != Material.WATER) return;
		
		Block to = event.getToBlock();
		if (Generators.notDestination(to.getRelative(event.getFace()).getType()))
			return;
		
		event.setCancelled(true);
		
		Generator generator = Generators.BLOCK.remove(to);
		if (generator != null) to.setType(generator.generate(), false);
		else to.setType(Material.COBBLESTONE, false);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (Generators.notVanillaBlock(event.getNewState().getType())) return;
		
		Generator generator = Generators.BLOCK.remove(event.getBlock());
		if (generator != null) event.getNewState().setType(generator.generate());
	}
}