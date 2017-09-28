package cluster.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import cluster.shop.core.Item;
import cluster.shop.core.ShopInventory;

public class ShopAPI {

	@SuppressWarnings("deprecation")
	public static double getBuyPrice(ItemStack item) {
		Material m = item.getType();
		byte d = item.getData().getData();
		
		for (ShopInventory shop : Shop.getShopManager().getShops()) {
			for (Item i : shop.getItems()) {
				if(i.getItemStack().getType() == m && i.getItemStack().getData().getData() == d) {
					return i.getBuyPrice() * item.getAmount();
				}
			}
		}
		return 0;
	}
	@SuppressWarnings("deprecation")
	public static double getSellPrice(ItemStack item) {
		Material m = item.getType();
		byte d = item.getData().getData();
		
		for (ShopInventory shop : Shop.getShopManager().getShops()) {
			for (Item i : shop.getItems()) {
				if(i.getItemStack().getType() == m && i.getItemStack().getData().getData() == d) {
					return i.getSellPrice() * item.getAmount();
				}
			}
		}
		return 0;
	}
}
