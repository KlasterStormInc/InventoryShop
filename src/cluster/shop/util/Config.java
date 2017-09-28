package cluster.shop.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import cluster.shop.Shop;

public class Config {

	public static String noItem;
	public static String noMoney;
	public static String bought;
	public static String sold;
	
	public static boolean enabledChangeAmountItem;
	public static int changeAmountItemSlot;
	private static ItemStack changeAmountItem;
	public static boolean ignoreColoredDisplay;
	
	public static void load() {
		FileConfiguration c = Shop.getInstance().getConfig();
		
		noItem = c.getString("messages.noItem", "&cYou must have {amount} of {item}").replace("&", "\u00a7");
		noMoney = c.getString("messages.noMoney", "&cYou must have {money} to buy this").replace("&", "\u00a7");
		bought = c.getString("messages.bought", "&cYou have bought {item} ({amount})").replace("&", "\u00a7");
		sold = c.getString("messages.sold", "&cYou have sold {item} ({amount})").replace("&", "\u00a7");
		
		
		try {
			changeAmountItem = Deserializer.craft(c.getString("changeAmountIcon.id"),
					c.getString("changeAmountIcon.name"), c.getStringList("changeAmountIcon.lore"));
		} catch (Exception e) {
			changeAmountItem = null;
		}
		enabledChangeAmountItem = changeAmountItem == null ? false : c.getBoolean("changeAmountIcon.enable");
		changeAmountItemSlot = c.getInt("changeAmountIcon.slot", -1);
		
		ignoreColoredDisplay = c.getBoolean("ignoreColoredDisplay", true);
	}
	
	
	
	public static ItemStack getChangeAmountItem() {
		return changeAmountItem.clone();
	}
	
	
	
	
}
