package me.drmayx.voxelmoneymaker.voxelversemoneymaker.commands;

import me.drmayx.voxelmoneymaker.voxelversemoneymaker.VoxelVerseMoneyMaker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class MoneyMakerCommand implements CommandExecutor {

    private VoxelVerseMoneyMaker plugin;

    public MoneyMakerCommand(VoxelVerseMoneyMaker p){
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("moneymaker.command")){
            sender.sendMessage("§4You do not have permission to use that command§r");
            return true;
        }
        if(args.length < 1){
            return false;
        }

        switch(args[0]){
            case "reload":
                sender.sendMessage("Reloading the plugin ...");
                System.out.println("Reloading the plugin. Caused by " + sender.getName());
                Bukkit.getPluginManager().disablePlugin(plugin);
                Bukkit.getPluginManager().enablePlugin(plugin);
                break;
            default:
                sender.sendMessage("Invalid sub command!");
                sender.sendMessage(command.getUsage());
                return false;
        }
        return true;
    }
}
