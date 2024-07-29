package me.tuan.eternity.generator;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class Generator implements Comparable<Generator> {
	private static final List<Generator> holder = new ArrayList<>();
	private static final Map<UUID, Generator> generators = new HashMap<>();
	private static final SplittableRandom random = new SplittableRandom();
	
	private final List<Material> materials = new ArrayList<>();;
	private final List<Double> chances = new ArrayList<>();
	private final List<Integer> alias = new ArrayList<>();
	
	private final String permission;
	private final Boolean isDefault;
	
	public Generator(Map<Material, Double> blocks, String permission, boolean isDefault) {
		if (blocks.isEmpty()) throw new IllegalStateException();
		
		materials.addAll(blocks.keySet());
		
		this.permission = permission;
		this.isDefault = isDefault;
		
		compute(blocks);
	}
	
	private void compute(Map<Material, Double> blocks) {
		List<Integer> small = new ArrayList<>();
		List<Integer> large = new ArrayList<>();
		
		int s = blocks.size();
		double[] prob = new double[s];
		double[] aliasProb = new double[s];
		
		alias.addAll(Collections.nCopies(s, -1));
	
		double totalChance = 0;
		for (double chance : blocks.values()) totalChance += chance;
		
		int i = 0;
		for (Entry<Material, Double> entry : blocks.entrySet()) {
			prob[i] = entry.getValue() * s / totalChance;
			if (prob[i] < 1.0) small.add(i);
			else large.add(i); i++;
		}
		
		while (!small.isEmpty() && !large.isEmpty()) {
			int less = small.remove(small.size() - 1);
			int more = large.remove(large.size() - 1);
			
			aliasProb[less] = prob[less];
			alias.set(less, more);
			
			prob[more] = prob[more] + prob[less] - 1.0;
			if (prob[more] < 1.0) small.add(more);
			else large.add(more);
		}
		
		while (!small.isEmpty()) aliasProb[small.remove(small.size() - 1)] = 1.0;
		while (!large.isEmpty()) aliasProb[large.remove(large.size() - 1)] = 1.0;

		for (i = 0; i < s; i++) chances.add(aliasProb[i]);
	}
	
	public Material generate() {
		int column = random.nextInt(materials.size());
		boolean coinToss = random.nextDouble() < chances.get(column);
		return materials.get(coinToss ? column : alias.get(column));
	}
	
	public String permission() {
		return permission;
	}
	
	public Boolean isDefault() {
		return isDefault;
	}
	
	public boolean hasBlock(Material type) {
		return materials.contains(type) || isGeneratorBlock(type);
	}
	
	public boolean isGeneratorBlock(Material type) {
		return type == Material.COBBLESTONE || type == Material.BASALT;
	}
	
	@Override
	public int compareTo(Generator other) {
		if (permission != null) {
			int first = permission.compareTo(other.permission());
			return first != 0 ? first : Boolean.compare(isDefault, other.isDefault());
		}
		return Boolean.compare(isDefault, other.isDefault());
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
	
	@SuppressWarnings("unchecked")
	public static void load(Configuration config) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) config.getList("generators");
		
		for (Map<String, Object> map : list) {
			Map<String, Object> blocks = (Map<String, Object>) map.get("blocks");
			
			Map<Material, Double> newBlocks = new HashMap<>();
			for (Entry<String, Object> entry : blocks.entrySet()) {
				Material material = Material.getMaterial(entry.getKey());
				double chance = ((Number) entry.getValue()).doubleValue();
				newBlocks.put(material, chance);
			}
			
			String permission = (String) map.get("permission");
			Boolean isDefault = (Boolean) map.get("default");
			
			holder.add(new Generator(newBlocks, permission, isDefault));
		}
	}
	
	public static Stream<Generator> stream() {
		return holder.stream();
	}
}