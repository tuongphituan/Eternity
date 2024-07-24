package me.tuan.eternity.command;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends Command {
	
	public ReloadCommand() {
		super("reload", "eternity.command.reload");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!canExecute(sender)) return;
		
	}
}