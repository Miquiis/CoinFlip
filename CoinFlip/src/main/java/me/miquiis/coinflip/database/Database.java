package me.miquiis.coinflip.database;

import com.google.gson.Gson;
import me.miquiis.coinflip.CoinFlip;
import me.miquiis.coinflip.database.sqlite.Errors;
import me.miquiis.coinflip.database.sqlite.SDatabase;
import me.miquiis.coinflip.database.sqlite.SQLite;
import me.miquiis.coinflip.match.Cache;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Database {

    private static SDatabase db;
    private CoinFlip main;
    private Cache cache;

    public Database(CoinFlip plugin)
    {
        main = plugin;

        this.cache = plugin.getCache();

        db = new SQLite(plugin);
        db.load();

        initDatabase();
    }

    private void initDatabase()
    {
        try {
            Statement s = db.getSQLConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS users (uuid CHAR(36) NOT NULL UNIQUE, information LONGTEXT NOT NULL);");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        retrieveData();
    }

    private void retrieveData()
    {
        cachePlayers();
    }

    public void cachePlayers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM users;");
            rs = ps.executeQuery();
            while(rs.next()){
                Gson gson = new Gson();
                cache.cachePlayer(gson.fromJson(rs.getString("information"), PlayerData.class));
            }
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void purgeUsers()
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM users");
            ps.execute();
            return;
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void removeUser(UUID uuid)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM users WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ps.execute();
            return;
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void savePlayers(List<PlayerData> playerData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            final Gson gson = new Gson();
            conn = db.getSQLConnection();
            for (PlayerData pd : playerData)
            {
                ps = conn.prepareStatement("INSERT OR REPLACE INTO users (uuid,information) VALUES(?,?);");
                ps.setString(1, pd.getUUID().toString());
                ps.setString(2, gson.toJson(pd));
                ps.executeUpdate();
            }
            return;
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void close(CoinFlip instance)
    {
        if (cache.getPlayers() != null)
            savePlayers(cache.getPlayers());

        try {
            db.getSQLConnection().close();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        db = null;
    }
}
