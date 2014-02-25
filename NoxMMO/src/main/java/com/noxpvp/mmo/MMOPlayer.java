package com.noxpvp.mmo;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.events.PlayerDataSaveEvent;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.abilities.PlayerAbility;
import com.noxpvp.mmo.classes.ExperienceType;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {
	
	public MMOPlayer(OfflinePlayer player)
	{
		super(player);
	}
	
	public MMOPlayer(String name)
	{
		super(name);
	}

	public MMOPlayer(NoxPlayerAdapter player)
	{
		super(player);
	}
	
	public PlayerClass getPrimaryClass() {
		throw new NotImplementedException("PlayerClasses not completed.");
	}
	
	public PlayerClass getSecondaryClass() {
		throw new NotImplementedException("PlayerClasses not completed.");
	}
	
	public void setClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setClass(PlayerClass c) {
		if (c.isPrimaryClass())
			setPrimaryClass(c);
		else
			setSecondaryClass(c);
	}
	
	public void setPrimaryClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setPrimaryClass(PlayerClass c) {
		throw new NotImplementedException("PlayerClasses not completed.");
	}
	
	public void setSecondaryClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setSecondaryClass(PlayerClass c) {
		throw new NotImplementedException("PlayerClasses not completed.");
	}
	
	public LivingEntity getTarget() {
		throw new NotImplementedException("Targets need reimplementation");
	}
	
	public void setTarget(LivingEntity entity) {
		throw new NotImplementedException("Targets need reimplementation");
	}
	
	public PlayerAbility[] getPlayerAbilities() {
		throw new NotImplementedException();
	}
	
	public PassiveAbility<?>[] getPassiveAbilities() {
		throw new NotImplementedException();
	}
	
	public void addExp(ExperienceType type, int amount) {
		throw new NotImplementedException();
		
		/*
		 * If successful, do this
		 */
//		Player p = getPlayer();
//		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, 0);
	}
	
	public void load() {
		// TODO Implement loading below this line.
		
	}
	
	public void save() {
		
		// TODO Implement saving above this line.
		CommonUtil.callEvent(new PlayerDataSaveEvent(this, true));
	}
}
