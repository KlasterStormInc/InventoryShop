package cluster.shop.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import cluster.shop.Shop;
import cluster.shop.bridge.EconomyBridge;
import cluster.shop.util.Config;
import cluster.shop.util.Deserializer;
import cluster.shop.util.RequiredItem;
import cluster.shop.util.Sounds;

public class ShopInventory {
	
	private String name;
	private int multiplier;
	private Inventory inventory;
	private Map<Integer, Item> items = new HashMap<Integer, Item>();
	private List<Player> holder = new ArrayList<Player>();
	
	ShopInventory(String name, String displayName, int rows, List<String> items, int multiplier) {
		this.name = name;
		inventory = Bukkit.createInventory(null, rows * 9, displayName);
		this.multiplier = multiplier;
		int n = 0;
		
		for (String item : items) {
			String[] arr = item.split(" ");
			MaterialData material = Deserializer.deserialize(arr[0]);
			double buy = Double.parseDouble(arr[1]) * multiplier;
			double sell = Double.parseDouble(arr[2]) * multiplier;
			ItemStack stack = craftStack(material, multiplier, buy, sell);
			inventory.setItem(n, stack);
			this.items.put(n, new Item(stack, buy, sell));
			n++;
		}
		
		if(Config.enabledChangeAmountItem)
		{
			int slot = Config.changeAmountItemSlot;
			if(slot < 0) return;
			ItemStack item = Config.getChangeAmountItem();
			this.inventory.setItem(slot, item);
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	public String getName() {
		return name;
	}
	
	public Collection<Item> getItems() {
		return items.values();
	}
	
	public void open(Player p) {
		if(holder.contains(p)) return;
		holder.add(p);
		p.openInventory(inventory);
	}
	
	public void close(Player p, boolean skip) {
		if(!holder.contains(p)) return;
		holder.remove(p);
		if(skip) p.closeInventory();
	}
	
	public void closeAll() {
		Player[] p = holder.toArray(new Player[holder.size()]);
		for (int i = 0; i < p.length; i++) {
			close(p[i], true);
		}
	}
	
	/**
	 * right: true - sell, false - buy
	 */
	public void performClick(Player p, int slot, boolean right) {
		if(Config.changeAmountItemSlot == slot && Config.changeAmountItemSlot > 0)
		{
			if(right) previousShop(p);
			else nextShop(p);
			return;
		}
		
		
		Item item = items.get(slot);
		if(item == null) return;
		if(right) {
			ItemStack it = item.getItemStack();
			RequiredItem r = new RequiredItem(it.getType(), multiplier);
			r.setRestrictiveDataValue(it.getDurability());
			
			if(r.hasItem(p)) {
				r.takeItem(p);
				EconomyBridge.giveMoney(p, item.getSellPrice());
				String msg = Config.sold;
				msg = msg.replace("{amount}", String.valueOf(multiplier)).
						replace("{item}", item.getSimpleItem().getType().toString());
				p.sendMessage(msg);
				Sounds.play(p, Sounds.sold);
			} else {
				String msg = Config.noItem;
				msg = msg.replace("{amount}", String.valueOf(multiplier)).
						replace("{item}", item.getSimpleItem().getType().toString());
				p.sendMessage(msg);
				Sounds.play(p, Sounds.noItem);
			}
		} else {
			if(EconomyBridge.hasMoney(p, item.getBuyPrice())) {
				EconomyBridge.takeMoney(p, item.getBuyPrice());
				p.getInventory().addItem(new ItemStack
						(item.getItemStack().getType(), multiplier, item.getItemStack().getDurability()));
				String msg = Config.bought;
				msg = msg.replace("{amount}", String.valueOf(multiplier)).
						replace("{item}", item.getSimpleItem().getType().toString());
				p.sendMessage(msg);
				Sounds.play(p, Sounds.bought);
			} else {
				String msg = Config.noMoney;
				msg = msg.replace("{money}", String.valueOf(item.getBuyPrice()));
				p.sendMessage(msg);
				Sounds.play(p, Sounds.noMoney);
			}
		}
	}
	
	
	
	
	private void nextShop(Player p) {
		close(p, false);
		ShopManager manager = Shop.getShopManager();
		manager.open(p, name, manager.regex.next(multiplier));
	}
	private void previousShop(Player p) {
		close(p, false);
		ShopManager manager = Shop.getShopManager();
		manager.open(p, name, manager.regex.previous(multiplier));
	}

	private ItemStack craftStack(MaterialData m, int multiplier, double b, double s) {
		ItemStack item = m.toItemStack();
		item.setAmount(multiplier);
		ItemMeta meta = item.getItemMeta();
		List<String> lm = new ArrayList<String>();
		
		List<String> lore = Shop.getInstance().getConfig().getStringList("iconsLore");
		for (String e : lore) {
			lm.add(
				e.replace("{buy_price}", String.valueOf(b))
				.replace("{sell_price}", String.valueOf(s))
				.replace("&", "\u00a7"));
		}
		meta.setLore(lm);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShopInventory) {
			return name.equals(((ShopInventory) obj).getName())
					&& multiplier == ((ShopInventory)obj).multiplier;
		}
		return false;
	}

	public int getMultiplier() {
		return multiplier;
	}
	
	
}
