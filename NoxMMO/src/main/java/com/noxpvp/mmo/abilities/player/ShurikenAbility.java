package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ShurikenAbility extends BasePlayerAbility{
        public static List<Player> shurikenThrowers = new ArrayList<Player>();
        
        private final static String ABILITY_NAME = "Shuriken";
        private final static String PERM_NODE = "shuriken";
        
        
        private double distanceVelo;
        
        /**
         * 
         * @return distanceVelo - The currently set multiplier for the users direction used as a velocity
         */
        public double getDistanceVelo() {return distanceVelo;}

        /**
         * 
         * @param distanceVelo - double multiplier of the users direction used as a velocity
         * @return ShurikenAbility - This instance, used for chaining
         */
        public ShurikenAbility setDistanceVelo(double distanceVelo) {this.distanceVelo = distanceVelo; return this;}

        /**
         * 
         * @param player - The user of this ability instance
         */
        public ShurikenAbility(Player player){
                super(ABILITY_NAME, player);
        }
        
        /**
         * 
         * @param player - The user of this ability instance
         * @param distanceVelo - double multiplier of the users direction used as a velocity
         */
        public ShurikenAbility(Player player, double distanceVelo){
                super(ABILITY_NAME, player);
                this.distanceVelo = distanceVelo;
        }

        /**
         * 
         * @return boolean - If this ability executed successfully
         */
        public boolean execute() {
                if (!mayExecute())
                        return false;
                
                final Player p = getPlayer();
                
                Arrow a = p.launchProjectile(Arrow.class);
                a.setShooter(p);
                a.setVelocity(p.getLocation().getDirection().multiply(distanceVelo));
                
                ShurikenAbility.shurikenThrowers.add(p);
                Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
                        
                        public void run() {
                                if (ShurikenAbility.shurikenThrowers.contains(p))
                                        ShurikenAbility.shurikenThrowers.remove(p);
                                
                        }
                }, 50);
                
                return true;
        }

        /**
         * 
         * @return boolean - If the execute() method is normally able to start
         */
        public boolean mayExecute() {
                return getPlayer() != null;
        }
        
}