package com.noxpvp.core.gui.rendering;

import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.runners.RenderRunner;

public abstract class CoreBoardRenderer extends RenderRunner implements ICoreBoardRenderer {

	private CoreBoard board;

	public CoreBoardRenderer(CoreBoard board) {
		this.board = board;
	}

	public final void render() {
		updateRender();
		board.updateScores();
	}

	protected abstract void updateRender();

	@Override
	public final void startRender() {
		board.show();
	}

	@Override
	public final void stopRender() {
		board.hide();
	}



}
