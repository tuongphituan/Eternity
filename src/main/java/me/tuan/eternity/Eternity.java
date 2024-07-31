package me.tuan.eternity;

import org.bukkit.plugin.java.JavaPlugin;
import me.tuan.eternity.generator.GeneratorsListener;
import me.tuan.eternity.generator.Generator;
import me.tuan.eternity.generator.IslandLevelHandle;
import me.tuan.eternity.generator.PlayersListener;

public class Eternity extends JavaPlugin {
	
	@Override
	public void onLoad() {
		saveDefaultConfig();

		Generator.HOLDER.load(getConfig());
	}
	
	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("SuperiorSkyblock2") != null)
			IslandLevelHandle.load();
		
		getServer().getPluginManager().registerEvents(new PlayersListener(), this);
		getServer().getPluginManager().registerEvents(new GeneratorsListener(), this);
	}
}