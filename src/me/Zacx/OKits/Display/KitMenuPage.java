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

import me.Zacx.OKits.Main.Access;
import me.Zacx.OKits.Main.Core;
import me.Zacx.OKits.Main.OKit;

public class KitMenuPage {

	public Inventory inv;
	private KitMenu menu;
	private ItemStack border, pArrow, nArrow;
	private byte borderData;
	private int n = 1;

	public List<OKit> kits = new ArrayList<OKit>();
	private Player p;

	private Core c;

	public KitMenuPage(KitMenu m, Player p) {

		c = Access.c;

		menu = m;
		m.pages.add(this);
		inv = Bukkit.createInventory(null, 36, "Kits");
		this.p = p;

		borderData = (byte) 0;
		border = new ItemStack(Material.STAINED_GLASS_PANE, 1, borderData);
		ItemMeta meta = border.getItemMeta();
		meta.setDisplayName(" ");
		border.setItemMeta(meta);

		pArrow = new ItemStack(Material.STAINED_GLASS_PANE, 1,
				DyeColor.BLACK.getData());
		meta = pArrow.getItemMeta();
		meta.setDisplayName("§f§lPrevious Page");
		pArrow.setItemMeta(meta);

		nArrow = new ItemStack(Material.STAINED_GLASS_PANE, 1,
				DyeColor.BLACK.getData());
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
			if (borderData > 12 || borderData < 0)
				n *= -1;
			borderData = (byte) (borderData + n);
			border = new ItemStack(Material.STAINED_GLASS_PANE, 1, borderData);
			inv.setItem(0, border);

			for (int i = 0; i < inv.getSize(); i++) {

				if (i == 8 || i == 17 || i == 26 || i == 35 || i > 26
						|| i < 10) {
					inv.setItem(i, border);
				}
				if (i % 9 == 0 && i < 36) {
					inv.setItem(i, border);
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

					if (!p.hasPermission(kit.getPermission())) {
						inv.setItem(i, kit.getRedPane());
						n++;
						continue;
					}

					if (!kit.cooldowns.containsKey(uid)) {
						
						if (c.getConfig().getBoolean("UseItemTag"))
							inv.setItem(i, kit.getItem(p));
						else
							inv.setItem(i, kit.getGreenPane());
						
					} else {
						long cd = kit.getCooldown(uid);
						inv.clear(i);
						inv.setItem(i,
								kit.getOrangePane(OKit.parseCooldown(cd)));
					}
					n++;
				}
			}
		}

	}

}
