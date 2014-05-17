package com.noxpvp.mmo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.entity.BandageAbility;
import com.noxpvp.mmo.abilities.entity.ChargeAbility;
import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.entity.WisdomAbility;
import com.noxpvp.mmo.abilities.player.FireBallAbility;
import com.noxpvp.mmo.abilities.player.FireSpinAbility;
import com.noxpvp.mmo.abilities.player.GuardianAngelAbility;
import com.noxpvp.mmo.abilities.player.HammerOfThorAbility;
import com.noxpvp.mmo.abilities.player.HookShotAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionAbility;
import com.noxpvp.mmo.abilities.player.MedPackAbility;
import com.noxpvp.mmo.abilities.player.TornadoAbility;
import com.noxpvp.mmo.abilities.targeted.CurseAbility;
import com.noxpvp.mmo.abilities.targeted.PickPocketAbility;
import com.noxpvp.mmo.abilities.targeted.SootheAbility;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;

@SuppressWarnings("unused")
public class PlayerInteractListener extends NoxListener<NoxMMO>{

	PlayerManager pm;
	
	public PlayerInteractListener(NoxMMO mmo)
	{
		super(mmo);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {
		
		Player p;
		MMOPlayer player = pm.getPlayer((p = e.getPlayer()));
		if (player == null) return;
		
		new TargetAbility(p).execute(e);//TODO make default range configized || passiveness
		
		//debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;

		//debug here
//		new TornadoAbility(p, 25, 20 * 10).execute();
//		new FireSpinAbility(p).execute();
//		new MassDestructionAbility(p).execute();
//		new LeapAbility(p).execute();
//		new CurseAbility(p).execute();
//		new ChargeAbility(p).execute();
//		new MedPackAbility(p).execute();
//		new PickPocketAbility(p).execute();
//		new HookShotAbility(p).execute();//XXX do this
//		new FireBallAbility(p).execute();
//		new HammerOfThorAbility(p).execute();
//		new GuardianAngelAbility(p).execute();
//		new BandageAbility(p).execute();
//		new WisdomAbility(p).execute();
//		new SootheAbility(p).execute();
	}
	
}
