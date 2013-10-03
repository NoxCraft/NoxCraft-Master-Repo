package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.mmo.abilities.PlayerAbility;

public class LeapAbility implements PlayerAbility {

	private NoxPlayer player;
	public final static String ABILITY_NAME = "Leap";

	public LeapAbility(NoxPlayer player){
		this.player = player;
	}
	
	public Player getPlayer() {
		return player.getPlayer();
	}

	public Player getEntity() {
		return getPlayer();
	}

	public NoxPlayer getNoxPlayer() {
		return player;
	}

	public String name() {
		return ABILITY_NAME;
	}

	public boolean execute(final double heightVelocity,final double forwardVelocity, final double power)
	{
		if (!mayExecute())
			return false;
		
		Vector newVelocity = player.getPlayer().getLocation().getDirection();
		
		newVelocity.setY(0).multiply(forwardVelocity * power).setY(heightVelocity * power);
		
		player.getPlayer().setVelocity(newVelocity);
		return true;
	}
	
	public boolean execute(double power)
	{
		double heightVel = 4.0;
		double forwardVel = 4.0;
		return execute(heightVel, forwardVel, power);
	}
	
	public boolean execute() {
		double heightVel = 4.0;
		double forwardVel = 4.0;
		double power = 1.0;
		
		return execute(heightVel, forwardVel, power);
	}

	public boolean mayExecute() {
		return player.getPlayer() != null;
	}
	
	public static String getName() {
		return ABILITY_NAME;
	}

}
