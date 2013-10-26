package com.noxpvp.mmo.abilities.player;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;

//TODO Finish ability on event-side. ask bbc what he intends to happen at that point
public class MassDestructionAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Mass Destruction";
	public HashSet<Player> massDestructors;
	private double hVelo = 4;
	
	public MassDestructionAbility sethVelo(double velo) {this.hVelo = velo; return this;}
	public double gethVelo() {return this.hVelo;}

	public MassDestructionAbility(Player p){
		super(ABILITY_NAME, p);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		Vector up = p.getLocation().getDirection();
		up.setY(gethVelo());
		Vector down = p.getLocation().getDirection();
		down.setY(-gethVelo());
		
		Runnable shootUp = new SetVelocityRunnable(getEntity(), up);
		Runnable shootDown = new SetVelocityRunnable(getEntity(), down);
		
		Bukkit.getScheduler().runTask(NoxMMO.getInstance(), shootUp);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), shootDown, 30);
		
		massDestructors.add(p);
		return true;
	}

	public boolean mayExecute() {
		return getPlayer() != null;
	}

}
