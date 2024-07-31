package me.tuan.eternity.generator;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class PlayersListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Generator generator = Generator.HOLDER.stream()
			.sorted()
			.filter(g -> canUse(g, player))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		Generator.PLAYER.put(player.getUniqueId(), generator);
	}
	
	private boolean canUse(Generator generator, Player player) {
		return (generator.permission() != null && 
			player.hasPermission(generator.permission()) || 
			(generator.isDefault() != null && generator.isDefault()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Generator.PLAYER.remove(event.getPlayer().getUniqueId());
	}
}