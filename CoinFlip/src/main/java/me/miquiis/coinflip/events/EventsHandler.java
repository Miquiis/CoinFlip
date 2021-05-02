package me.miquiis.coinflip.events;

import me.miquiis.coinflip.CoinFlip;
import me.miquiis.coinflip.database.PlayerData;
import me.miquiis.coinflip.gui.CoinFlipCreation;
import me.miquiis.coinflip.gui.CustomHolder;
import me.miquiis.coinflip.gui.Icon;
import me.miquiis.coinflip.match.Cache;
import me.miquiis.coinflip.match.MatchData;
import me.miquiis.coinflip.utils.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class EventsHandler {

    private final CoinFlip plugin;

    private final ArrayList<CustomEventHandler> listeners = new ArrayList<CustomEventHandler>() {
        {
            add(new DatabaseHandler());
            add(new CustomGUIHandler());
            add(new ChatInputHandler());
        }
    };

    public EventsHandler(CoinFlip plugin)
    {
        this.plugin = plugin;

        for (CustomEventHandler listener : listeners)
        {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            listener.init(plugin);
        }
    }

    public void reload()
    {
        listeners.forEach(list -> {
            list.reload(plugin);
        });
    }

}

abstract class CustomEventHandler implements Listener {

    public abstract void init(CoinFlip plugin);

    public abstract void reload(CoinFlip plugin);

}

class DatabaseHandler extends CustomEventHandler {

    private Cache cache;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final PlayerData playerData = cache.getPlayerData(e.getPlayer().getUniqueId());

        if (playerData == null)
        {
            cache.cachePlayer(new PlayerData(e.getPlayer().getUniqueId(),0, 0, 0.0));
        }
    }

    @Override
    public void init(CoinFlip plugin) {
        cache = plugin.getCache();
    }

    @Override
    public void reload(CoinFlip plugin) {
        cache = plugin.getCache();
    }
}

class ChatInputHandler extends CustomEventHandler {

    private CoinFlip plugin;
    private Cache cache;
    private Config config;

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e)
    {
        if (cache.getChat(e.getPlayer().getUniqueId()) != null)
        {
            e.setCancelled(true);

            final MatchData matchData = cache.getChat(e.getPlayer().getUniqueId());

            cache.uncacheChat(matchData);

            if (e.getMessage().equalsIgnoreCase("cancel"))
            {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    e.getPlayer().openInventory(matchData.getCreationInventory());
                });
                return;
            }

            try {
                Integer amount = Integer.valueOf(e.getMessage());

                if (amount >= config.getMinimumBet() && amount <= config.getMaximumBet())
                {
                    matchData.setBetPrice(amount.doubleValue());
                }
                else
                {
                    e.getPlayer().sendMessage("§6[§e§lCoinFlip§6]§c You must enter a valid integer value.");
                }
            } catch (Exception exception)
            {
                e.getPlayer().sendMessage("§6[§e§lCoinFlip§6]§c You must enter a valid integer value.");
            }

            CoinFlipCreation creationPage = (CoinFlipCreation) matchData.getCreationInventory().getHolder();
            creationPage.updateInformation();

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                e.getPlayer().openInventory(matchData.getCreationInventory());
            });
            return;
        }
    }

    @Override
    public void init(CoinFlip plugin) {
        this.plugin = plugin;
        cache = plugin.getCache();
        config = plugin.getCustomConfig();
    }

    @Override
    public void reload(CoinFlip plugin) {
        this.plugin = plugin;
        cache = plugin.getCache();
        config = plugin.getCustomConfig();
    }
}

class CustomGUIHandler extends CustomEventHandler {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getClickedInventory() == null) return;
        if (e.getInventory() == null) return;

        if (!(e.getView().getTopInventory().getHolder() instanceof CustomHolder)) return;

        if (e.getClickedInventory().getHolder() instanceof CustomHolder)
        {
            e.setCancelled(true);

            if (e.getWhoClicked() instanceof Player)
            {
                Player player = (Player)e.getWhoClicked();

                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

                CustomHolder customHolder = (CustomHolder)e.getView().getTopInventory().getHolder();

                Icon icon = customHolder.getIcon(e.getRawSlot());
                if (icon == null) return;

                icon.clickActions.forEach(c -> c.execute(player));

            }
        }
        else
        {
            if (e.isRightClick()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e)
    {
        if (e.getInventory() == null) return;

        if (e.getInventory().getHolder() instanceof CustomHolder)
        {
            e.setCancelled(true);
        }
    }

    @Override
    public void init(CoinFlip plugin) {

    }

    @Override
    public void reload(CoinFlip plugin) {

    }
}