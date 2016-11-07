package me.Zacx.OKits.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zacx.OKits.Display.KitMenu;
import me.Zacx.OKits.Files.FileParser;
import me.Zacx.OKits.Files.Updater;
import me.Zacx.OKits.jpaste.exceptions.PasteException;
import me.Zacx.OKits.jpaste.pastebin.Pastebin;

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
		fp.reg("");
		auth();
		Updater.getLastUpdate();
		fp.importKits();
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
		fp.reg();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("gkit")) {
			if (args.length == 0) {
			KitMenu m = new KitMenu(this);
			m.openKitMenu(p);
		} else 
			if (args[0].equalsIgnoreCase("create")) {
				for (int i = 0; i < p.getInventory().getContents().length; i++) {
					ItemStack item = p.getInventory().getContents()[i];
					
				}
			}
		
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
	//public static String uid = "Zacx";
	public boolean sts = true;
	public boolean registered = false;
	private URL check;
	public String key;
	     public void auth()
	     {
	       try
	       {
	    	   String devkey = "a3abc9e537764a5d0ad4f81e4e3fc952";
//	    	   String un = "Zacx";
//	    	   String pass = "Zachtheepic1";
//	    	   
//	    	   PastebinAccount acc = new PastebinAccount(devkey, un, pass);
//	    	   acc.login();
	    	   
	    	   if (!registered) {
	    		   System.out.println("Registering Licence...");
	    		   resID = "OKITS-" + r.nextInt(1000000) + "-" + r.nextInt(1000000) + "-" + uid + "-" + r.nextInt(1000);
	    		   check = Pastebin.pastePaste(devkey, resID, uid);
	    		   key = check.toExternalForm().substring(check.toExternalForm().lastIndexOf("/") + 1);
	    		   registered = true;
	    		   fp.reg();
	    	   } else {
	    		   System.out.println("Checking Licence...");
	    		   //check pastebin
	    		   String content = Pastebin.getContents(key);
	    		   if (!content.contains(resID)) {
	    			   System.out.println("[Error] Fatal Error 0x17");
	    			   this.getServer().getPluginManager().disablePlugin(this);
	    		   }
	    	   }
	    		   //Pastebin.getContents(check.toExternalForm());
	    		   //System.out.println(key);
	    	   
	    	   
	    	   
	       }
	       catch (PasteException e) {
			e.printStackTrace();
		}
	     }
	    
	     public void disableLeak()
	     {
	           System.out.println("Verification Failed.. Disabling.");
	         
	       getServer().getPluginManager().disablePlugin(this);
	       sts = false;
	     }
	    
	     public void disableNoInternet() {
	    	 System.out.println(ChatColor.RED + "You don't have a valid internet connection, please connect to the internet for the plugin to work!");
	         getServer().getPluginManager().disablePlugin(this);
	         sts = false;
	     }
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		KitMenu menu = null;
		
		if (e.getInventory().getName().equalsIgnoreCase("Kits")) {
			if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
				
				for (int i = 0; i < KitMenu.menus.size(); i++) {
					menu = KitMenu.menus.get(i);
					if(menu.p.getUniqueId().equals(p.getUniqueId()))
						break;
				}
				
				if (item.getType() != Material.STAINED_GLASS_PANE) {
			OKit kit = OKit.getKit(e.getCurrentItem().getItemMeta().getDisplayName());
			if (kit != null)
			if (!kit.isCoolingdown(p.getUniqueId())) {
				if (p.hasPermission(kit.getPermission())) {
				kit.giveItems(p);
				p.closeInventory();
				} else
					p.sendMessage("§cYou do not have access to that kit!");
			} else
				p.sendMessage("§cThat Kit is Cooling Down!");
				} else {
					if (item.getData().getData() == DyeColor.BLACK.getData()) {
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
