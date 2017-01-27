package me.cherrykit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Kits {

	//Attributes
	private Inventory GUI;
	private ItemStack[] swordsman;
	private int[] swordsmanSlots;
	private ItemStack[] archer;
	private int[] archerSlots;
	
	//Initializes the GUI and the kits
	public Kits() {
		//Creates GUI
		GUI = Bukkit.createInventory(null, 9, "Kits");
		
		//First item in GUI
		ItemStack kit1 = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta meta = kit1.getItemMeta();
		meta.setDisplayName("Swordsman");
		kit1.setItemMeta(meta);
		
		//Second item in GUI
		ItemStack kit2 = new ItemStack(Material.BOW, 1);
		meta = kit2.getItemMeta();
		meta.setDisplayName("Archer");
		kit2.setItemMeta(meta);
		
		//Add items to GUI
		GUI.setItem(0, kit1);
		GUI.setItem(1, kit2);
		
		//Creates swordsman kit
		swordsman = new ItemStack[] {new ItemStack(Material.IRON_SWORD,1), new ItemStack(Material.GOLDEN_APPLE,2),
				new ItemStack(Material.CHAINMAIL_BOOTS,1), new ItemStack(Material.CHAINMAIL_LEGGINGS,1), 
				new ItemStack(Material.CHAINMAIL_CHESTPLATE,1), new ItemStack(Material.CHAINMAIL_HELMET,1)};
		swordsmanSlots = new int[] {0,1,36,37,38,39};
		
		//Creates archer kit
		archer = new ItemStack[] {new ItemStack(Material.BOW,1), new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.GOLDEN_APPLE,2), new ItemStack(Material.IRON_BOOTS, 1), 
				new ItemStack(Material.CHAINMAIL_LEGGINGS,1), new ItemStack(Material.CHAINMAIL_CHESTPLATE,1),
				new ItemStack(Material.DIAMOND_HELMET,1)};
		archerSlots = new int[] {0,1,2,36,37,38,39};
		}
	
	//Opens the GUI
	public void openGUI(Player p) {
		p.openInventory(GUI);
	}
	
	//Sets the players inventory to the kit depending on which item they clicked
	public void setKit(ItemStack clicked, PlayerInventory inv) {
		//Clears current inventory
		inv.setContents(new ItemStack[39]);
		
		//Sets new inventory depending on item clicked
		if (clicked.getType() == Material.IRON_SWORD) {
			for (int i = 0; i < swordsman.length; i++) {
				inv.setItem(swordsmanSlots[i], swordsman[i]);
			}
		}
		
		if (clicked.getType() == Material.BOW) {
			for (int i = 0; i < archer.length; i++) {
				inv.setItem(archerSlots[i], archer[i]);
			}
		}
	}
	
}
