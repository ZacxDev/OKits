package me.Zacx.OKits.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zacx.OKits.Main.OKit;

public class KitMenuPage {

	public Inventory inv;
	private KitMenu menu;
	private ItemStack gPane, pArrow, nArrow;
	
	public List<OKit> kits = new ArrayList<OKit>();
	private Player p;
	
	public KitMenuPage(KitMenu m, Player p) {
		menu = m;
		m.pages.add(this);
		inv = Bukkit.createInventory(null, 36, "Kits");
		this.p = p;
		
		gPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
		ItemMeta meta = gPane.getItemMeta();
		meta.setDisplayName(" ");
		gPane.setItemMeta(meta);
		
		pArrow = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
		meta = pArrow.getItemMeta();
		meta.setDisplayName("§f§lPrevious Page");
		pArrow.setItemMeta(meta);
		
		nArrow = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
		meta = nArrow.getItemMeta();
		meta.setDisplayName("§f§lNext Page");
		nArrow.setItemMeta(meta);
	}
	
	public void open(Player p) {
		this.p = p;
		p.openInventory(inv);
		menu.viewers.remove(p.getUniqueId());
		menu.viewers.add(p.getUniqueId());
	}
	
	public void tick() {
		if (inv != null && p != null) {
		UUID uid = p.getUniqueId();
		inv.setItem(0, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData()));
		
		for (int i = 0; i < inv.getSize(); i++) {
			
			if (i == 8 || i == 17 || i == 26 || i == 35 || i > 26 || i < 10) {
				inv.setItem(i, gPane);
			}
			if (i % 9 == 0 && i < 36) {
				inv.setItem(i, gPane);
			}
			if (i == 30) {
				inv.setItem(i, pArrow);
			} else if (i == 32) {
				inv.setItem(i, nArrow);
			}
			
		}
		
		int n = 0;
		for (int i = 10; i < 26; i++) {
			if (i != 17 && i != 18) {
			OKit kit = null;
			if (n < kits.size())
			kit = kits.get(n);
			else
				break;
			
			if (!p.hasPermission(kit.getPermission())){
				inv.setItem(i, kit.getRedPane());
				n++;
				continue;
			}				
			
			if (!kit.cooldowns.containsKey(uid))
				inv.setItem(i, kit.getItem(p));
			else {
				long cd = kit.getCooldown(uid);	
				inv.clear(i);
				inv.setItem(i, kit.getOrangePane(OKit.parseCooldown(cd)));
			}
			n++;
			}
		}
		}
	
	}
	
}
