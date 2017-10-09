package cluster.shop.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cluster.shop.Shop;

public class ItemManager {

	private File file;
	private FileConfiguration stream;
	private List<ShopItem> items = new ArrayList<>();
	
	public ItemManager(Shop plugin) {
		this.file = new File(plugin.getDataFolder(), "items.yml");
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
		
		for (String name : keys) {
			try {
				Map<String, Integer> enchantments = new HashMap<>();
				ConfigurationSection sec = stream.getConfigurationSection(name + ".enchantments");
				if(sec != null) {
					Set<String> ekeys = sec.getKeys(false);
					if(ekeys != null) for (String ek : ekeys)
						enchantments.put(ek, stream.getInt(name + ".enchantments." + ek));
				}
				
				ShopItem item = ShopItem.deserialize(name,
						stream.getString(name + ".id"), stream.getString(name + ".name"),
						stream.getStringList(name + ".lore"), enchantments);
				items.add(item);
			} catch (Exception e) {
				Shop.err("Malformed item '" + name + "' in items.yml", e);
			}
		}
	}
	
	public ShopItem getItem(String key) {
		for (ShopItem i : items) {
			if(i.getKey().equalsIgnoreCase(key)) return i;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
