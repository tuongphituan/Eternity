package me.tuan.eternity.generator;

import me.tuan.eternity.config.Config;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Material;

public class GeneratorsConfig extends Config {
	private final List<Generator> generators = new ArrayList<>();
	
	private final Yaml yaml = new Yaml();
	
	public GeneratorsConfig(String folder) {
		super(folder, "generators.yml");
	}
	
	@Override
	public void load() {
		super.load();
		
		try {
			compute(yaml.load(newReader()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Generator> get() {
		return generators;
	}
	
	private void compute(List<Map<String, Object>> holders) {
		generators.clear();
		
		for (Map<String, Object> holder : holders) {
			Map<Material, Double> blocks = ((Map<?, ?>) holder.get("blocks")).entrySet()
				.stream()
				.collect(Collectors.toMap(
					e -> Material.getMaterial(e.getKey().toString()), 
					e -> ((Number) e.getValue()).doubleValue()));
			String permission = (String) holder.get("permission");
			boolean isDefault = (Boolean) holder.getOrDefault("default", false);
			
			generators.add(new Generator(blocks, permission, isDefault));
		}
	}
}