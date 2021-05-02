package me.miquiis.coinflip.match;


import me.miquiis.coinflip.CoinFlip;
import me.miquiis.coinflip.database.PlayerData;
import me.miquiis.coinflip.gui.CoinFlipAnimationCreation;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.text.NumberFormat;
import java.util.UUID;

public class MatchData {

    private Inventory creationInventory;

    private UUID matchCreator;
    private UUID matchAgainst;

    private boolean hasStarted;

    private Double betPrice;
    private BukkitTask flipCoin;

    public MatchData(UUID matchCreator, Double betPrice)
    {
        this.matchCreator = matchCreator;
        this.betPrice = betPrice;

        this.hasStarted = false;
        this.matchAgainst = null;
    }

    public void setCreationInventory(Inventory inventory)
    {
        this.creationInventory = inventory;
    }

    public Inventory getCreationInventory()
    {
        return this.creationInventory;
    }

    public String getFormattedRewardPrice()
    {
        final NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String currency = fmt.format(getRewardPrice());
        return currency.substring(0, currency.length() - 3);
    }

    public String getFormattedBetPrice()
    {
        final NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String currency = fmt.format(getBetPrice());
        return currency.substring(0, currency.length() - 3);
    }

    public Double getRewardPrice()
    {
        return this.betPrice * 2;
    }

    public UUID getMatchCreator()
    {
        return matchCreator;
    }

    public void setMatchAgainst(UUID uuid)
    {
        this.matchAgainst = uuid;
    }

    public UUID getMatchAgainst()
    {
        return matchAgainst;
    }

    public Double getBetPrice()
    {
        return betPrice;
    }

    public void setBetPrice(Double betPrice)
    {
        this.betPrice = betPrice;
    }

    public void addBetPrice(Double betPrice)
    {
        this.betPrice += betPrice;
    }

    public void removeBetPrice(Double betPrice)
    {
        if (this.betPrice - betPrice < 0.0)
            this.betPrice = 0.0;
        else
            this.betPrice -= betPrice;
    }

    public void declareWinner(OfflinePlayer winner)
    {
        final Economy econ = CoinFlip.getInstance().getEcon();
        final Cache cache = CoinFlip.getInstance().getCache();

        OfflinePlayer loser;

        if (winner.getUniqueId().equals(matchCreator))
            loser = Bukkit.getOfflinePlayer(matchAgainst);
        else
            loser = Bukkit.getOfflinePlayer(matchCreator);

        final PlayerData loserData = cache.getPlayerData(loser.getUniqueId()), winnerData = cache.getPlayerData(winner.getUniqueId());

        econ.depositPlayer(winner, getRewardPrice());
        winnerData.addWin();
        winnerData.addProf(getRewardPrice());
        if (winner.getPlayer() != null)
        winner.getPlayer().sendMessage(
                "\n        §e§lGame Summary\n" +
                   "           §a§lWINNER\n" + " " + "\n" +
                   "§eTotal Bet: §f" + getFormattedRewardPrice() + "\n" +
                   "§eAmount Bet: §f" + getFormattedBetPrice() + "\n" +
                   "§eWinner: §a" + winner.getName() + "\n" +
                   "§eLoser: §c" + loser.getName() + "\n "
        );

        loserData.addLoss();
        if (loser.getPlayer() != null)
        loser.getPlayer().sendMessage(
                      "\n           §e§lGame Summary\n" +
                        "§c  You lost, better luck next time.\n" + " " + "\n" +
                        "§eTotal Bet: §f" + getFormattedRewardPrice() + "\n" +
                        "§eAmount Bet: §f" + getFormattedBetPrice() + "\n" +
                        "§eWinner: §a" + winner.getName() + "\n" +
                        "§eLoser: §c" + loser.getName() + "\n "
        );

        cache.endMatch(matchCreator);
    }

    public void startMatch()
    {
        this.hasStarted = true;

        CoinFlipAnimationCreation coinFlipAnimationCreation = new CoinFlipAnimationCreation(this, "§7§lFlipping Coin...", 9*3);
        Inventory inventory = coinFlipAnimationCreation.getInventory();

        Player player1 = Bukkit.getPlayer(matchCreator), player2 = Bukkit.getPlayer(matchAgainst);

        if (player1 != null) player1.openInventory(inventory);
        if (player2 != null) player2.openInventory(inventory);

        final int flipTimes = getRandomNumber(3, 8);

        flipCoin = CoinFlip.getInstance().getServer().getScheduler().runTaskTimer(CoinFlip.getInstance(), new Runnable() {
            int flipped = flipTimes;
            @Override
            public void run() {
                flipped--;

                if (flipped == 1)
                {
                    if (player1 != null) player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
                    if (player2 != null) player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
                    OfflinePlayer winner = coinFlipAnimationCreation.getWinner();
                    coinFlipAnimationCreation.getWinnerFace(winner);
                    declareWinner(winner);
                    return;
                }

                if (flipped == 0)
                {
                    if (player1 != null) player1.closeInventory();
                    if (player2 != null) player2.closeInventory();
                    flipCoin.cancel();
                    return;
                }

                coinFlipAnimationCreation.flipCoin();
                if (player1 != null) player1.playSound(player1.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1,1);
                if (player2 != null) player2.playSound(player2.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1,1);

            }
        }, 30L , 30L);

    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public boolean hasStarted()
    {
        return hasStarted;
    }

}
