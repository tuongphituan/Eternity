package me.tuan.eternity.command;

import org.bukkit.command.CommandSender;

public class Command {
	private final String name, permission;
	
	public Command(String name, String permission) {
		this.name = name;
		this.permission = permission;
	}
	
	public boolean canExecute(CommandSender sender) {
		return sender.hasPermission(permission);
	}
	
	public void execute(CommandSender sender, String[] args) {
	}
}