package me.tuan.eternity.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.BufferedReader;

public class Config {
	private final Path file;
	
	public Config(String name, String... more) {
		this.file = Paths.get(name, more);
	}
	
	public void load() {
		
	}
	
	public void loadDefaults() throws IOException {
		if (Files.notExists(file.getParent())) Files.createDirectories(file.getParent());
		if (Files.notExists(file)) Files.copy(getDefaultAsStream(), file);
	}
	
	public InputStream getDefaultAsStream() {
		return getClass().getClassLoader().getResourceAsStream(getName());
	}
	
	public BufferedReader reader() throws IOException {
		return Files.newBufferedReader(file);
	}
	
	public String getName() {
		return file.getFileName().toString();
	}
	
	public Path getFile() {
		return file;
	}
}