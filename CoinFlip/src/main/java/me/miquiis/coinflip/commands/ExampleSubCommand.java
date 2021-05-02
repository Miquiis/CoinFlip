package me.miquiis.coinflip.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ExampleSubCommand extends mSubCommand {
    @Override
    public String getName() {
        return "example";
    }

    @Override
    public String getDescription() {
        return "Example of sub command.";
    }

    @Override
    public String getSyntax() {
        return "/example example";
    }

    @Override
    public Boolean canConsole() {
        return true;
    }

    @Override
    public String getPermission() {
        return "example.example";
    }

    @Override
    public ArrayList<mSubCommand> getSubcommand() {
        return null;
    }

    @Override
    public void perform(mCommand main, CommandSender player, ArrayList<String> args) {
        if (args.size() == 1)
        {

        }
        else
        {
            player.sendMessage("§aTry using this syntax: §e" + getSyntax());
        }
    }
}
