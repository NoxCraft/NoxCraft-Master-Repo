package com.noxpvp.mmo.renderers;

import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.rendering.ICoreBoardRenderer;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.abilities.Ability;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CoreBoardACRenderer extends BaseAbilityCyclerRenderer implements ICoreBoardRenderer {
	private CoreBoard board;
	private List<String> entries;
	private String currentEntry, nextEntry, prevEntry;

	public CoreBoardACRenderer(AbilityCycler cycler) {
		this(cycler, new CoreBoard("Abilities", cycler.getPlayer()));
		entries = new ArrayList<String>();

		for (Ability ability : cycler.getList())
			entries.add(ability.getName());
	}

	public CoreBoardACRenderer(AbilityCycler cycler, CoreBoard board) {
		super(cycler);
		this.board = board;
	}

	//Warning: This is slightly intensive if constantly being updated.
	private void update() {
		board.clear();
		for (String entry : entries)
			board.addEntry(entry,  entry, (entry.equals(currentEntry)? ChatColor.YELLOW: entry.equals(nextEntry) || entry.equals(prevEntry)? ChatColor.AQUA: null));
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 * @deprecated Please use {@link #renderCurrent()}
	 */
	@Override
	@Deprecated
	public void renderNext() {
		renderCurrent();
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 * @deprecated Please use {@link #renderCurrent()}
	 */
	@Override
	@Deprecated
	public void renderPrevious() {
		renderCurrent();
	}

	/**
	 * Renders a view provided by the current through {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	@Override
	public void renderCurrent() {
		update();
	}

	@Override
	public void startRender() {
		board.show();
	}

	@Override
	public void stopRender() {
		board.hide();
	}
}
