package me.tuan.eternity;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import me.tuan.eternity.generator.GeneratorsListener;
import me.tuan.eternity.generator.GeneratorsConfig;

public class Eternity extends JavaPlugin {
	private final GeneratorsConfig generatorsConfig = new GeneratorsConfig(getDataFolder().getPath());
	
	@Override
	public void onLoad() {
		generatorsConfig.load();
	}
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new GeneratorsListener(generatorsConfig), this);
	}
}