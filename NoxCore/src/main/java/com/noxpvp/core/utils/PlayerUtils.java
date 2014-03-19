package com.noxpvp.core.utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class PlayerUtils extends PlayerUtil{
	
	public static boolean hasAtleast(Player p, ItemStack item){
		return hasAtleast(p.getInventory(), item, item.getAmount());
	}
	
	public static boolean hasAtleast(Player p, ItemStack item, int amount){
		return hasAtleast(p.getInventory(), item, amount);
	}
	
	public static boolean hasAtleast(Inventory inv, ItemStack item, int amount){
		Material type = item.getType();
		int totals = 0;
		
		for(ItemStack s : inv.getContents()){
			if (s == null || s.getType() != type) continue;
			
			totals += s.getAmount();
			
		}
		
		return totals >= amount;
		
	}
	
	/**
	 * @author burgerkiller - https://forums.bukkit.org/threads/determine-whether-the-player-can-see-a-mob.56129/
	 * 
	 */
	public static class LineOfSightUtil {
		public static Block getTarget(Location from, int distance, Material... transparentTypes) {
			if (transparentTypes == null || transparentTypes.length == 0) {
				return getTarget(from, distance, (Set<Material>) null);
			} else {
				Set<Material> types = new HashSet<Material>(transparentTypes.length);
				for (Material b : transparentTypes) types.add(b);
				return getTarget(from, distance, types);
			}
		}
		
		public static Block getTarget(Location from, int distance, Set<Material> transparentTypes) {
			BlockIterator itr = new BlockIterator(from, 0, distance);
			while (itr.hasNext()) {
				Block block = itr.next();
				Material type = block.getType();//TODO use materials
				if (transparentTypes == null) {
					if (type == Material.AIR) continue;
				} else if (transparentTypes.contains(type)) {
					continue;
				}
				return block;
			}
			return null;
		}
		
		public static Block getTarget(LivingEntity from, int distance, Set<Material> transparentTypes) {
			Location from2 = from.getEyeLocation();
			from2.setPitch(0);
			from2.setYaw(0);
			
			return getTarget(from2, distance, transparentTypes);
		}
		
		public static Block getTarget(LivingEntity from, int distance, Material... transparentTypes) {
			return getTarget(from.getEyeLocation(), distance, transparentTypes);
		}
		
	    public static boolean hasLineOfSight(LivingEntity from, Location to, Set<Material> transparentTypes) {
	        return getTarget(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypes) == null;
	    }
	    
	    public static boolean isLookingAt(LivingEntity from, Location to, Material... transparentTypes) {
			return getTarget(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypes) == null;
		}
		
	}

}
