package me.Zacx.OKits.Main;

import org.bukkit.event.Listener;

public class EventHandle implements Listener {

	private Core c;
	
	public EventHandle(Core c) {
		this.c = c;
		c.getServer().getPluginManager().registerEvents(this, c);
	}
	
	
//	@EventHandler
//	public void onJoin(PlayerJoinEvent e) {
//		Player p = e.getPlayer();
//		if (p.isOp() && c.up) {
//			p.sendMessage("§a[GKits] §bNew Update Available!");
//		}
//	}
	
}
