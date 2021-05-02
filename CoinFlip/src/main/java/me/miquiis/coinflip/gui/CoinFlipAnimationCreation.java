package me.miquiis.coinflip.gui;

import me.miquiis.coinflip.match.MatchData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CoinFlipAnimationCreation extends CustomHolder {

    private Inventory inventory;
    private MatchData matchData;

    private boolean isCreator = false;

    public CoinFlipAnimationCreation(MatchData matchData, String title, int size)
    {
        super(size, title);
        this.matchData = matchData;
    }

    private void updateIcon(Icon icon, Integer slot)
    {
        setIcon(slot, icon);
        inventory.setItem(slot, icon.getItem());
    }

    public void flipCoin()
    {
        isCreator = !isCreator;

        if (isCreator)
        {
            getAgainstFace();
        }
        else
        {
            getOwnerFace();
        }
    }

    public OfflinePlayer getWinner()
    {
        OfflinePlayer winner;
        if (isCreator) winner =  Bukkit.getOfflinePlayer(matchData.getMatchCreator());
        else winner = Bukkit.getOfflinePlayer(matchData.getMatchAgainst());

        return winner;
    }

    public void getWinnerFace(OfflinePlayer winner)
    {
        isCreator = !isCreator;

        final Icon head = new Icon(CustomItem.createSkull(winner, "§e" + winner.getName(), new ArrayList<>()));
        final Icon background = new Icon(CustomItem.createItem(Material.CYAN_STAINED_GLASS_PANE, "§f", new ArrayList<>()));

        for (int i = 0; i < super.getSize(); i++)
        {
            updateIcon(background, i);
        }

        updateIcon(head, 13);
    }

    public void getOwnerFace()
    {
        final OfflinePlayer header = Bukkit.getOfflinePlayer(matchData.getMatchCreator());
        final Icon head = new Icon(CustomItem.createSkull(header, "§e" + header.getName(), new ArrayList<>()));
        final Icon background = new Icon(CustomItem.createItem(Material.YELLOW_STAINED_GLASS_PANE, "§f", new ArrayList<>()));

        for (int i = 0; i < super.getSize(); i++)
        {
            updateIcon(background, i);
        }

        updateIcon(head, 13);
    }

    public void getAgainstFace()
    {
        final OfflinePlayer header = Bukkit.getOfflinePlayer(matchData.getMatchAgainst());
        final Icon head = new Icon(CustomItem.createSkull(header, "§e" + header.getName(), new ArrayList<>()));
        final Icon background = new Icon(CustomItem.createItem(Material.GRAY_STAINED_GLASS_PANE, "§f", new ArrayList<>()));

        for (int i = 0; i < super.getSize(); i++)
        {
            updateIcon(background, i);
        }

        updateIcon(head, 13);
    }

    public Inventory getInventory() {
        final OfflinePlayer header = Bukkit.getOfflinePlayer(matchData.getMatchCreator());
        final Icon head = new Icon(CustomItem.createSkull(header, "§e" + header.getName(), new ArrayList<>()));
        final Icon background = new Icon(CustomItem.createItem(Material.YELLOW_STAINED_GLASS_PANE, "§f", new ArrayList<>()));

        super.setIcon(13, head);

        for (int i = 0; i < super.getSize(); i++)
        {
            if (getIcon(i) == null)
            {
                super.setIcon(i, background);
            }
        }

        this.inventory = super.getInventory();
        return inventory;
    }

}
