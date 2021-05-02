package me.miquiis.coinflip.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Icon {

    public ItemStack itemStack;

    public final List<ClickAction> clickActions = new ArrayList<>();

    public Icon(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public ItemStack getItem()
    {
        return this.itemStack;
    }

    public void updateItem(ItemStack item)
    {
        this.itemStack = item;
    }

    public Icon addClickAction(ClickAction clickAction)
    {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions()
    {
        return this.clickActions;
    }

}
