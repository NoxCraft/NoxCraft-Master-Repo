package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.bases.ExtendedEntity;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
 
/**
 * @author NoxPVP
 *
 */
public class AlphaMaleAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Alpha Male";
	public final static String PERM_NODE = "alphamale";
	
	public static List<Wolf> wolves = new ArrayList<Wolf>();
	
	private Wolf a;
 
	/**
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public AlphaMaleAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute(){
		if (!mayExecute())
			return false;
		
		final Player p = getPlayer();
		this.a = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		
		a.setLeashHolder(p);
		a.setOwner(p);
		a.setCollarColor(DyeColor.BLACK);
		a.setBreed(false);
		a.setAngry(true);
		a.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + "'s AlphaMale");
		a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		ExtendedEntity<Wolf> wc = new ExtendedEntity<Wolf>(a);
		
		WolfController controller = new WolfController(wc);
		controller.runTaskTimer(NoxMMO.getInstance(), 0, 2);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {return getPlayer() != null;}
	
	private class WolfController extends BukkitRunnable{
		private Player p;
		private ExtendedEntity<Wolf> wc;
		
		public WolfController(ExtendedEntity<Wolf> wc){
			this.p = getPlayer();
			this.wc = wc;
		}
		
		public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
		
		public void run(){
			if (a == null || a.isDead() || !a.isLeashed()){
				if (AlphaMaleAbility.wolves.contains(a)){
					AlphaMaleAbility.wolves.remove(a);
				}
				safeCancel();
				return;}
			if ((a.getTarget() instanceof LivingEntity && !a.getTarget().isDead()) || a.isSitting()) return;
			
			Location pl = p.getLocation();
			Location al = wc.getLocation();
			
			if (pl.getPitch() > 40 || pl.getPitch() < -60) return;
			
			if (al.getYaw() > pl.getYaw() + 6 || al.getYaw() < pl.getYaw() - 6){
				wc.setRotation(pl.getYaw(), al.getYaw());
			}
			
			if (p.getLocation().distance(wc.getLocation()) > 4.0) return;
			
			wc.setVelocity(pl.getDirection().setY(0).multiply(0.75));
		}
	}
 
}