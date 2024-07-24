package me.tuan.eternity.generator;

import me.tuan.eternity.config.Config;
import java.io.IOException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Material;

public class GeneratorsConfig extends Config {
	private static final List<Generator> generators = new ArrayList<>();
	
	private final Yaml yaml = new Yaml();
	private final Mapper mapper = new Mapper();
	
	public GeneratorsConfig(String folder) {
		super(folder, "generators.yml");
	}
	
	@Override
	public void load() {
		try {
			loadDefaults();
			generators.clear();
			SequenceNode node = (SequenceNode) yaml.compose(reader());
			node.getValue().forEach(mapper::apply);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Generator> get() {
		return generators;
	}
	
	private static class Mapper extends Constructor {
		
		public Mapper() {
			super(new LoaderOptions());
		}
		
		public void apply(Node node) {
			Map<Object, Object> map = constructMapping((MappingNode) node);
			
			Map<Material, Double> blocks = ((Map<?, ?>) map.get("blocks")).entrySet()
				.stream()
				.collect(Collectors.toMap(
					e -> Material.getMaterial(e.getKey().toString()), 
					e -> ((Number) e.getValue()).doubleValue()));
			String permission = (String) map.getOrDefault("permission", "");
			boolean isDefault = (boolean) map.getOrDefault("default", false);
			
			generators.add(new Generator(blocks, permission, isDefault));
		}
	}
}