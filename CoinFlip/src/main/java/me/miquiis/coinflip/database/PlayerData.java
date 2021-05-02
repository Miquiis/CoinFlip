package me.miquiis.coinflip.database;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {

    private UUID player;

    private Integer wins, losses;
    private Double profits;

    public PlayerData(UUID player, Integer wins, Integer losses, Double profits)
    {
        this.player = player;
        this.wins = wins;
        this.losses = losses;
        this.profits = profits;
    }

    public OfflinePlayer getPlayer()
    {
        return Bukkit.getOfflinePlayer(player);
    }

    public UUID getUUID()
    {
        return player;
    }

    public String getWinPerc()
    {
        if (getTotalMatches() == 0) return "0.0%";
        return (getWins().doubleValue() / getTotalMatches()) * 100 + "%";
    }

    public String getProfitsFormatted()
    {
        final NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String currency = fmt.format(getProfits());
        return currency.substring(0, currency.length() - 3);
    }

    public Integer getTotalMatches()
    {
        return getWins() + getLosses();
    }

    public Integer getWins()
    {
        return wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public Double getProfits() {
        return profits;
    }

    public void addProf(Double profit)
    {
        this.profits += profit;
    }

    public void decProf(Double profit)
    {
        this.profits -= profit;
    }

    public void addWin()
    {
        this.wins++;
    }

    public void decWin()
    {
        this.wins--;
    }

    public void addLoss() {
        this.losses++;
    }

    public void decLoss() {
        this.losses--;
    }

}
