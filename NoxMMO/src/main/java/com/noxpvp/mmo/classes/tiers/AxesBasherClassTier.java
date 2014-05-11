package com.noxpvp.mmo.classes.tiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.CriticalHitAbility;
import com.noxpvp.mmo.classes.internal.*;

public class AxesBasherClassTier extends ClassTier {

	public static final String TIER_NAME = "Basher";
	
	private Map<String, Ability> abilities = new HashMap<String, Ability>();
	
	private volatile static String dName = "Basher";
	
	public AxesBasherClassTier(PlayerClass retainer) {
		super(retainer, TIER_NAME, 1);
		
		//abilities.put("Iron Grip", /* HERP */)
		abilities.put("Leap", new LeapAbility(retainer.getPlayer()));
		abilities.put("CriticalHit", new CriticalHitAbility(retainer.getPlayer()));
	}

	public void setDisplayName(String displayName) {
		AxesBasherClassTier.dName = displayName;
	}

	public String getDisplayName() {
		return AxesBasherClassTier.dName;
	}

	public List<String> getLore() {
		return Collections.emptyList();//XXX implement soon
	}

	public int getMaxExp(int level) {
		return 0;
	}

	public int getNeededExp() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNeededExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLevel(int level) {
		// TODO Auto-generated method stub

	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setExp(int amount) {
		// TODO Auto-generated method stub

	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.unmodifiableMap(abilities);
	}

	public ExperienceType[] getExpTypes() {
		return ExperienceType.COMBAT;
	}

	@Override
	public void load(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

}