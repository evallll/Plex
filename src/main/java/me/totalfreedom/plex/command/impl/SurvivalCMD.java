package me.totalfreedom.plex.command.impl;

import java.util.Collections;
import java.util.List;
import me.totalfreedom.plex.command.PlexCommand;
import me.totalfreedom.plex.command.annotation.CommandParameters;
import me.totalfreedom.plex.command.annotation.CommandPermissions;
import me.totalfreedom.plex.command.exception.CommandFailException;
import me.totalfreedom.plex.command.source.CommandSource;
import me.totalfreedom.plex.command.source.RequiredCommandSource;
import me.totalfreedom.plex.rank.enums.Rank;
import me.totalfreedom.plex.util.PlexUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = RequiredCommandSource.ANY)
@CommandParameters(aliases = "gms", description = "Set your own or another player's gamemode to survival mode")
public class SurvivalCMD extends PlexCommand
{
    public SurvivalCMD()
    {
        super("survival");
    }

    @Override
    public void execute(CommandSource sender, String[] args)
    {
        if (args.length == 0)
        {
            // doesn't work
            if (isConsole())
            {
                throw new CommandFailException("You must define a player when using the console!");
            }

            sender.getPlayer().setGameMode(GameMode.SURVIVAL);
            send(tl("gameModeSetTo", "survival"));
            return;
        }

        if (isAdmin(sender.getPlexPlayer()))
        {
            if (args[0].equals("-a"))
            {
                for (Player targetPlayer : Bukkit.getServer().getOnlinePlayers())
                {
                    targetPlayer.setGameMode(GameMode.SURVIVAL);
                }
                send(tl("gameModeSetTo", "survival"));
                return;
            }

            Player player = getNonNullPlayer(args[0]);
            send(tl("setOtherPlayerGameModeTo", player.getName(), "survival"));
            player.sendMessage(tl("playerSetOtherGameMode", sender.getName(), "survival"));
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args)
    {
        if (isAdmin(sender.getPlexPlayer()))
        {
            return PlexUtils.getPlayerNameList();
        }
        return Collections.emptyList();
    }
}