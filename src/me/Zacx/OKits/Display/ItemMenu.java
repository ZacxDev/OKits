package me.Zacx.OKits.Display;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zacx.OKits.Main.Access;
import me.Zacx.OKits.Main.OKit;

public class ItemMenu implements Listener{

	private Inventory inv;
	private List<ItemStack> items;
	private ItemStack back;
	private Player p;
	
	public ItemMenu(OKit kit) {
		Access.c.getServer().getPluginManager().registerEvents(this, Access.c);
		items = kit.items;
		back = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("§f§lBack");
		back.setItemMeta(meta);
	}
	
	public void open(Player p) {
		
		this.p = p;
		inv = Bukkit.createInventory(p, 54, "Items");
		
		inv.setItem(0, back);
		for (int i = 0; i < items.size(); i++) {
			ItemStack item = items.get(i);
			inv.addItem(item);
		}
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		ItemStack item = e.getCurrentItem();
		
		if (e.getInventory().getName().equalsIgnoreCase(inv.getName())) {
			e.setCancelled(true);
			if (item != null && item.getType() == Material.STAINED_GLASS_PANE) {
				if (item.getData().getData() == DyeColor.BLACK.getData()) {
					KitMenu m = new KitMenu(Access.c);
					m.openKitMenu(p);
				}
			}
		}
		
	}
}