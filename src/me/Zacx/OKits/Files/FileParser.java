package me.Zacx.OKits.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
			if (!ranksFile.exists()) {
				ranksFile.createNewFile();
			}

			BufferedReader br = new BufferedReader(
					new FileReader(c.getDataFolder() + "/kits.txt"));
			String line = br.readLine();

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
								String[] lines = v.split("%n");
								for (int n = 0; n < lines.length; n++) {
									System.out.println(lines[n]);
									list.add(lines[n].replaceAll("%n", ""));
								}
								meta.setLore(list);
							} else if (s.contains("amount:")) {
								v = s.substring(s.indexOf(":") + 1,
										s.lastIndexOf("]"));
								item.setAmount(Integer.parseInt(v));
							} else if (s.contains("data:")) {
								v = s.substring(s.indexOf(":") + 1);
								item = new ItemStack(item.getType(), item.getAmount(), Byte.parseByte(v));
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

			if (line == null && OKit.kits.isEmpty() == false) {
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
	
	public void writeKit(List<String> info, String name, String cooldown) {
		
		File folder = c.getDataFolder();
		File kitsFile = new File(c.getDataFolder() + "/kits.txt");

		try {

			if (!folder.exists())
				folder.mkdir();
			if (!kitsFile.exists()) {
				kitsFile.createNewFile();
			}

			
			BufferedReader br = new BufferedReader(
					new FileReader(c.getDataFolder() + "/kits.txt"));
			
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(c.getDataFolder() + "/kits.temp"));
			
			String line = br.readLine();
			
			if (line == null) {
				
				bw.write("Name: " + name);
				bw.newLine();
				bw.write("Lore: insert bad joke");
				bw.newLine();
				bw.write("Item: STONE");
				bw.newLine();
				bw.write("Cooldown: " + cooldown);
				bw.newLine();
				bw.write("Items:");
				bw.newLine();
				
				for (int n = 0; n < info.size(); n++) {
					String item = info.get(n);
					bw.write(item);
					bw.newLine();
				}
				
				bw.write("END");
				bw.newLine();
				
			}
			
			while (line != null) {
				
				bw.write(line);
				bw.newLine();
				line = br.readLine();
				
				if (line == null) {
					
					bw.write("Name: " + name);
					bw.newLine();
					bw.write("Lore: insert bad joke");
					bw.newLine();
					bw.write("Item: STONE");
					bw.newLine();
					bw.write("Cooldown: " + cooldown);
					bw.newLine();
					bw.write("Items:");
					bw.newLine();
					
					for (int n = 0; n < info.size(); n++) {
						String item = info.get(n);
						bw.write(item);
						bw.newLine();
					}
					
					bw.write("END");
					bw.newLine();
					break;
				}
				
			}
			
			
			
			bw.flush();
			bw.close();
			br.close();
			
			File og = new File(c.getDataFolder() + "/kits.txt");
			Files.delete(og.toPath());
			new File(c.getDataFolder() + "/kits.temp").renameTo(kitsFile);
			
		} catch(Exception e) {}
		
	}
	
	public void saveCooldowns() {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(c.getDataFolder() + "/cd.txt")));
	
			for (int i = 0; i < OKit.kits.size(); i++) {
			OKit kit = OKit.kits.get(i);
			List<UUID> uids = new LinkedList<UUID>(kit.cooldowns.keySet());
			for (int n = 0; n < uids.size(); n++) {
				bw.write(kit.name + ":" + uids.get(n).toString() + ":" + kit.cooldowns.get(uids.get(n)).toString());
				bw.newLine();
			}
		}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readCooldowns() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(c.getDataFolder() + "/cd.txt")));
			String line = br.readLine();
			
			while(line != null) {
				
				String kn = line.substring(0, line.indexOf(":"));
				OKit kit = OKit.getKit(kn);
				
				String uid = line.substring(line.indexOf(":") + 1, line.lastIndexOf(":"));
				UUID uuid = UUID.fromString(uid);
				
				String cd = line.substring(line.lastIndexOf(":") + 1);
				Long cdl = Long.parseLong(cd);
				
				kit.cooldowns.put(uuid, cdl);
				
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}