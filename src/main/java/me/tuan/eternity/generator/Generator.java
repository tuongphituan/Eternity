package me.tuan.eternity.generator;

import org.bukkit.Material;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class Generator {
	private static final Map<UUID, Generator> generators = new HashMap<>();
	private static final SplittableRandom random = new SplittableRandom();
	
	private Map<Material, Double> blocks;
	private Set<Entry<Material, Double>> entries;
	private double totalChance;
	private String permission;
	private boolean isDefault;
	
	public Generator(Map<Material, Double> blocks, String permission, boolean isDefault) {
		this.blocks = blocks;
		this.entries = blocks.entrySet();
		this.totalChance = blocks.values().stream().reduce(0.0, Double::sum);
		this.permission = permission;
		this.isDefault = isDefault;
	}
	
	public Material generate() {
		if (!blocks.isEmpty()) {
			double value = random.nextDouble(totalChance);
			for (Entry<Material, Double> entry : entries)
				if ((value -= entry.getValue()) < 0) return entry.getKey();
		}
		return Material.COBBLESTONE;
	}
	
	public boolean canUse(Player player) {
		return isDefault || player.hasPermission(permission);
	}
	
	public void addBlock(Material type, double chance) {
		Double oldChance = blocks.put(type, chance);
		if (oldChance != null) totalChance -= oldChance;
		entries.add(Map.entry(type, chance));
		totalChance += chance;
	}
	
	public void removeBlock(Material type) {
		Double chance = blocks.remove(type);
		if (chance != null) {
			totalChance -= chance;
			entries.remove(Map.entry(type, chance));
		}
	}
	
	public boolean hasBlock(Material type) {
		return blocks.containsKey(type) || hasBlock1(type);
	}
	
	private boolean hasBlock1(Material type) {
		return type == Material.COBBLESTONE || type == Material.BASALT;
	}
	
	public static Generator get(UUID id) {
		return generators.get(id);
	}
	
	public static void store(UUID id, Generator generator) {
		generators.put(id, generator);
	}
	
	public static void remove(UUID id) {
		generators.remove(id);
	}
}