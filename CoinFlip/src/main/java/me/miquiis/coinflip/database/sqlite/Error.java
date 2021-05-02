package me.miquiis.coinflip.database.sqlite;

import me.miquiis.coinflip.CoinFlip;

import java.util.logging.Level;

public class Error {
    public static void execute(CoinFlip plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(CoinFlip plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}