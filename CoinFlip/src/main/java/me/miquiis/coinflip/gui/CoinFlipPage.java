package me.miquiis.coinflip.gui;

import me.miquiis.coinflip.CoinFlip;
import me.miquiis.coinflip.database.PlayerData;
import me.miquiis.coinflip.match.Cache;
import me.miquiis.coinflip.match.MatchData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoinFlipPage extends CustomHolder {

    private final int page;

    public CoinFlipPage(String title, int size, int page)
    {
        super(size, title);
        this.page = page;
    }

    public Inventory getInventory(Player player) {
        final Cache cache = CoinFlip.getInstance().getCache();
        final PlayerData playerData = cache.getPlayerData(player.getUniqueId());
        final Icon blackPanel = new Icon(CustomItem.createItem(Material.GRAY_STAINED_GLASS_PANE, "§f", new ArrayList<>()));
        final Icon previousPage = new Icon(CustomItem.createItem(Material.PAPER, "§c§lPrevious Page", new ArrayList<String>(){{add("§7Click to go to the previous page.");}}));
        final Icon nextPage = new Icon(CustomItem.createItem(Material.PAPER, "§2§lNext Page", new ArrayList<String>(){{add("§7Click to go to the next page.");}}));
        final Icon createGame = new Icon(CustomItem.createItem(Material.NETHER_STAR, "§a§lCreate Game", new ArrayList<String>(){{add("§7Click to go to the create a new game.");}}));
        final Icon stats = new Icon(CustomItem.createItem(Material.BOOK, "§6§lStatistics", new ArrayList<String>(){{add(" "); add("§fHere you can view your statistics"); add("§ffrom all your CoinFlip games."); add(" "); add(" §ePlayer: §f" + player.getName()); add(" "); add(" §eWins: §f" + playerData.getWins()); add(" §eLosses: §f" + playerData.getLosses()); add(" §eProfit: §f" + playerData.getProfitsFormatted()); add(" §eWin Percentage: §f" + playerData.getWinPerc());}}));

        super.setIcon(27, blackPanel);
        super.setIcon(28, blackPanel);
        super.setIcon(29, blackPanel);
        super.setIcon(33, blackPanel);
        super.setIcon(34, blackPanel);

        super.setIcon(30, previousPage.addClickAction(p -> {
            if (page == 1) return;
            CoinFlipPage coinFlipPage = new CoinFlipPage("§7§lCoinFlip Games", 9*4, page - 1);
            p.openInventory(coinFlipPage.getInventory(p));
        }));
        super.setIcon(32, nextPage.addClickAction(p -> {
            if (cache.getMatches().size() < 27 * (page + 1)) return;
            CoinFlipPage coinFlipPage = new CoinFlipPage("§7§lCoinFlip Games", 9*4, page + 1);
            p.openInventory(coinFlipPage.getInventory(p));
        }));

        super.setIcon(31, stats);

        super.setIcon(35, createGame.addClickAction(p -> {
            if (cache.getMatch(p.getUniqueId()) != null)
            {
                p.sendMessage("§6[§e§lCoinFlip§6]§c You already have a bet placed.");
                return;
            }
            CoinFlipCreation coinFlipCreation = new CoinFlipCreation("§7§lGame Builder", 9*3);
            p.openInventory(coinFlipCreation.getInventory(p));
        }));

        for (int slot = 0; slot < 27; slot++)
        {

            final ArrayList<MatchData> matches = cache.getMatches();
            final Integer index = slot + (27 * (page - 1));

            if (matches.size() <= index) break;

            final MatchData matchData = matches.get(index);

            final OfflinePlayer creator = Bukkit.getOfflinePlayer(matchData.getMatchCreator());

            final Icon head;

            if (!matchData.hasStarted())
            {
                head = new Icon(CustomItem.createSkull(creator, "§e" + creator.getName(), new ArrayList<String>(){{add(""); add("§e§lWager"); add("  §f§l" + matchData.getFormattedBetPrice()); add(""); add("§7§oClick here to start the CoinFlip game.");}}));
            }
            else
            {
                head = new Icon(CustomItem.createSkull(creator, "§e" + creator.getName(), new ArrayList<String>(){{add(""); add("§e§lWager"); add("  §f§l" + matchData.getFormattedBetPrice()); add(""); add("§c§oThis CoinFlip match is already in progress.");}}));
            }

            super.setIcon(slot, head.addClickAction(p -> {
                if (matchData.hasStarted()) return;
                if (matchData.getMatchCreator().equals(p.getUniqueId())) return;

                final Economy econ = CoinFlip.getInstance().getEcon();

                Double balance = econ.getBalance(p);
                if (balance < matchData.getBetPrice())
                {
                    p.sendMessage("§6[§e§lCoinFlip§6]§c You don't have enough money to play the bet.");
                    return;
                }

                matchData.setMatchAgainst(p.getUniqueId());
                matchData.startMatch();
                econ.withdrawPlayer(p, matchData.getBetPrice());
            }));
        }

        return super.getInventory();
    }

}
