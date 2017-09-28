package cluster.shop.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import cluster.shop.Shop;
import cluster.shop.util.AmountRegex;

public class ShopManager {

	private File file;
	private FileConfiguration stream;
	private List<ShopInventory> shops = new ArrayList<ShopInventory>();
	AmountRegex regex;
	
	public ShopManager(Shop plugin)
	{
		this.file = new File(plugin.getDataFolder(), "shops.yml");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		stream = YamlConfiguration.loadConfiguration(file);
		Set<String> keys = stream.getKeys(false);
		if(keys == null || keys.isEmpty()) return;
		
		String pattern = Shop.getInstance().getConfig().getString("amountRegex");
		try {
			regex = AmountRegex.compile(pattern);
		} catch(Exception e) {
			Shop.getInstance().getLogger().log(Level.SEVERE, "Illegal amount expression", e);
			return;
		}
		Shop.getInstance().getLogger().info("Registered amount regex: " + pattern);
		
		for (String name : keys) {
			String displayName = stream.getString(name + ".name", name).replace("&", "\u00a7");
			int rows = stream.getInt(name + ".rows", 6);
			List<String> items = stream.getStringList(name + ".items");
			createNewShop(name, displayName, rows, items);
			Shop.getInstance().getLogger().info("Registered shop: " + name);
		}
	}
	
	
	
	public ShopInventory getShop(String name) {
		for (ShopInventory s : shops) {
			if(s.getName().equals(name)) return s;
		}
		return null;
	}
	
	public ShopInventory getShop(Inventory inventory) {
		for (ShopInventory s : shops) {
			if(s.getInventory().equals(inventory)) return s;
		}
		return null;
	}
	
	private void createNewShop(String name, String displayName, int rows, List<String> items) {
		if(items == null) items = new ArrayList<String>();
		for (int i : regex.getSlots()) {
			try {
				ShopInventory shop = new ShopInventory(name, displayName, rows, items, i);
				shops.add(shop);
			} catch (Exception e) {
				Shop.err("Failed to create shop '" + name + "'", e);
			}
		}
	}
	
	public List<ShopInventory> getShops() {
		return shops;
	}
	
	
	
	public void close() {
		for (ShopInventory s : shops) {
			s.closeAll();
		}
		shops.clear();
	}
	
	
	public boolean open(Player p, String shop) {
		return open(p, shop, 1);
	}
	
	public boolean open(Player p, String shop, int multiplier) {
		
		ShopInventory s = null;
		for (ShopInventory i : shops) {
			if(i.getName().equals(shop) && i.getMultiplier() == multiplier) {
				s = i;
			}
		}
		if(s == null) return false;
		
		s.open(p);
		return true;
	}
	
	
	
	
	
}
