package me.cherrykit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	private Kits kit;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		kit = new Kits();
		Stats.getConnection();
	}
	
	@Override
	public void onDisable() {
		Stats.closeConnection();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
		//Registers /kit command
		if (cmd.getName().equalsIgnoreCase("kit") && sender instanceof Player) {
			Player p = (Player) sender;
			kit.openGUI(p);
			return true;
		}
        
		//Registers /stats command
		if (cmd.getName().equalsIgnoreCase("stats") && sender instanceof Player) {
			Player p = (Player) sender;
			String pname;
			boolean otherplayer = false;
            
			//Checks if command is used on self or other player, sets pname
			try {
				pname = args[0];
				otherplayer = true;
			} catch (Exception e) {
				pname = p.getName();
			}
			
			//Gets stats of player
			String[] stats = Stats.getStats(pname);
			if (otherplayer) {
				p.sendMessage(ChatColor.GREEN + "The player " + pname + " has " + stats[0] + " kills and " + stats[1] + " deaths.");
			} else {
				p.sendMessage(ChatColor.GREEN + "You have " + stats[0] + " kills and " + stats[1] + " deaths.");
			}
            return true;
        }
        
		return false;
    }
	
	//Prevents players from moving items in inventory and sets their kits
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		//Prevents players from moving items
		e.setCancelled(true);
		
		//If the inventory is the GUI: sets kit and closes inventory
		if (!(e.getClickedInventory() instanceof PlayerInventory)) {
			kit.setKit(e.getCurrentItem(), e.getWhoClicked().getInventory());
			e.getWhoClicked().closeInventory();
		}
	}
	
	//Ensures items don't drop and updates stats
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		//Clears inventory on death without dropping items
		e.setKeepInventory(true);
		e.getEntity().getInventory().setContents(new ItemStack[39]);
		
		//Increases deaths in stats
		String died = e.getEntity().getName();
		String[] currentstats = Stats.getStats(died);
		String deaths = String.valueOf(Integer.parseInt(currentstats[1]) + 1);
		Stats.setStats(died,currentstats[0],deaths);
		
		//Increases kills in stats
		Player killer = e.getEntity().getKiller();
		if (killer != null) {
			currentstats = Stats.getStats(killer.getName());
			String kills = String.valueOf(Integer.parseInt(currentstats[0]) + 1);
			Stats.setStats(killer.getName(), kills, currentstats[1]);
		}
	}
}
