package me.drmayx.voxelmoneymaker.voxelversemoneymaker.commands;

import me.drmayx.voxelmoneymaker.voxelversemoneymaker.VoxelVerseMoneyMaker;
import me.drmayx.voxelmoneymaker.voxelversemoneymaker.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MoneyMakerCommand implements CommandExecutor {

    private VoxelVerseMoneyMaker plugin;

    public MoneyMakerCommand(VoxelVerseMoneyMaker p){
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length < 1){
            if(sender.hasPermission("moneymaker.command")){
                return false;
            }else{
                sender.sendMessage("§4You do not have permission to use that command§r");
                return true;
            }
        }

        switch(args[0]){
            case "readconfig":
            case "rc":
                if(sender.hasPermission("moneymaker.command")) {
                    sender.sendMessage("Current configuration :");
                    FileConfiguration config = plugin.getConfig();
                    for (String key : config.getKeys(true)) {
                        sender.sendMessage(String.format("%s is %s", key, config.get(key).toString()));
                    }
                }else{
                    sender.sendMessage("§4You do not have permission to use that command§r");
                }
                break;
            default:
                sender.sendMessage("Invalid sub command!");
                sender.sendMessage(command.getUsage());
                return false;
        }
        return true;
    }
}
