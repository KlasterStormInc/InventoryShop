package cluster.shop.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import cluster.shop.util.Deserializer;

public class ShopItem {
	
	private String key;
	private ItemStack item;
	
	@SuppressWarnings("deprecation")
	ShopItem(String key, MaterialData type, String name, List<String> lore, Map<String, Integer> ench) {
		this.key = key;
		item = new ItemStack(type.getItemType(), 1, type.getData());
		ItemMeta m = item.getItemMeta();
		if(name != null) {
			m.setDisplayName(name.replace("&", "§"));
		}
		if(lore != null) {
			List<String> lorez = new ArrayList<>();
			for (String l : lore) lorez.add(l.replace("&", "§"));
			m.setLore(lorez);
		}
		if(ench != null) for (String e : ench.keySet()) {
			try {
				Enchantment enchant = Enchantment.getByName(e);
				if(enchant == null) continue;
				m.addEnchant(enchant, ench.get(e).intValue(), true);
			} catch(Exception err) {  }
 		}
		item.setItemMeta(m);
	}
	
	public static ShopItem deserialize(String key, String id, String name, List<String> lore, Map<String, Integer> ench) {
		MaterialData type = Deserializer.deserialize(id);
		return new ShopItem(key, type, name, lore, ench);
	}
	
	
	
	public String getKey() {
		return key;
	}
	
	
	public ItemStack getItem() {
		return item == null ? null : item.clone();
	}
}
