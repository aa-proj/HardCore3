package jp.ahoaho;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("setlife")){
            if(!sender.hasPermission("op")){
                sender.sendMessage("opを持っていません");
                return false;
            }
            try {
                int life = Integer.parseInt(args[0]);
                Hardcore3.config.set("life", life);
                Bukkit.getServer().broadcastMessage(String.format("合計ライフは%dに設定されました。", life));
                Hardcore3.getPlugin(Hardcore3.class).reloadPluginConfig();
                return true;
            } catch (Exception e) {
                sender.sendMessage("opを持っていません");
                return false;
            }
        }
        return false;
    }
}
