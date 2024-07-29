package me.tuan.eternity.generator;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.WeakHashMap;

public class GeneratorsListener implements Listener {
	private final Map<Block, Generator> generators = new WeakHashMap<>();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Generator generator = Generator.get(event.getPlayer().getUniqueId());
		if (generator != null && generator.hasBlock(event.getBlock().getType()))
			generators.put(event.getBlock(), generator);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		Generator generator = generators.remove(event.getBlock());
		if (generator != null && generator.isGeneratorBlock(event.getNewState().getType()))
			event.getNewState().setType(generator.generate());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Generator generator = Generator.stream()
			.filter(g -> canUse(g, player))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		Generator.store(player.getUniqueId(), generator);
	}
	
	private boolean canUse(Generator generator, Player player) {
		return generator.isDefault() || (generator.permission() != null && 
			player.hasPermission(generator.permission()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Generator.remove(event.getPlayer().getUniqueId());
	}
}