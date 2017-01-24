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
        
		if (cmd.getName().equalsIgnoreCase("kit") && sender instanceof Player) {
            Player p = (Player) sender;
            kit.openGUI(p);
            return true;
        }
        
		if (cmd.getName().equalsIgnoreCase("stats") && sender instanceof Player) {
            Player p = (Player) sender;
			String pname;
			boolean otherplayer = false;
            
			try {
            	pname = args[0];
            	otherplayer = true;
            } catch (Exception e) {
            	pname = p.getName();
            }
			
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
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		e.setCancelled(true);
		
		if (!(e.getClickedInventory() instanceof PlayerInventory)) {
			kit.setKit(e.getCurrentItem(), e.getWhoClicked().getInventory());
			e.getWhoClicked().closeInventory();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.setKeepInventory(true);
		e.getEntity().getInventory().setContents(new ItemStack[39]);
		
		String died = e.getEntity().getName();
		String[] currentstats = Stats.getStats(died);
		String deaths = String.valueOf(Integer.parseInt(currentstats[1]) + 1);
		Stats.setStats(died,currentstats[0],deaths);
		
		Player killer = e.getEntity().getKiller();
		if (killer != null) {
			currentstats = Stats.getStats(killer.getName());
			String kills = String.valueOf(Integer.parseInt(currentstats[0]) + 1);
			Stats.setStats(killer.getName(), kills, currentstats[1]);
		}
	}
}