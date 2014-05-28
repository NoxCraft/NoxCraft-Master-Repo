package com.noxpvp.core.runners;

import com.noxpvp.core.gui.rendering.IRenderer;

/**
 * Shortcut to making renderers able to be thrown through something requiring runnables.
 * <p>This is probably the best way to throw renderers through things.</p>
 */
public abstract class RenderRunner implements IRenderer, Runnable {
	public final void run() {
		render();
	}
}
