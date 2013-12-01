package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;

/**
 * @author NoxPVP
 *
 */
//TODO Finish ability on event-side. ask bbc what he intends to happen at that point
public class MassDestructionAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "mass-destruction";
	public static final String ABILITY_NAME = "Mass Destruction";
	
	/**
	 * 
	 * 
	 * The class list of current mass destructors. Used for event side checking
	 */
	public static List<Player> massDestructors = new ArrayList<Player>();
	private double hVelo = 4;
	
	/**
	 * 
	 * 
	 * @param velo Double velocity value for player upwards/downwards effect
	 * @return MassDestructionAbility This instance, used for chaining
	 */
	public MassDestructionAbility sethVelo(double velo) {this.hVelo = velo; return this;}
	
	/**
	 * 
	 * 
	 * @return Double The current set velocity used for the player upwards/downwards effect
	 */
	public double gethVelo() {return this.hVelo;}

	/**
	 * 
	 * 
	 * @param p The Player type user for this instance
	 */
	public MassDestructionAbility(Player p){
		super(ABILITY_NAME, p);
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		NoxMMO instance = NoxMMO.getInstance();
		
		Player p = getPlayer();
		
		Vector up = p.getLocation().getDirection();
		up.setY(gethVelo());
		Vector down = p.getLocation().getDirection();
		down.setY(-gethVelo());
		
		SetVelocityRunnable shootUp = new SetVelocityRunnable(getEntity(), up);
		SetVelocityRunnable shootDown = new SetVelocityRunnable(getEntity(), down);
		
		shootUp.runTask(instance);
		shootDown.runTaskLater(instance, 30);
		
		MassDestructionAbility.massDestructors.add(getPlayer());
		return true;
	}

	/**
	 * 
	 * 
	 * @return Boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}

}
