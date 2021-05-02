package me.miquiis.coinflip.match;

import me.miquiis.coinflip.database.PlayerData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Cache {

    private Map<UUID, MatchData> currentMatches = new ConcurrentHashMap();

    private Map<UUID, MatchData> chatAwait = new ConcurrentHashMap<>();

    private Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public Cache(){};

    public List<PlayerData> getPlayers()
    {
        return players.values().stream().collect(Collectors.toList());
    }

    public PlayerData getPlayerData(UUID player)
    {
        return players.get(player);
    }

    public void cachePlayer(PlayerData playerData)
    {
        players.put(playerData.getUUID(), playerData);
    }

    public void uncachePlayer(UUID player)
    {
        players.remove(player);
    }

    public void cacheMatch(MatchData matchData)
    {
        currentMatches.put(matchData.getMatchCreator(), matchData);
    }

    public MatchData getChat(UUID chatter) { return chatAwait.get(chatter); }

    public void cacheChat(MatchData matchData) { chatAwait.put(matchData.getMatchCreator(), matchData); }

    public void uncacheChat(MatchData matchData) { chatAwait.remove(matchData.getMatchCreator()); }

    public MatchData getMatch(UUID creator)
    {
        return currentMatches.get(creator);
    }

    public void endMatch(UUID creator)
    {
        currentMatches.remove(creator);
    }

    public ArrayList<MatchData> getMatches()
    {
        return new ArrayList<MatchData>(currentMatches.values());
    }

}
