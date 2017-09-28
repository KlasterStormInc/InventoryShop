package cluster.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cluster.shop.core.ShopInventory;
import cluster.shop.util.Config;
import cluster.shop.util.Sounds;

public class ShopCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0)
		{
			if(sender.hasPermission("shop.command.usage")) {
				sender.sendMessage("§7--------[§3Shop§7]--------");
				sender.sendMessage("§aOpen shop §7- §b/" + label + " open <name> [player]");
				sender.sendMessage("§aList of shops §7- §b/" + label + " list");
				sender.sendMessage("§aReload §7- §b/" + label + " reload");
			} else {
				sender.sendMessage("§cYou don't have permission.");
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("open") && sender.hasPermission("shop.command.open")) {
			if(args.length < (sender instanceof Player ? 2 : 3)) {
				sender.sendMessage("§cUsage - /" + label + " open <name> [player]");
				return true;
			}
			
			Player p;
			if(args.length > 2) {
				p = Bukkit.getPlayer(args[2]);
			} else if(sender instanceof Player) {
				p = (Player) sender;
			} else return true;
			
			if(p == null) {
				sender.sendMessage("§cPlayer '" + args[2] + " not found");
				return true;
			}
			
			ShopInventory shop = Shop.getShopManager().getShop(args[1]);
			if(shop == null) {
				sender.sendMessage("§cShop '" + args[1] + "' not found");
				return true;
			}
			
			shop.open(p);
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("list") && sender.hasPermission("shop.command.list")) {
			List<ShopInventory> list = Shop.getShopManager().getShops();
			if(list.isEmpty()) {
				sender.sendMessage("§cThere are no shops.");
				return true;
			}
			List<String> sh = new ArrayList<String>();
			for(ShopInventory shop : list) {
				if(!sh.contains(shop.getName())) sh.add(shop.getName());
			}
			sender.sendMessage("§6There are " + sh.size() + " shops:");
			for(String s : sh) {
				sender.sendMessage("§a" + s);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("reload") && sender.hasPermission("shop.command.reload")) {
			Shop.getInstance().reloadConfig();
			Shop.getShopManager().close();
			Config.load();
			Sounds.load();
			Shop.getInstance().defineManager();
			sender.sendMessage("§aConfiguration and shops reloaded!");
			return true;
		}
		
		sender.sendMessage("§cUnknown command - '" + args[0] + "'");
		return true;
	}

	
	
	
	
	
	
	
	
	
	
}
