package com.noxpvp.mmo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.effect.shaped.LevelUpGlimmer;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.entity.BandageEntityAbility;
import com.noxpvp.mmo.abilities.entity.ChargeEntityAbility;
import com.noxpvp.mmo.abilities.entity.FireNovaEntityAbility;
import com.noxpvp.mmo.abilities.entity.LeapEntityAbility;
import com.noxpvp.mmo.abilities.entity.WisdomEntityAbility;
import com.noxpvp.mmo.abilities.player.FireBallPlayerAbility;
import com.noxpvp.mmo.abilities.player.FireSpinPlayerAbility;
import com.noxpvp.mmo.abilities.player.GuardianAngelPlayerAbility;
import com.noxpvp.mmo.abilities.player.HammerOfThorPlayerAbility;
import com.noxpvp.mmo.abilities.player.MedPackPlayerAbility;
import com.noxpvp.mmo.abilities.player.ReincarnatePlayerAbility;
import com.noxpvp.mmo.abilities.player.RejuvenationPlayerAbility;
import com.noxpvp.mmo.abilities.player.TornadoPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.HookShotPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.MassDestructionPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.ThrowPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.BoltPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.CursePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.DrainLifePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.MortalWoundPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.PickPocketPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.SoothePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.SoulStealPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.TargetPlayerAbility;
import com.noxpvp.mmo.events.EntityAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerTargetedAbilityExecutedEvent;

@SuppressWarnings("unused")
public class PlayerInteractListener extends NoxListener<NoxMMO> {

	MMOPlayerManager pm;

	public PlayerInteractListener(NoxMMO mmo) {
		super(mmo);

		this.pm = MMOPlayerManager.getInstance();
	}

	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
	}

	private static BasePlayerAbility ab;
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
//		new TornadoAbility(p, 25, 20 * 10).execute();
//		new FireSpinAbility(p).execute();
//		new MassDestructionAbility(p).execute();
//		new LeapAbility(p).execute();
//		new CurseAbility(p).execute();
//		new ChargeAbility(p).execute();
//		new MedPackAbility(p).execute();
//		new PickPocketAbility(p).execute();
//		new HookShotAbility(p).execute();//XXX do this
		
		MMOPlayer mp = MMOPlayerManager.getInstance().getPlayer(p);
		if (ab == null)
			ab = new HookShotPlayerAbility(p);
		
		if (ab != null && ab.execute())
			CommonUtil.callEvent(new PlayerAbilityExecutedEvent(p, ab));
		
//		new HammerOfThorAbility(p).execute();
//		new GuardianAngelAbility(p).execute();
//		new BandageAbility(p).execute();
//		new WisdomAbility(p).execute();
//		new SootheAbility(p).execute();
//		new ThrowAbility(p).execute();
//		new MortalWoundAbility(p).execute();
//		new FireNovaAbility(p).execute();
//		new BoltAbility(p).execute();
//		new RejuvenationAbility(p).execute();
//		new ReincarnateAbility(p).execute();
//		new DrainLifeAbility(p).execute();
//		new SoulStealAbility(p).execute();
//		new LevelUpGlimmer(p.getLocation(), 20 * 10, 10).start(0);

//		Bukkit.broadcastMessage(p.getLocation().getDirection().getY() + "");
	}

}
