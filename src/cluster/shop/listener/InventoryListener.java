package cluster.shop.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import cluster.shop.Shop;
import cluster.shop.core.ShopInventory;

public class InventoryListener implements Listener {

	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		ShopInventory shop = Shop.getShopManager().getShop(e.getInventory());
		if(shop == null) return;
		e.setCancelled(true);
		if(!e.getInventory().equals(e.getClickedInventory())) return;
		Player p = (Player) e.getWhoClicked();
		
		int slot = e.getSlot();
		ClickType type = e.getClick();
		if(type == ClickType.RIGHT) {
			shop.performClick(p, slot, true);
		} else if(type == ClickType.LEFT) {
			shop.performClick(p, slot, false);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(!(e.getPlayer() instanceof Player)) return;
		
		ShopInventory shop = Shop.getShopManager().getShop(e.getInventory());
		if(shop != null) {
			shop.close((Player) e.getPlayer(), false);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
