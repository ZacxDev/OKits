package me.Zacx.OKits.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zacx.OKits.Main.Core;
import me.Zacx.OKits.Main.OKit;

public class FileParser {

	private Core c;
	
	public FileParser(Core c) {
		this.c = c;
	}
	
	public void importKits() {

		File folder = c.getDataFolder();
		File ranksFile = new File(c.getDataFolder() + "/kits.txt");

		int i = 0;
		try {

			if (!folder.exists())
				folder.mkdir();
			if (!ranksFile.exists())
				ranksFile.createNewFile();

			BufferedReader br = new BufferedReader(
					new FileReader(c.getDataFolder() + "/kits.txt"));
			String line = br.readLine();
			line = br.readLine();

			boolean readingKit = false;
			boolean readingMeta = false;
			boolean readingItem = false;
			boolean readingEnchantments = false;

			OKit kit = null;
			ItemStack item = null;
			ItemMeta meta = null;

			while (line != null) {
				line = line.trim();
				if (line.isEmpty() || line.equalsIgnoreCase(" ") || line.equalsIgnoreCase("\r\n") || line.equalsIgnoreCase("")) {
					line = br.readLine();
					continue;
				}
				line = line.replaceAll("&", "§");
				// System.out.println(line);
				// add last item to kit
				if ((line.startsWith("-") && readingEnchantments)
						|| line.equals("END")) {
					item.setItemMeta(meta);
					//System.out.println(meta.getDisplayName() + ", "
							//+ item.getType().name());
					// System.out.println(kit.name);
					kit.items.add(item);
					readingEnchantments = false;
				}

				// start new kit
				if (line.equals("END")) {
					kit.setPermission();
					readingEnchantments = false;
					readingItem = false;
					readingKit = false;
					readingMeta = false;
				}
				// start new kit
				if (!readingKit) {
					kit = new OKit();
					readingKit = true;
					i++;
				}
				// basic kit info
				if (readingKit && !readingItem)
					kit.setProperty(line);

				if (line.startsWith("Items")) {
					readingItem = true;
				}

				if (readingItem) {
					// type, name, lore
					if (line.startsWith("-")) {
						readingMeta = true;
						String typeName = line.substring(1,
								line.lastIndexOf("["));
						// System.out.println(typeName);
						item = new ItemStack(Material.getMaterial(typeName));
						meta = item.getItemMeta();
					}

					if (readingMeta) {
						String[] metas = line.split(",");
						// System.out.println("reading meta");
						for (int j = 0; j < metas.length; j++) {
							String s = metas[j];
							String v = "";
							if (s.contains("name:")) {
								v = s.substring(s.indexOf(":") + 1);
								meta.setDisplayName(v);
							} else if (s.contains("lore:")) {
								v = s.substring(s.indexOf(":") + 1);
								ArrayList<String> list = new ArrayList<String>();
								list.add(v);
								meta.setLore(list);
							} else if (s.contains("amount:")) {
								v = s.substring(s.indexOf(":") + 1,
										s.lastIndexOf("]"));
								item.setAmount(Integer.parseInt(v));
							}
							// System.out.println(v);
						}
						readingMeta = false;
					}

					if (readingEnchantments) {
						String e = line.substring(0, line.indexOf(" "));
						String lvl = line.substring(line.indexOf(" ") + 1);
						meta.addEnchant(Enchantment.getByName(e),
								Integer.parseInt(lvl), true);
						// System.out.println(e + " " + lvl);
					}
					

					if (line.startsWith("Enchantments:")) {
						readingEnchantments = true;
						readingMeta = false;
					}

				}

				line = br.readLine();
			}

			if (line == null) {
				OKit.kits.removeLast();
				i--;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Imported " + i + " Kits.");
		}

	}
	
	public void reg() {


		File folder = c.getDataFolder();
		File ranksFile = new File(c.getDataFolder() + "/uid.txt");

		try {

			if (!folder.exists())
				folder.mkdir();
			if (!ranksFile.exists())
				ranksFile.createNewFile();
			else {
				ranksFile.delete();
				// ranksFile.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(ranksFile));

			out.write("true");
			out.newLine();
			out.write(c.resID);
			out.newLine();
			out.write(c.key);
			
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void reg(String s) {


		File folder = c.getDataFolder();
		File ranksFile = new File(c.getDataFolder() + "/uid.txt");

		try {

			if (!folder.exists())
				folder.mkdir();
			if (!ranksFile.exists())
				return;
			
			BufferedReader br = new BufferedReader(
					new FileReader(c.getDataFolder() + "/uid.txt"));
			String line = br.readLine().trim();
			
			while (line != null) {
				
				//System.out.println("read: " + line);
				if (Boolean.parseBoolean(line)) {
					//System.out.println("parsing");
					c.registered = true;
					line = br.readLine();
					c.resID = line;
					line = br.readLine();
					c.key = line;
				} else
					break;
				
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
