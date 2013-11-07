package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class LeapAbility extends BaseEntityAbility {

	private static final String ABILITY_NAME = "Leap";
	public final static String PERM_NODE = "leap";
	
	private double hPower;
	private double vPower;
	
	public LeapAbility(Entity ent, double hPower, double vPower)
	{
		super(ABILITY_NAME, ent);
		this.hPower = hPower;
		this.vPower = vPower;
	}
	
	public double getHPower() { return hPower; }
	public double getVPower() { return vPower; }
	
	public void setHPower(double value) { hPower = value; }
	public void setVPower(double value) { vPower = value; }
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Vector newVelocity = getEntity().getLocation().getDirection();
		
		newVelocity.setY(0).multiply(hPower).setY(vPower);
		
		getEntity().setVelocity(newVelocity);		
		return true;
	}

	public boolean mayExecute() {
		if (isValid() && getEntity() instanceof Player)
			return VaultAdapter.permission.has((Player)getEntity(), NoxMMO.PERM_NODE + ".abilities."+PERM_NODE);
		return isValid();
	}

}
