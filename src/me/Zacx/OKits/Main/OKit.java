package me.Zacx.OKits.Main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;


public class OKit {


	public static LinkedList<OKit> kits = new LinkedList<OKit>();
	public LinkedHashMap<UUID, Long> cooldowns = new LinkedHashMap<UUID, Long>();
	

	public List<ItemStack> items = new ArrayList<ItemStack>();
	public String name, lore;
	public Material item;
	private Permission perm;
	private long cooldown;

	public OKit(String name, List<ItemStack> items) {
		kits.add(this);
		this.items = items;
	}
	
	public OKit() {
		kits.add(this);
	}
	
	
	public void tick() {
		List<UUID> list = new ArrayList<UUID>(cooldowns.keySet());
		for (int i = 0; i < cooldowns.size(); i++) {
			UUID uid = list.get(i);
			cooldowns.put(uid, cooldowns.get(uid) - 1);
			if (cooldowns.get(uid) <= 0)
				cooldowns.remove(uid);
		}
	}

	public void giveItems(Player p) {
		cooldowns.put(p.getUniqueId(), cooldown);
		Inventory inv = p.getInventory();
		//System.out.println("giveiing");
		for (int i = 0; i < items.size(); i++) {
			inv.addItem(items.get(i));
			//System.out.println(items.get(i).getItemMeta().getDisplayName());
		}
	}

	public void setProperty(String line) {
		if (!line.contains("Items:") && line.contains(":")) {
		String p = "";
		String s = line.substring(line.lastIndexOf(":") + 2);
		if (line.contains("Name")) {
			this.name = s;
			p = "Name";
		} else if (line.contains("Lore")) {
			this.lore = s;
			p = "Lore";
		} 
		else if (line.contains("Item")) {
			this.item = Material.getMaterial(s);
			p = "Item";
		} else if (line.contains("Cooldown")) {
			this.cooldown = Long.parseLong(s);
			p = "Cooldown";
		} else
			return;
		
		//System.out.println("Set Property: " + p + " to " + s);
	} else
		return;
	}
	
	public long getCooldown(UUID uid) {
		return cooldowns.get(uid);
	}
	
	public ItemStack getItem(Player p) {
		String cd = cooldowns.get(p.getUniqueId()) + "";
		ItemStack i = new ItemStack(item);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		l.add("§f" + lore);
		m.setLore(l);
		m.setDisplayName(name);
		i.setItemMeta(m);
		return i;
	}

	public ItemStack getRedPane() {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4§l" + name);
		List<String> lore = new ArrayList<String>();
		lore.add("§cYou do not have access to this kit!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public ItemStack getOrangePane(String cd) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4§l" + name);
		List<String> lore = new ArrayList<String>();
		lore.add("§cThis kit is cooling down!");
		lore.add("§e" + cd);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static String parseCooldown(double i) {
		double cd = i;
		StringBuilder sb = new StringBuilder();
		int s = 0, m = 0, h = 0;
		
		while (cd >= 1) {
			  if (cd / (60 * 60) >= 1) {
			//h
			h++;
			cd -= (60 * 60);
		} else if (cd / (60) >= 1) {
			//m
			m++;
			cd -= (60);
		} else {
			s = (int) cd;
			cd = 0;
		}
//		else if (cd / 20 >= 1) {
//			//s
//			if (cd / 20 > 1)
//			s++;
//			cd -= (20);
//		} 
		}
		sb.append(h + "h " + m + "m " + s + "s ");
		return sb.toString();
	}

	public static OKit getKit(String name) {
		String s = name;
		while (s.contains(ChatColor.COLOR_CHAR + "")) {
			int i = s.indexOf(ChatColor.COLOR_CHAR);
			s = s.replace(s.substring(i, i + 2), "");
			}
		
		for (int i = 0; i < kits.size(); i++) {
			OKit kit = kits.get(i);
			String n = kit.name;
			while (n.contains(ChatColor.COLOR_CHAR + "")) {
				int j = n.indexOf(ChatColor.COLOR_CHAR);
				n = n.replace(n.substring(j, j + 2), "");
				}
			
			if (n.equalsIgnoreCase(s)) {
				return kit;
			}
		}
		return null;
	}
	
	public boolean isCoolingdown(UUID uid) {
		return cooldowns.containsKey(uid);
	}
	
	public Permission getPermission() {
		return perm;
	}
	
	public void setPermission() {
		String s = name;
//		if (s.contains(ChatColor.COLOR_CHAR + "")) {
//			s = name.substring(name.lastIndexOf(ChatColor.COLOR_CHAR) + 2);
//		}
		if (s.contains(ChatColor.COLOR_CHAR + "")) {
			while (s.contains(ChatColor.COLOR_CHAR + "")) {
			int i = s.indexOf(ChatColor.COLOR_CHAR);
			s = s.replace(s.substring(i, i + 2), "");
			}
		}
		perm = new Permission("gkits." + s.toLowerCase());
	}
}
