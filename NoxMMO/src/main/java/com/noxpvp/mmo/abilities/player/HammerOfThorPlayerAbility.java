package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class HammerOfThorPlayerAbility extends BasePlayerAbility  implements IPVPAbility {
	public static FixedMetadataValue hammerSecurity = new FixedMetadataValue(NoxMMO.getInstance(), "HammerSecurity");
	
	public static final String ABILITY_NAME = "Hammer of Thor";
	public static final String PERM_NODE = "hammer-of-thor";
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitListener;
	private BaseMMOEventHandler<EntityDamageEvent> hitEntityListener;
	
	private double distanceVelo;
	private double damageMultiplier;
	private boolean active = false;
	
	private void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;
		
		if (changed)
			if (active) {
				registerHandler(hitEntityListener);
				registerHandler(hitListener);
			} else {
				unregisterHandler(hitEntityListener);
				unregisterHandler(hitListener);
			}
	}
	
	/**
	 * 
	 * @return distanceVelo The currently set multiplier for the users direction used as a velocity
	 */
	public double getDistanceVelo() {return distanceVelo;}

	/**
	 * 
	 * @param distanceVelo double multiplier of the users direction used as a velocity
	 * @return HammerOfThorAbility This instance, used for chaining
	 */
	public HammerOfThorPlayerAbility setDistanceVelo(double distanceVelo) {this.distanceVelo = distanceVelo; return this;}

	/**
	 * Gets the current damage multiplier
	 * 
	 * @return HammerOfThorAbility this
	 */
	public double getDamageMultiplier() { return damageMultiplier; }

	
	/**
	 * Sets the damage multiplier
	 * 
	 * @param damageMultiplier
	 * @return HammerOfThorAbility this
	 */
	public HammerOfThorPlayerAbility setDamageMultiplier(double damageMultiplier) { this.damageMultiplier = damageMultiplier; return this; }


	/**
	 * 
	 * @param player The user of this ability instance
	 */
	public HammerOfThorPlayerAbility(Player player){
			super(ABILITY_NAME, player);
			
			this.distanceVelo = 1.5;
			this.damageMultiplier = 4;
			
			this.hitEntityListener = new BaseMMOEventHandler<EntityDamageEvent>(
					new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageEvent").toString(),
					EventPriority.LOW, 1) {
				
				public boolean ignoreCancelled() {
					return true;
				}
				
				public Class<EntityDamageEvent> getEventType() {
					return EntityDamageEvent.class;
				}
				
				public String getEventName() {
					return "EntityDamageEvent";
				}
				
				public void execute(EntityDamageEvent event) {
					if (!active) {
						unregisterHandler(this);
						return;
					}
					
					Projectile damager = DamageUtil.getAttackingProjectile(event);
					
					if (damager != null && damager.hasMetadata("HammerSecurity")) {
						event.setDamage(event.getDamage() * getDamageMultiplier());
						damager.remove();
						setActive(false);
					}
					
					return;
				}
			};
			
			this.hitListener = new BaseMMOEventHandler<ProjectileHitEvent>(
					new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
					EventPriority.NORMAL, 1) {
				
				public boolean ignoreCancelled() {
					return true;
				}
				
				public Class<ProjectileHitEvent> getEventType() {
					return ProjectileHitEvent.class;
				}
				
				public String getEventName() {
					return "ProjectileHitEvent";
				}
				
				public void execute(ProjectileHitEvent event) {
					if (!active) {
						unregisterHandler(this);
						return;
					}
					
					Projectile a = event.getEntity();
					if (a != null && a.hasMetadata("HammerSecurity")) {
							a.remove();
							setActive(false);
					}
					
					return;
				}
			};
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final Player p = getPlayer();
		
		Arrow a = p.launchProjectile(Arrow.class);
		a.setMetadata("HammerSecurity", hammerSecurity);
		a.setVelocity(p.getLocation().getDirection().multiply(distanceVelo));
		
		ItemStack hammer = new ItemStack(Material.DIAMOND_AXE);
		ItemMeta meta = hammer.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
		hammer.setItemMeta(meta);
		
		NoxPacketUtil.disguiseArrow(a, hammer);
		
		setActive(true);
		return true;
	}
	
}