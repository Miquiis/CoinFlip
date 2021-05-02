package me.miquiis.coinflip.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public abstract class mCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract String getPermissionMessage();

    public abstract TextComponent getHelp();

    public abstract ArrayList<mSubCommand> getSubcommand();

    public abstract void perform(CommandSender player, ArrayList<String> args);

}
