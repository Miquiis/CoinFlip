package me.miquiis.coinflip.gui;

import me.miquiis.coinflip.CoinFlip;
import me.miquiis.coinflip.match.Cache;
import me.miquiis.coinflip.match.MatchData;
import me.miquiis.coinflip.utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CoinFlipCreation extends CustomHolder {

    private Inventory inventory;
    private MatchData matchData;

    public CoinFlipCreation(String title, int size)
    {
        super(size, title);
    }

    public void updateInformation()
    {
        final Icon information = new Icon(CustomItem.createItem(Material.OAK_SIGN, "§f§lCurrency", new ArrayList<String>(){{add(" "); add("§7Amount Bet: §f" + matchData.getFormattedBetPrice());}}));
        setIcon(13, information);
        inventory.setItem(13, information.getItem());
    }

    public Inventory getInventory(Player player) {
        matchData = new MatchData(player.getUniqueId(), 0.0);

        final Cache cache = CoinFlip.getInstance().getCache();
        final Config config = CoinFlip.getInstance().getCustomConfig();

        final Economy econ = CoinFlip.getInstance().getEcon();

        final NumberFormat fmt = NumberFormat.getCurrencyInstance();

        final Integer minBet = config.getMinimumBet(), maxBet = config.getMaximumBet();

        final String minBetFormatt = fmt.format(minBet).substring(0, fmt.format(minBet).length() - 3);;

        final String maxBetFormatt = fmt.format(maxBet).substring(0, fmt.format(maxBet).length() - 3);

        final Icon blackPanel = new Icon(CustomItem.createItem(Material.GRAY_STAINED_GLASS_PANE, "§f", new ArrayList<>()));
        final Icon createGame = new Icon(CustomItem.createItem(Material.LIME_DYE, "§a§lCreate Game", new ArrayList<String>(){{add("§7Click to go to the create the game.");}}));
        final Icon information = new Icon(CustomItem.createItem(Material.OAK_SIGN, "§f§lCurrency", new ArrayList<String>(){{add(" "); add("§7Amount Bet: §f" + matchData.getFormattedBetPrice());}}));
        final Icon customAmount = new Icon(CustomItem.createItem(Material.ANVIL, "§6§lSet Custom Amount", new ArrayList<String>(){{add("§7Click to enter a value in chat!");}}));

        final ItemStack greenHead = CustomItem.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19", "", new ArrayList<>());
        final ItemStack redHead = CustomItem.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=", "", new ArrayList<>());

        final Icon plus1000 = new Icon(CustomItem.customizeItem(new ItemStack(greenHead), "§a§l+1000", new ArrayList<>()));
        final Icon plus500 = new Icon(CustomItem.customizeItem(new ItemStack(greenHead), "§a§l+500", new ArrayList<>()));
        final Icon plus100 = new Icon(CustomItem.customizeItem(new ItemStack(greenHead), "§a§l+100", new ArrayList<>()));

        final Icon minus1000 = new Icon(CustomItem.customizeItem(new ItemStack(redHead), "§c§l-1000", new ArrayList<>()));
        final Icon minus500 = new Icon(CustomItem.customizeItem(new ItemStack(redHead), "§c§l-500", new ArrayList<>()));
        final Icon minus100 = new Icon(CustomItem.customizeItem(new ItemStack(redHead), "§c§l-100", new ArrayList<>()));

        super.setIcon(10, plus100.addClickAction(p -> {
            matchData.addBetPrice(100.0);
            updateInformation();
        }));
        super.setIcon(11, plus500.addClickAction(p -> {
            matchData.addBetPrice(500.0);
            updateInformation();
        }));
        super.setIcon(12, plus1000.addClickAction(p -> {
            matchData.addBetPrice(1000.0);
            updateInformation();
        }));

        super.setIcon(13, information);

        super.setIcon(14, minus1000.addClickAction(p -> {
            matchData.removeBetPrice(1000.0);
            updateInformation();
        }));
        super.setIcon(15, minus500.addClickAction(p -> {
            matchData.removeBetPrice(500.0);
            updateInformation();
        }));
        super.setIcon(16, minus100.addClickAction(p -> {
            matchData.removeBetPrice(100.0);
            updateInformation();
        }));

        super.setIcon(18, customAmount.addClickAction(p -> {
            p.sendMessage(String.format("§6[§e§lCoinFlip§6]§a Please enter a value between §e%s§a and §e%s§a. Type 'cancel' to cancel.", minBetFormatt, maxBetFormatt));
            cache.cacheChat(matchData);
            p.closeInventory();
        }));

        super.setIcon(19, blackPanel);
        super.setIcon(20, blackPanel);
        super.setIcon(21, blackPanel);
        super.setIcon(22, blackPanel);
        super.setIcon(23, blackPanel);
        super.setIcon(24, blackPanel);
        super.setIcon(25, blackPanel);

        super.setIcon(26, createGame.addClickAction(p -> {

            final Double balance = econ.getBalance(p);

            if (matchData.getBetPrice() < minBet)
            {
                p.sendMessage(String.format("§6[§e§lCoinFlip§6]§c The minimum amount of bet is §e%s§c.", minBetFormatt));
                return;
            }

            if (matchData.getBetPrice() > maxBet)
            {
                p.sendMessage(String.format("§6[§e§lCoinFlip§6]§c The maximum amount of bet is §e%s§c.", maxBetFormatt));
                return;
            }

            if (balance < matchData.getBetPrice())
            {
                p.sendMessage("§6[§e§lCoinFlip§6]§c You don't have enough money to create the bet.");
                p.closeInventory();
                return;
            }

            CoinFlip.getInstance().getCache().cacheMatch(matchData);
            p.closeInventory();
            p.sendMessage("§6[§e§lCoinFlip§6]§f Created CoinFlip match betting §e" + matchData.getFormattedBetPrice() + "§f!");
            econ.withdrawPlayer(p, matchData.getBetPrice());
        }));

        this.inventory = super.getInventory();
        matchData.setCreationInventory(this.inventory);
        return inventory;
    }

}
