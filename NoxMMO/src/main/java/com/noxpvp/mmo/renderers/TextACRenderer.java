package com.noxpvp.mmo.renderers;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.AbilityCycler;
import org.bukkit.entity.Player;

public class TextACRenderer extends BaseAbilityCyclerRenderer {

	public TextACRenderer(AbilityCycler cycler) {
		super(cycler);
	}

	@Override
	public void renderNext() {
		if (getCycler() != null && getCycler().getPlayer() != null)
			MessageUtil.sendMessage(getCycler().getPlayer(), MessageUtil.parseColor(getCycler().peekNext().getDisplayName()));
	}

	@Override
	public void renderPrevious() {
		if (getCycler() != null && getCycler().getPlayer() != null)
			MessageUtil.sendMessage(getCycler().getPlayer(), MessageUtil.parseColor(getCycler().peekPrevious().getDisplayName()));

	}

	@Override
	public void renderCurrent() {
		if (getCycler() != null) {
			Player p = getCycler().getPlayer();
			if (p != null)
				MessageUtil.sendMessage(p, MessageUtil.parseColor(getCycler().current().getDisplayName()));
		}
	}
}
