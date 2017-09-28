package cluster.shop.bridge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyBridge {
	
	private static Economy economy;

	public static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	public static Economy getEconomy() {
		return economy;
	}
	
	public static double getMoney(Player player) {
		if (economy == null) throw new IllegalStateException();
		return economy.getBalance(player);
	}
	
	public static boolean hasMoney(Player player, double minimum) {
		if (economy == null) throw new IllegalStateException();
		if (minimum < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + minimum);
		
		double balance = economy.getBalance(player);
		
		if (balance < minimum) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return true if the operation was successful.
	 */
	public static boolean takeMoney(Player player, double amount) {
		if (economy == null) throw new IllegalStateException();
		if (amount < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + amount);
		
		EconomyResponse response = economy.withdrawPlayer(player, amount);
		boolean result = response.transactionSuccess();
		
		return result;
	}
	
	public static boolean giveMoney(Player player, double amount) {
		if (economy == null) throw new IllegalStateException();
		if (amount < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + amount);
		
		EconomyResponse response = economy.depositPlayer(player, amount);
		boolean result = response.transactionSuccess();
		
		return result;
	}
}
