package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ShurikenAbility extends BasePlayerAbility{
        
        public static final String ABILITY_NAME = "Shuriken";
        public static final String PERM_NODE = "shuriken";
        
        //TODO make this
        
        private double distanceVelo;
        
        /**
         * 
         * @return distanceVelo The currently set multiplier for the users direction used as a velocity
         */
        public double getDistanceVelo() {return distanceVelo;}

        /**
         * 
         * @param distanceVelo double multiplier of the users direction used as a velocity
         * @return ShurikenAbility This instance, used for chaining
         */
        public ShurikenAbility setDistanceVelo(double distanceVelo) {this.distanceVelo = distanceVelo; return this;}
        
        /**
         * 
         * @param player The user of this ability instance
         * @param distanceVelo double multiplier of the users direction used as a velocity
         */
        public ShurikenAbility(Player player, double distanceVelo){
        	super(ABILITY_NAME, player);
        	
        	this.distanceVelo = distanceVelo;
        }

        /**
         * 
         * @param player The user of this ability instance
         */
        public ShurikenAbility(Player player){
                super(ABILITY_NAME, player);
                
                this.distanceVelo = 1;
        }

        /**
         * 
         * @return boolean If this ability executed successfully
         */
        public boolean execute() {
                if (!mayExecute())
                        return false;
                
                final Player p = getPlayer();
                
                Arrow a = p.launchProjectile(Arrow.class);
                a.setShooter(p);
                a.setVelocity(p.getLocation().getDirection().multiply(distanceVelo));
                
                return true;
        }
        
}