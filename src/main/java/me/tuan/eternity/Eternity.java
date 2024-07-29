package me.tuan.eternity;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import me.tuan.eternity.generator.GeneratorsListener;
import me.tuan.eternity.generator.Generator;

public class Eternity extends JavaPlugin {
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		Generator.load(getConfig());
	}
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new GeneratorsListener(), this);
	}
}