package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ReincarnateAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Reincarnate";
	public final static String PERM_NODE = "reincarnate";
	
	private double maxRadius;
	private int timeLimit;
	
	/**
	 * Sets the radius to check for a players last death location
	 * 
	 * @param maxRadius The radius this ability instance should check for a players last death location
	 * @return ReincarnateAbility This instance
	 */
	public ReincarnateAbility setMaxRadius(double maxRadius) {
		this.maxRadius = maxRadius;
		return this;
	}
	
	/**
	 * Gets the radius the ability will check for a players last death location - Defaults to 10
	 * 
	 * @return double The radius this ability instance is currently set to check for a players last death location
	 */
	public double getMaxRadius() {
		return maxRadius;
	}
	
	/**
	 * The max amount of seconds that have passed to Reincarnate the target player
	 * 
	 * @param timeLimit The max amount of seconds that can have passed
	 * @return ReincarnateAbility This instance
	 */
	public ReincarnateAbility setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
		return this;
	}
	
	/**
	 * the currently set max amount of seconds that have passed to Reincarnate a player - Defaults to 60 seconds
	 * 
	 * @return Integer The max amount of seconds ago the target player can have died
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * Constructs a new ReincarnateAbility with a specified player as the user
	 * 
	 * @param player The user for this ability instance
	 */
	public ReincarnateAbility(Player player){
		super(ABILITY_NAME, player);
		this.timeLimit = 60;
		this.maxRadius = 10;
	}
	
	/**
	 * Constructs a new ReincarnateAbility with a specified player as the user
	 * 
	 * @param playey The user for this ability instance
	 * @param timeLimit The max amount of seconds that can have passed since the target player died
	 * @param maxRadius the max amount of from the user to check for a targets players death location
	 */
	public ReincarnateAbility(Player player, int timeLimit, double maxRadius){
		super(ABILITY_NAME, player);
		
		this.timeLimit = timeLimit;
		this.maxRadius = maxRadius;
	}
	
	/**
	 * Returns if the ability execution was carried out successfully
	 * 
	 * @return boolean If this ability executed successfully
	 */
	public boolean execute(){
		if (!mayExecute())
			return false;
		
		PlayerManager pm = NoxMMO.getInstance().getPlayerManager();
		
		Player p = getPlayer();
		Location pLoc = p.getLocation();
		
		Player target = null;
		long ct = System.currentTimeMillis();
		
		for (Player pl : Bukkit.getOnlinePlayers()){
			MMOPlayer mmoP = pm.getMMOPlayer(pl.getName());
			
			if (mmoP.getLastDeathLoc().distance(pLoc) > getMaxRadius()) continue;
			if (((ct - mmoP.getLastDeathTimeStamp()) / 1000) > timeLimit) continue;
			
			target = pl;
			break;
		}
		
		if (target == null) return false;
		
		target.teleport(pLoc);
		target.playEffect(EntityEffect.WOLF_HEARTS);
		return true;
	}
	
	/**
	 * Returns is the player of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute(){
		return getPlayer() != null;
	}
}
