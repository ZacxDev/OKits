package me.Zacx.OKits.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zacx.OKits.Display.ItemMenu;
import me.Zacx.OKits.Display.KitMenu;
import me.Zacx.OKits.Files.FileParser;
import me.Zacx.OKits.Files.Updater;


public class Core extends JavaPlugin implements Listener{

	private FileParser fp;
	public String resID;
	private Random r;
	
	public Core() {
		new Access(this);
		fp = new FileParser(this);
		r = new Random();
	}
	
	public void onEnable() {
		Updater.getLastUpdate();
		fp.importKits();
		fp.readCooldowns();
		this.getServer().getPluginManager().registerEvents(this, this);
		
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (int i = 0; i < OKit.kits.size(); i++) {
					OKit kit = OKit.kits.get(i);
					kit.tick();
				}
				for (int i = 0; i < KitMenu.menus.size(); i++) {
					KitMenu menu = KitMenu.menus.get(i);
					menu.tick();
				}
			}
		}, 0L, 20L);
	}
	
	public void onDisable() {
		fp.saveCooldowns();
		saveDefaultConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("gkit")) {
			if (args.length == 0) {
			KitMenu m = new KitMenu(this);
			m.openKitMenu(p);
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("create") && p.isOp()) {
				List<String> itemList = new LinkedList<String>();
				for (int i = 0; i < p.getInventory().getContents().length; i++) {
					ItemStack item = p.getInventory().getContents()[i];
					List<String> itemString = Access.itemStackToString(item);
					for (int n = 0; n < itemString.size(); n++) {
						System.out.println(itemString.get(n));
						itemList.add(itemString.get(n));
					}
				}
				fp.writeKit(itemList, args[1], args[2]);
				p.sendMessage("§aNew GKit named §b" + args[1] + " §awith cooldown §e" + OKit.parseCooldown(Double.parseDouble(args[2])));
			}
		} else
			p.sendMessage("§c/gkit create [name] [cooldown]");
	}
		return true;
	}
	
	public static List<Object> getSubCollection(List list, int start, int end) {
		List<Object> r = new ArrayList<Object>();
		for (int i = start; i <= end; i++) {
			Object o = list.get(i);
			r.add(o);
		}
		return r;
	}
	
	
	public static String uid = "%%__USER__%%";
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		KitMenu menu = null;
		
		if (e.getInventory().getName().equalsIgnoreCase("Kits")) {
			if (item != null && item.getType() != Material.AIR ) {
				
				for (int i = 0; i < KitMenu.menus.size(); i++) {
					menu = KitMenu.menus.get(i);
					if(menu.p.getUniqueId().equals(p.getUniqueId()))
						break;
				}
				
				if (item.hasItemMeta() && OKit.getKit(item.getItemMeta().getDisplayName()) != null) {
			OKit kit = OKit.getKit(item.getItemMeta().getDisplayName());
			
			if (e.getClick() == ClickType.LEFT) {
			
			if (!kit.isCoolingdown(p.getUniqueId())) {
				if (p.hasPermission(kit.getPermission())) {
				kit.giveItems(p);
				p.closeInventory();
				} else
					p.sendMessage("§cYou do not have access to that kit!");
			} else
				p.sendMessage("§cThat Kit is Cooling Down!");
				} else if (e.getClick() == ClickType.RIGHT) {
					new ItemMenu(kit).open(p);
				} 
				} else {
					if (item.getData().getData() == DyeColor.BLACK.getData() && item.hasItemMeta()) {
						if (e.getSlot() == 30 && menu.pageIndex > 0) {
							menu.pageIndex--;
						} else if (e.getSlot() == 32 && menu.pageIndex < menu.pages.size() - 1) {
							menu.pageIndex++;
						}
						menu.pages.get(menu.pageIndex).open(p);
					}
				}
				
				
			e.setCancelled(true);
		}
		}
		
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		KitMenu menu = null;
		if (e.getInventory().getName().equalsIgnoreCase("Kits")) {
			
			for (int i = 0; i < KitMenu.menus.size(); i++) {
				menu = KitMenu.menus.get(i);
				if(menu.p.getUniqueId().equals(e.getPlayer().getUniqueId()))
					break;
			}
			
			menu.viewers.remove(e.getPlayer().getUniqueId());			
			
		}
	}
	
}
