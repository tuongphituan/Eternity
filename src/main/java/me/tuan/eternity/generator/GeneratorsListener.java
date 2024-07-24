package me.tuan.eternity.generator;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.Material;
import java.util.Map;
import java.util.WeakHashMap;

public class GeneratorsListener implements Listener {
	private static final Map<Block, Generator> generators = new WeakHashMap<>();
	
	private final GeneratorsConfig config;
	
	public GeneratorsListener(GeneratorsConfig config) {
		this.config = config;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Generator generator = Generator.get(event.getPlayer().getUniqueId());
		if (generator != null && generator.hasBlock(event.getBlock().getType()))
			generators.put(event.getBlock(), generator);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (cantForm(event.getNewState().getType())) return;
		Generator generator = generators.remove(event.getBlock());
		if (generator != null)
			event.getNewState().setType(generator.generate());
	}
	
	private boolean cantForm(Material type) {
		return type != Material.COBBLESTONE && type != Material.BASALT;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Generator generator = config.get().stream()
			.filter(g -> g.canUse(event.getPlayer()))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		Generator.store(event.getPlayer().getUniqueId(), generator);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Generator.remove(event.getPlayer().getUniqueId());
	}
}