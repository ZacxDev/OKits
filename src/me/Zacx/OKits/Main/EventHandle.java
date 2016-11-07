package me.Zacx.OKits.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventHandle implements Listener {

	private Core c;
	
	public EventHandle(Core c) {
		this.c = c;
		c.getServer().getPluginManager().registerEvents(this, c);
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOp() && c.up) {
			p.sendMessage("§a[GKits] §bNew Update Available!");
		}
	}
	
}
