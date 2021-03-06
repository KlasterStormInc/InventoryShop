package cluster.shop;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import cluster.shop.bridge.EconomyBridge;
import cluster.shop.core.ShopManager;
import cluster.shop.items.ItemManager;
import cluster.shop.listener.InventoryListener;
import cluster.shop.util.Config;
import cluster.shop.util.Sounds;

public class Shop extends JavaPlugin {

	private static Shop instance;
	private ShopManager manager;
	private ItemManager items;
	
	@Override
	public void onEnable() {
		instance = this;
		
		saveDefaultConfig();
		saveResource("shops.yml", false);
		saveResource("items.yml", false);
		
		Config.load();
		Sounds.load();
		defineItems();
		defineManager();
		EconomyBridge.setupEconomy();
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
		getCommand("shop").setExecutor(new ShopCommand());
	}
	
	@Override
	public void onDisable() {
		manager.close();
	}
	
	public static Shop getInstance() {
		return instance;
	}
	public static ShopManager getShopManager() {
		return instance.manager;
	}
	public static ItemManager getItemManager() {
		return instance.items;
	}
	
	public static void err(String msg, Throwable e) {
		instance.getLogger().log(Level.SEVERE, msg, e);
	}


	public static void err(Throwable e) {
		instance.getLogger().log(Level.SEVERE, "An error occurred", e);
	}
	
	public static void err(String s) {
		instance.getLogger().log(Level.SEVERE, s);
	}


	public void defineItems() {
		items = new ItemManager(this);
	}
	
	public void defineManager() {
		manager = new ShopManager(this);
	}
	
	
	
	
	
	
}
