package me.drmayx.voxelmoneymaker.voxelversemoneymaker.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

    public static YamlConfiguration generateConfig(JavaPlugin plugin){
        YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
        config.options().header("All settings require server restart.\n" +
                                "payout_interval - enter the interval between payouts in seconds;\n" +
                                "Use this list to set payouts for each player level.\n" +
                                "You can use any permission name you want. e.g. myserver.donor");

        config.set("payout_interval", 600);

        config.set("payouts.defaultPayout.payout_amount", 5);
        config.set("payouts.defaultPayout.permission", "moneymaker.basic");
        config.set("payouts.defaultPayout.message", "You were paid 5 dollars");
        config.set("payouts.vipPayout.payout_amount", 100);
        config.set("payouts.vipPayout.permission", "moneymaker.vip");
        config.set("payouts.vipPayout.message", "You were paid 100 dollars");
        config.set("payouts.125.payout_amount", 125);
        config.set("payouts.125.permission", "custompermission.125");
        config.set("payouts.125.message", "You were paid 125 dollars");

        plugin.saveConfig();
        return (YamlConfiguration) plugin.getConfig();
    }

}
