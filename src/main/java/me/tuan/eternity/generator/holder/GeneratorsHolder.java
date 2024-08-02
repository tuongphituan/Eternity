package me.tuan.eternity.generator.holder;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import me.tuan.eternity.generator.Generator;
import org.bukkit.configuration.Configuration;
import org.bukkit.Material;

public class GeneratorsHolder extends ArrayList<Generator> {
	static final long serialVersionUID = 1374763L;
		
	public void load(Configuration config) {
		config.getList("generators")
			.stream()
			.map(obj -> (Map<?, ?>) obj)
			.forEach(this::load);
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