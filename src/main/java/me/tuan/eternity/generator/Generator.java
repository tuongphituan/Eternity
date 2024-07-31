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
import java.util.Comparator;
import java.util.stream.Collectors;

public class Generator implements Comparable<Generator> {
	public static final Holder HOLDER = new Holder();
	public static final Map<UUID, Generator> PLAYER = new HashMap<>();
	
	private static final SplittableRandom random = new SplittableRandom();
	
	private final List<Material> materials = new ArrayList<>();;
	private final List<Double> chances = new ArrayList<>();
	private final List<Integer> alias = new ArrayList<>();
	
	private final String permission;
	private final Boolean isDefault;
	
	public Generator(Map<Material, Double> blocks, String permission, Boolean isDefault) {
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
	
	public static boolean isGeneratorBlock(Material type) {
		return type == Material.COBBLESTONE || type == Material.BASALT;
	}
	
	@Override
	public int compareTo(Generator other) {
		int result = Comparator.comparing(Generator::permission, Comparator.nullsLast(Comparator.naturalOrder()))
			.compare(this, other);
		if (result != 0) return result;
		
		return Comparator.comparing(Generator::isDefault, Comparator.nullsLast(Comparator.naturalOrder()))
			.compare(this, other);
	}
	
	public static class Holder extends ArrayList<Generator> {
		static final long serialVersionUID = 1374763L;
		
		public void load(Configuration config) {
			List<?> list = config.getList("generators");
			list.stream().map(obj -> (Map<?, ?>) obj).forEach(this::load);
		}
		
		private void load(Map<?, ?> map) {
			Map<Material, Double> blocks = ((Map<?, ?>) map.get("blocks")).entrySet()
				.stream()
				.collect(Collectors.toMap(
					e -> Material.getMaterial(e.getKey().toString()),
					e -> ((Number) e.getValue()).doubleValue()));
			
			String permission = (String) map.get("permission");
			Boolean isDefault = (Boolean) map.get("default");
			
			add(new Generator(blocks, permission, isDefault));
		}
	}
}