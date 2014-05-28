package com.noxpvp.core.gui.rendering;

import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.runners.RenderRunner;

public abstract class CoreBoardRender extends RenderRunner {

	private CoreBoard board;

	public CoreBoardRender(CoreBoard board) {
		this.board = board;
	}

	public final void render() {
		updateRender();
		board.updateScores();
	}

	protected abstract void updateRender();

	public final void startRender() {
		board.show();
	}

	public final void stopRender() {
		board.hide();
	}



}
