package me.miquiis.coinflip.utils;

import me.miquiis.coinflip.CoinFlip;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private CoinFlip plugin;
    private FileConfiguration config;

    public Config(CoinFlip plugin)
    {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getErrorPrefix()
    {
        return color(config.getString("Messages.command.error-prefix"));
    }

    public String getSuccessPrefix()
    {
        return color(config.getString("Messages.command.success-prefix"));
    }

    public String getCommand(String name)
    {
        return getSuccessPrefix() + " " + color(config.getString("Messages.command." + name));
    }

    public String getErrorCommand(String name)
    {
        return getErrorPrefix() + " " + color(config.getString("Messages.command." + name));
    }

    public Integer getMinimumBet()
    {
        return config.getInt("CoinFlip.match-settings.minimum-bet");
    }
    public Integer getMaximumBet()
    {
        return config.getInt("CoinFlip.match-settings.maximum-bet");
    }
    
    public String color(String message)
    {
        return message.replace("&", "§").replace("\n", "\n").replace("\\n", "\n");
    }

}
