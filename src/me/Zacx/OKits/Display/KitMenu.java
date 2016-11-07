package me.Zacx.OKits.Display;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zacx.OKits.Main.Core;
import me.Zacx.OKits.Main.OKit;


public class KitMenu implements Listener {

	public List<UUID> viewers = new ArrayList<UUID>();
	public static List<KitMenu> menus = new ArrayList<KitMenu>();
	
	private Inventory inv;
	private ItemStack gPane;
	public List<KitMenuPage> pages = new LinkedList<KitMenuPage>();
	private boolean loadingPage = false;
	private KitMenuPage page = null;
	public int pageIndex = 0;
	public Player p;
	private Core c;
	//private HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();;
	
	public KitMenu(Core c) {
		this.c = c;
		c.getServer().getPluginManager().registerEvents(this, c);
		menus.add(this);
		gPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
		ItemMeta meta = gPane.getItemMeta();
		meta.setDisplayName(" ");
		gPane.setItemMeta(meta);
		
//		for (int i = 0; i < PrisonKit.kits.size(); i++) {
//			PrisonKit kit = PrisonKit.kits.get(i);
//			//items.put(kit.getItem(), i);
//			inv.setItem(i, kit.getItem());
//		}
	}

	public void openKitMenu(Player p) {
		inv = Bukkit.createInventory(p, 36, "Kits");
		this.p = p;
		viewers.add(p.getUniqueId());
		tick();
		pages.get(0).open(p);
		pageIndex = 0;
	}
	
	public void tick() {
		
		if (!viewers.isEmpty()) {
		if (inv != null && p != null) {
			loadingPage = false;
			
		Player p = (Player) inv.getHolder();
		UUID uid = p.getUniqueId();

		if (pages.isEmpty())
		for (int i = 0; i < OKit.kits.size(); i++) {
			OKit kit = OKit.kits.get(i);
			
			
			if (!loadingPage) {
				page = new KitMenuPage(this, p);
				loadingPage = true;
			}
			if (page.kits.size() > 14) {
				loadingPage = false;
				page = new KitMenuPage(this, p);
				page.kits.add(kit);
				loadingPage = true;
			} else {
				page.kits.add(kit);
			}
		}
		}
		for (int n = 0; n < pages.size(); n++) {
			KitMenuPage page = pages.get(n);
			page.tick();
		}
		
		}
		
		if (viewers.isEmpty())
			KitMenu.menus.remove(this);
	}
	
	

	
	
}
