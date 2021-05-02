package me.miquiis.coinflip.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public abstract class mSubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract Boolean canConsole();

    public abstract ArrayList<mSubCommand> getSubcommand();

    public abstract void perform(mCommand main, CommandSender sender, ArrayList<String> args);

}

