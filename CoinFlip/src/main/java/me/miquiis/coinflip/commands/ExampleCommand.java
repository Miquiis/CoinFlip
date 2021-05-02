package me.miquiis.coinflip.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ExampleCommand extends mCommand {

    @Override
    public String getName() {
        return "example";
    }

    @Override
    public String getDescription() {
        return "Example of a command.";
    }

    @Override
    public String getSyntax() {
        return "/exameple";
    }

    @Override
    public String getPermission() {
        return "exameple.usage";
    }

    @Override
    public String getPermissionMessage() {
        return "";
    }

    @Override
    public TextComponent getHelp() {
        TextComponent main = new TextComponent("§f[§b§lExample§r§f]§9 List of Commands and Usage:");

        for (mSubCommand subCommand : getSubcommand())
        {
            TextComponent subText = new TextComponent("\n§2> §a" + subCommand.getSyntax());
            subText.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    "§bCommand: §2" + subCommand.getName() + "\n" +
                          "§bDescription: §2" + subCommand.getDescription() + "\n" +
                          "§bUsage: §2" + subCommand.getSyntax() + "\n" +
                          "§bPermission: §2" + subCommand.getPermission()
            ).create() ) );
            subText.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, subCommand.getSyntax()));
            main.addExtra(subText);
        }

        return main;
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
