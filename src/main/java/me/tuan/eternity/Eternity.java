package me.tuan.eternity;

import org.bukkit.plugin.java.JavaPlugin;
import me.tuan.eternity.listener.GeneratorsListener;
import me.tuan.eternity.listener.PlayersListener;
import me.tuan.eternity.generator.Generators;
import java.util.Map;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

public class Eternity extends JavaPlugin {
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		
		getConfig().getList("generators").stream()
			.map(obj -> (Map<?, ?>) obj)
			.forEach(Generators::create);
	}
	
	@Override
	public void onEnable() { 
		if (getServer().getPluginManager().getPlugin("SuperiorSkyblock2") != null)
			SuperiorSkyblockAPI.getModules().getModule("generators").disableModule();
		
		getServer().getPluginManager().registerEvents(new PlayersListener(), this);
		getServer().getPluginManager().registerEvents(new GeneratorsListener(), this);
	}
}