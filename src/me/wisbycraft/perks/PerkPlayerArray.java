package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

public class PerkPlayerArray extends ArrayList<PerkPlayer> {

	private static final long serialVersionUID = 1847806522351967601L;
	
	public boolean removePlayer(Player player) {
		
		PerkPlayer perkPlayer = getPlayer(player);
		if (perkPlayer != null) {
			remove(perkPlayer);
			return true;
		}
		
		return false;
	}

	public PerkPlayer getPlayer(Player player) {
		
		Iterator<PerkPlayer> itr = iterator();
		while(itr.hasNext()) {
			PerkPlayer currentPlayer = itr.next();
			if (currentPlayer.getPlayer().equals(player)) {
				return currentPlayer;
			}
		}	
		
		return null;
	}

}
