package me.miquiis.coinflip;

import me.miquiis.coinflip.commands.CommandManager;
import me.miquiis.coinflip.database.Database;
import me.miquiis.coinflip.events.EventsHandler;
import me.miquiis.coinflip.match.Cache;
import me.miquiis.coinflip.utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;

public class CoinFlip extends JavaPlugin {

    private static CoinFlip instance;

    private Economy econ = null;

    private Database database;

    private Config customConfig;

    private CommandManager commandManager;

    private EventsHandler eventsHandler;

    private Cache cache;

    private BukkitTask autoSave;

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        instance = this;

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        cache = new Cache();

        database = new Database(this);

        customConfig = new Config(this);

        commandManager = new CommandManager(this);

        eventsHandler = new EventsHandler(this);

        initTasks();
    }

    @Override
    public void onDisable()
    {
        stopTasks();

        if (commandManager != null) commandManager.close();
        commandManager = null;
        eventsHandler = null;

        instance = null;
    }

    private void initTasks()
    {
        autoSave = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {

            if (cache.getPlayers() != null) {
                database.savePlayers(cache.getPlayers());
            }

        }, 20 * 60, 20*60);
    }

    private void stopTasks()
    {
        if (autoSave != null) autoSave.cancel();
        autoSave = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static CoinFlip getInstance()
    {
        return instance;
    }

    public Economy getEcon()
    {
        return econ;
    }

    public Config getCustomConfig()
    {
        return customConfig;
    }

    public Cache getCache()
    {
        return cache;
    }

}
