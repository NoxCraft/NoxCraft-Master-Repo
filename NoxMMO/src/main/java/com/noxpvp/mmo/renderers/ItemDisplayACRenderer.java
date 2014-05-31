/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.renderers;

import com.noxpvp.mmo.AbilityCycler;

public class ItemDisplayACRenderer extends BaseAbilityCyclerRenderer {

	public ItemDisplayACRenderer(AbilityCycler cycler) {
		super(cycler);
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 */
	@Override
	public void renderNext() {

	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 */
	@Override
	public void renderPrevious() {

	}

	/**
	 * Renders a view provided by the current through {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	@Override
	public void renderCurrent() {

	}
}
