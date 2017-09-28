package cluster.shop.util;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cluster.shop.Shop;

public class Sounds {

	public static Sound bought;
	public static Sound sold;
	public static Sound noMoney;
	public static Sound noItem;
	
	public static void load() {
		FileConfiguration c = Shop.getInstance().getConfig();
		try {
			bought = Sound.valueOf(c.getString("sounds.bought").toUpperCase());
		} catch(Exception e) {  }
		try {
			sold = Sound.valueOf(c.getString("sounds.sold").toUpperCase());
		} catch(Exception e) {  }
		try {
			noMoney = Sound.valueOf(c.getString("sounds.noMoney").toUpperCase());
		} catch(Exception e) {  }
		try {
			noItem = Sound.valueOf(c.getString("sounds.noItem").toUpperCase());
		} catch(Exception e) {  }
	}

	public static void play(Player p, Sound sound) {
		if(p != null && sound != null) {
			p.playSound(p.getLocation(), sound, 1, 1);
		}
	}
}
