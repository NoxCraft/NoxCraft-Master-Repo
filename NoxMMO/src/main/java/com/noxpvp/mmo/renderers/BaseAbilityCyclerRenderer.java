package com.noxpvp.mmo.renderers;

import com.noxpvp.core.runners.RenderRunner;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.MMOPlayer;

public abstract class BaseAbilityCyclerRenderer extends RenderRunner {

	public static final BaseAbilityCyclerRenderer dummy = new BaseAbilityCyclerRenderer() {
		@Override
		public void renderNext() {

		}

		@Override
		public void renderPrevious() {

		}

		@Override
		public void renderCurrent() {

		}
	};

	private AbilityCycler cycler;

	public BaseAbilityCyclerRenderer(AbilityCycler cycler) {
		this.cycler = cycler;
	}

	private BaseAbilityCyclerRenderer() {
		this(null);
	}

	public AbilityCycler getCycler() {
		return cycler;
	}

	@Override
	public final void render() {
		renderCurrent();;
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 */
	public abstract void renderNext();

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 */
	public abstract void renderPrevious();

	/**
	 * Renders a view provided by the current through {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	public abstract void renderCurrent();
}
