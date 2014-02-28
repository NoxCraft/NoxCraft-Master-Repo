package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class MeasuringTapeAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Measuring Tape";
	public final static String PERM_NODE = "measuring-tape";
	
	public static Map<String, MeasuringTapeAbility> players = new HashMap<String, MeasuringTapeAbility>();
	
	private final Block b;

	public MeasuringTapeAbility(Player player, Block b) {
		super(ABILITY_NAME, player);
		
		this.b = b;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final Player p = getPlayer();
		
		CommonPacket commonFakeBlock = new CommonPacket(35, true);
		//NMSPacketPlayOutBlockAction fakeBlock = new NMSPacketPlayOutBlockAction();
		
		try {
			commonFakeBlock.write(0, b.getX());
			commonFakeBlock.write(1, b.getY());
			commonFakeBlock.write(2, b.getZ());
			commonFakeBlock.write(3, Material.WOOL.getId());
			commonFakeBlock.write(4, 14);
			
			PacketUtil.sendPacket(p, commonFakeBlock);
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e) {e.printStackTrace();}
		
		if (MeasuringTapeAbility.players.containsKey(p.getName())){
			final MeasuringTapeAbility first = MeasuringTapeAbility.players.get(p.getName());//save copy	
			MeasuringTapeAbility.players.remove(p.getName());//then remove
			
			p.sendMessage(ChatColor.GOLD + "Distance: " + ChatColor.GREEN + (int) first.b.getLocation().distance(b.getLocation()));
			
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				
				private Block bTwo = first.b;
				private World w = p.getWorld();
				
				public void run() {
					CommonPacket refreshBlock = new CommonPacket(35, true),
							refreshBlockTwo = new CommonPacket(35, true);
					//NMSPacketPlayOutBlockAction fakeBlock = new NMSPacketPlayOutBlockAction();
					
					try {
					
					refreshBlock.write(0, b.getX());
					refreshBlock.write(1, b.getY());
					refreshBlock.write(2, b.getZ());
					refreshBlock.write(3, b.getData());
					
					refreshBlockTwo.write(0, bTwo.getX());
					refreshBlockTwo.write(1, bTwo.getY());
					refreshBlockTwo.write(2, bTwo.getZ());
					refreshBlockTwo.write(3, bTwo.getData());
					
					PacketUtil.sendPacket(p, refreshBlock);
					PacketUtil.sendPacket(p, refreshBlockTwo);
					}
					catch (IllegalArgumentException e) {}
					catch (Exception e) {e.printStackTrace();}
					
				}
			}, 60);
		} else {		
			MeasuringTapeAbility.players.put(p.getName(), this);
		}
		
		return true;
	}

}
