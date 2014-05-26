package com.noxpvp.mmo.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.entity.FireNovaEntityAbility;
import com.noxpvp.mmo.abilities.entity.WisdomEntityAbility;
import com.noxpvp.mmo.abilities.player.ChargePlayerAbility;
import com.noxpvp.mmo.abilities.player.FireSpinPlayerAbility;
import com.noxpvp.mmo.abilities.player.GuardianAngelPlayerAbility;
import com.noxpvp.mmo.abilities.player.LeapPlayerAbility;
import com.noxpvp.mmo.abilities.player.RejuvenationPlayerAbility;
import com.noxpvp.mmo.abilities.player.TornadoPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.HookShotPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.MassDestructionPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.ThrowPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.BoltPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.DrainLifePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.MortalWoundPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.SoothePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.TargetPlayerAbility;

public class PlayerInteractListener extends NoxListener<NoxMMO> {

	MMOPlayerManager pm;

	public PlayerInteractListener(NoxMMO mmo) {
		super(mmo);

		this.pm = MMOPlayerManager.getInstance();
	}

	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
	}

	private static Map<String, BasePlayerAbility> abs = new HashMap<String, BasePlayerAbility>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {

		Player p;
		MMOPlayer player = pm.getPlayer((p = e.getPlayer()));
		if (player == null) return;

		new TargetPlayerAbility(p).execute(e);//TODO make default range configized || passiveness

		//debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;

		//debug here
//		new TornadoPlayerAbility(p, 25, 20 * 10).execute();
//		new FireSpinPlayerAbility(p).execute();
//		new MassDestructionPlayerAbility(p).execute();
//		new LeapPlayerAbility(p).execute();
//		new CurseAbility(p).execute();
//		new ChargePlayerAbility(p).execute();
//		new MedPackAbility(p).execute();
//		new PickPocketAbility(p).execute();
//		new HookShotAbility(p).execute();//XXX do this
		
//		abs = new HashMap<String, BasePlayerAbility>();
		MMOPlayer mp = MMOPlayerManager.getInstance().getPlayer(p);
		if (!abs.containsKey(p.getName())) {
			BasePlayerAbility ab = new ThrowPlayerAbility(p);
			abs.put(p.getName(), ab);
		}
//
		abs.get(p.getName()).execute();
		
//		new FireSpinPlayerAbility(p).execute();
		
//		new HammerOfThorAbility(p).execute();
//		new GuardianAngelPlayerAbility(p).execute();
//		new BandageAbility(p).execute();
//		new WisdomEntityAbility(p).execute();
//		new SoothePlayerAbility(p).execute();
//		new ThrowPlayerAbility(p).execute();
//		new MortalWoundPlayerAbility(p).execute();
//		new FireNovaEntityAbility(p).execute();
//		new BoltPlayerAbility(p).execute();
//		new RejuvenationPlayerAbility(p).execute();
//		new ReincarnateAbility(p).execute();
//		new DrainLifePlayerAbility(p).execute();
//		new SoulStealAbility(p).execute();
//		new LevelUpGlimmer(p.getLocation(), 20 * 10, 10).start(0);

//		Bukkit.broadcastMessage(p.getLocation().getDirection().getY() + "");
	}

}
