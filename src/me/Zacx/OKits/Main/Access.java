package me.Zacx.OKits.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Access {

	public static Core c;
	
	public Access(Core c) {
		this.c = c;
	}

	 public static List<String> itemStackToString(ItemStack is) {
	       
	        List<String> r = new ArrayList<String>();
	       
	        StringBuilder sb = new StringBuilder();
	        sb.append("-" + is.getType().name() + "[");
	       
	        StringBuilder lore = new StringBuilder();
	       
	        if (is.hasItemMeta()) {
	            if (is.getItemMeta().hasDisplayName()) {
	                sb.append("name:" + is.getItemMeta().getDisplayName() + ",");
	            if (is.getItemMeta().hasLore()) {
	                for (int i = 0; i < is.getItemMeta().getLore().size(); i++) {
	                    String s = is.getItemMeta().getLore().get(i);
	                    lore.append(s + " ");
	                }
	                sb.append("lore:" + lore.toString() + ",");
	            }
	        }
	    }
	        sb.append("amount:" + is.getAmount() + "]");
	        r.add(sb.toString());
	       
	       
	        if (is.getItemMeta().hasEnchants()) {
	            r.add("Enchantments:");
	            sb.replace(0, sb.length(), "");
	            List<Enchantment> enchants = new ArrayList<Enchantment>(is.getItemMeta().getEnchants().keySet());
	            for (int i = 0; i < is.getItemMeta().getEnchants().size(); i++) {
	                Enchantment ench = enchants.get(i);
	                int lvl = is.getItemMeta().getEnchants().get(ench);
	                r.add("- " + ench.getName() + " " + lvl);
	            }
	        }
	        return r;
	    }
	
}