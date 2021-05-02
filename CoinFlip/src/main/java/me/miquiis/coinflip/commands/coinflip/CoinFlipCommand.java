package me.miquiis.coinflip.commands.coinflip;

import me.miquiis.coinflip.commands.mCommand;
import me.miquiis.coinflip.commands.mSubCommand;
import me.miquiis.coinflip.gui.CoinFlipPage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CoinFlipCommand extends mCommand {

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String getDescription() {
        return "Opens the GUI of the CoinFlip.";
    }

    @Override
    public String getSyntax() {
        return "/coinflip";
    }

    @Override
    public String getPermission() {
        return "coinflip.use";
    }

    @Override
    public String getPermissionMessage() {
        return "§cYou don't have enough permissions to execute this command.";
    }

    @Override
    public TextComponent getHelp() {
        return null;
    }

    @Override
    public ArrayList<mSubCommand> getSubcommand() {
        return new ArrayList<mSubCommand>(){
            {

            }
        };
    }

    @Override
    public void perform(CommandSender player, ArrayList<String> args) {
        if (!player.hasPermission(getPermission())) {
            player.sendMessage(getPermissionMessage());
            return;
        }

        if (args.size() == 0 && player instanceof Player)
        {
            Player p = (Player)player;
            CoinFlipPage coinFlipPage = new CoinFlipPage("§7§lCoinFlip Games", 9*4, 1);
            p.openInventory(coinFlipPage.getInventory(p));
            return;
        }

        if (args.size() > 0) {
            for (mSubCommand sub : getSubcommand()) {
                if (args.get(0).equalsIgnoreCase(sub.getName())) {
                    if (!player.hasPermission(sub.getPermission())) {
                        player.sendMessage(getPermissionMessage());
                        return;
                    }
                    args.remove(0);
                    sub.perform(this, player, args);
                    return;
                }
            }
        }
        player.spigot().sendMessage(getHelp());
        return;
    }
}
