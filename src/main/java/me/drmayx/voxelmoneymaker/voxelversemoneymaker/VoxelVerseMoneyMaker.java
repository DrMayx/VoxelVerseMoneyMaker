package me.drmayx.voxelmoneymaker.voxelversemoneymaker;

import com.earth2me.essentials.Essentials;
import me.drmayx.voxelmoneymaker.voxelversemoneymaker.commands.MoneyMakerCommand;
import me.drmayx.voxelmoneymaker.voxelversemoneymaker.config.Config;
import me.drmayx.voxelmoneymaker.voxelversemoneymaker.payout.Payout;
import me.drmayx.voxelmoneymaker.voxelversemoneymaker.utils.ActionBarUtils;
import net.milkbowl.vault.economy.Economy ;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class VoxelVerseMoneyMaker extends JavaPlugin {

    private static String CONFIG_PATH = "plugins/VoxelVerseMoneyMaker/config.yml";

    private Economy economy = null;
    private Essentials essentials = null;

    private List<Payout> payouts = null;
    private long payoutInterval = (long) Double.POSITIVE_INFINITY;

    private int setupTaskId;
    private int payoutTaskId;

    private int setupCounter = 0;

    private YamlConfiguration config = null;

    @Override
    public void onEnable() {

        getCommand("moneymaker").setExecutor(new MoneyMakerCommand(this));

        File configFile = new File(CONFIG_PATH);

        if(!configFile.exists())
        {
            config = Config.generateConfig(this);
        }
        else{
            config = YamlConfiguration.loadConfiguration(configFile);
        }

        setUpDataFromConfig();

        setupTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::setupNecessaryPlugins, 40L, 20L);

        payoutTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::giveMoneyEachIteration, 80L, payoutInterval * 20L);
    }

    @Override
    public void onDisable() {
        if(setupTaskId != -1 && (Bukkit.getScheduler().isQueued(setupTaskId) || Bukkit.getScheduler().isCurrentlyRunning(setupTaskId))){
            Bukkit.getScheduler().cancelTask(setupTaskId);
        }

        if(payoutTaskId != -1 && (Bukkit.getScheduler().isQueued(payoutTaskId) || Bukkit.getScheduler().isCurrentlyRunning(payoutTaskId))){
            Bukkit.getScheduler().cancelTask(payoutTaskId);
        }
    }

    public void setUpDataFromConfig(){

        payouts = new ArrayList<>();

        payoutInterval = config.getLong("payout_interval");

        for(String key : config.getConfigurationSection("payouts").getKeys(false)){
            Payout payout = new Payout();
            payout.payoutAmount = config.getDouble("payouts." + key + ".payout_amount");
            String permission = config.getString("payouts." + key + ".permission");
            payout.permission = (permission == null) || permission.isEmpty() ? "" : permission;
            String payoutMessage = config.getString("payouts." + key + ".message");
            payout.message = (payoutMessage == null) || payoutMessage.isEmpty() ? "" : payoutMessage;
            payouts.add(payout);
        }
    }

    private void setupNecessaryPlugins(){
        boolean ecoDone = false;
        boolean essDone = false;
        setupCounter +=1;

        if(setupEconomy()){
            ecoDone = true;
        }

        if(setupEssentials()){
            essDone = true;
        }

        if(ecoDone && essDone){
            Bukkit.getScheduler().cancelTask(setupTaskId);
        }else if(setupCounter >= 5){
            String message = "";
            if(!ecoDone){
                message += "Could not load Essentials plugin.\n";
            }
            if(!essDone){
                message += "Could not load Essentials economy plugin.\n";
            }
            message += message.isEmpty() ? "An error occurred during setup.\nDisabling the plugin." : "Disabling the plugin.";
            System.out.println(message);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return economy != null;
    }

    private boolean setupEssentials(){
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")){
            essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            return true;
        }
        return false;
    }

    private void giveMoneyEachIteration()
    {
        Payout finalPayout = null;
        for(Player player : getServer().getOnlinePlayers()){
            if(!essentials.getUser(player).isAfk() && player.hasPermission("moneymaker.earn")){
                finalPayout = getPayoutForPlayer(player);

                if(finalPayout == null){continue;}

                economy.depositPlayer(player, finalPayout.payoutAmount);

                ActionBarUtils.sendActionBarMessage(player, finalPayout.message);
            }
        }
    }

    private Payout getPayoutForPlayer(Player player){
        Payout finalPayout = null;

        for (Payout payout : payouts) {
            if (payout.permission.equalsIgnoreCase("")) {
                finalPayout = payout;
            }else if (player.hasPermission(payout.permission)) {
                finalPayout = payout;
            }
        }

        return finalPayout;
    }
}
