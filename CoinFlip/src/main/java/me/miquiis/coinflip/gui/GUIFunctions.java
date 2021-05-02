package me.miquiis.coinflip.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIFunctions {

    public static void fillEmpty(Inventory inventory, ItemStack itemStack)
    {
        while (inventory.firstEmpty() != -1)
        {
            inventory.setItem(inventory.firstEmpty(), itemStack);
        }
    }

}
