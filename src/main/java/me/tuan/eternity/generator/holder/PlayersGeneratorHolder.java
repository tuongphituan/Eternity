package me.tuan.eternity.generator.holder;

import java.util.HashMap;
import java.util.UUID;
import me.tuan.eternity.generator.Generator;
import org.bukkit.entity.Player;

public class PlayersGeneratorHolder extends HashMap<UUID, Generator> {
	static final long serialVersionUID = 124738L;
		
	public void load(Player player) {
		Generator generator = Generator.CURRENT.stream()
			.sorted()
			.filter(g -> canUse(g, player))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
			
		put(player.getUniqueId(), generator);
	}
		
	private boolean canUse(Generator generator, Player player) {
		return (generator.permission() != null && 
			player.hasPermission(generator.permission()) || 
			(generator.isDefault() != null && generator.isDefault()));
	}
}