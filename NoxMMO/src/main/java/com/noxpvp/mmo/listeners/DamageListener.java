package com.noxpvp.mmo.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.core.utils.StaticEffects;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.locale.MMOLocale;

public class DamageListener extends NoxListener<NoxMMO>{

	PlayerManager pm;
	
	public DamageListener(NoxMMO mmo)
	{
		super(mmo);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public DamageListener() {
		this(NoxMMO.getInstance());
	}
	
	

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {	
		
//		Entity e = event.getEntity();
		
		LivingEntity livingDamaged = DamageUtil.getDamagedEntity(event),
				livingAttacker = DamageUtil.getAttackingLivingEntity(event);
		
		Player playerDamaged = DamageUtil.getDamagedPlayer(event),
				playerAttacker = DamageUtil.getAttackingPlayer(event);
		
		if (playerAttacker != null) {
			
			if (livingDamaged != null) {
				com.noxpvp.mmo.PlayerManager.getInstance().getPlayer(playerAttacker).setTarget(livingDamaged);
				
				if (getPlugin().getMMOConfig().get("effect.damage.blood", Boolean.class, Boolean.TRUE)){
					StaticEffects.BloodEffect(livingDamaged, getPlugin());
				}
				if (getPlugin().getMMOConfig().get("effect.damage.damage-particle", Boolean.class, Boolean.TRUE)){
					StaticEffects.DamageAmountParticle(livingDamaged, event.getDamage());
				}
				
				String color = MMOLocale.GUI_BAR_COLOR.get();
				CoreBar bar = pm.getPlayer(playerAttacker).getCoreBar();
				
				if (playerDamaged != null) {
					
					NoxPlayer noxPlayerDamaged = pm.getPlayer(playerDamaged.getName());
					
					if (noxPlayerDamaged != null)
						bar.newLivingTracker(livingDamaged, noxPlayerDamaged.getFullName(), false);
						
					
				} else {
					bar.newLivingTracker(livingDamaged, livingDamaged.getType().name(), false);
				}
				
			}
		}
		
	}
	
}
