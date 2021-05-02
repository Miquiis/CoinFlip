package me.miquiis.coinflip.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class CustomItem {

    public static ItemStack customizeItem(ItemStack item, String name, List<String> lores)
    {
        ItemMeta im = item.getItemMeta();

        lores.forEach(s -> lores.set(lores.indexOf(s), ChatColor.translateAlternateColorCodes('&', s)));

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        im.setLore(lores);

        item.setItemMeta(im);
        return item;
    }

    public static ItemStack createItem(Material material, String name, List<String> lores)
    {
        ItemStack ci = new ItemStack(material);
        ItemMeta im = ci.getItemMeta();

        lores.forEach(s -> lores.set(lores.indexOf(s), ChatColor.translateAlternateColorCodes('&', s)));

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        im.setLore(lores);

        ci.setItemMeta(im);
        return ci;
    }

   // public static ItemStack createPermissionItem()

    public static ItemStack createSkull(OfflinePlayer player, String name, List<String> lores)
    {
        ItemStack ci = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta im = ci.getItemMeta();

        SkullMeta skullMeta = (SkullMeta)im;
        skullMeta.setOwningPlayer(player);

        lores.forEach(s -> lores.set(lores.indexOf(s), ChatColor.translateAlternateColorCodes('&', s)));

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        im.setLore(lores);

        ci.setItemMeta(im);
        return ci;
    }

    public static ItemStack createSkull(String texture, String name, List<String> lores)
    {
        ItemStack ci = getSkull(texture);
        ItemMeta im = ci.getItemMeta();

        lores.forEach(s -> lores.set(lores.indexOf(s), ChatColor.translateAlternateColorCodes('&', s)));

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        im.setLore(lores);

        ci.setItemMeta(im);
        return ci;
    }

    private static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {

        }
        head.setItemMeta(headMeta);
        return head;
    }

}
