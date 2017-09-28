package cluster.shop.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class Deserializer {

	@SuppressWarnings("deprecation")
	public static MaterialData deserialize(String name) {
		if(name.contains(":")) {
			String[] arr = name.split(":");
			byte data;
			
			try {
				data = Byte.parseByte(arr[1]);
			} catch(Exception e) {
				throw new IllegalArgumentException("Data (" + arr[1] + ") must be a number", e);
			}
			
			try {
				int id = Integer.parseInt(arr[0]);
				return new MaterialData(id, data);
			} catch(Exception e) {
				
			}
			
			try {
				Material mat = Material.valueOf(arr[0].toUpperCase());
				return new MaterialData(mat, data);
			} catch(Exception e) {
				throw new IllegalArgumentException("Unknown material name - " + arr[0], e);
			}
		}
		try {
			int id = Integer.parseInt(name);
			return new MaterialData(id);
		} catch(Exception e) {
			
		}
		
		try {
			Material mat = Material.valueOf(name.toUpperCase());
			return new MaterialData(mat);
		} catch(Exception e) {
			throw new IllegalArgumentException("Unknown material name - " + name, e);
		}
	}
	
	
	public static ItemStack craft(String id, String name, List<String> lore)
	{
		 ItemStack item = deserialize(id).toItemStack(1);
		 ItemMeta meta = item.getItemMeta();
		 if(name != null) meta.setDisplayName(name.replace("&", "\u00a7"));
		 if(lore != null && !lore.isEmpty())
		 {
			 List<String> lorez = new ArrayList<String>();
			 for (String s : lore) {
				lorez.add(s.replace("&", "\u00a7"));
			}
			 meta.setLore(lorez);
		 }
		 item.setItemMeta(meta);
		 return item;
	}
	
	
	
	
	
	
	
	
	
	
	
}
