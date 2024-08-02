package me.tuan.eternity.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import me.tuan.eternity.generator.Generators;
import me.tuan.eternity.generator.Generator;

public class PlayersListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Generator generator = Generators.CURRENT.stream()
			.sorted()
			.filter(g -> player.hasPermission(g.permission()) || g.isDefault())
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		
		Generators.PLAYER.put(player.getUniqueId(), generator);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Generators.PLAYER.remove(event.getPlayer().getUniqueId());
	}
}