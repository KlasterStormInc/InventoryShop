package cluster.shop.core;

import org.bukkit.inventory.ItemStack;

public class Item {

	private ItemStack itemStack;
	private double buyPrice;
	private double sellPrice;

	public Item(ItemStack itemStack, double buyPrice, double sellPrice) {
		this.itemStack = itemStack;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public ItemStack getSimpleItem() {
		return new ItemStack(itemStack.getType(), itemStack.getAmount());
	}
}
