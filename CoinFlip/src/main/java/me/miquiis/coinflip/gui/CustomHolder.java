package me.miquiis.coinflip.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomHolder implements InventoryHolder {

    private final Map<Integer, Icon> icons = new HashMap<>();

    private final int size;
    private final String title;

    public CustomHolder(int size, String title)
    {
        this.size = size;
        this.title = title;
    }

    public void setIcon(int slot, Icon icon)
    {
        this.icons.put(slot, icon);
    }

    public Icon getIcon(int slot)
    {
        return this.icons.get(slot);
    }

    public Integer getSize()
    {
        return size;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, this.size, this.title);

        for (Map.Entry<Integer, Icon> entry : this.icons.entrySet())
        {
            inventory.setItem(entry.getKey(), entry.getValue().getItem());
        }

        return inventory;
    }

}
