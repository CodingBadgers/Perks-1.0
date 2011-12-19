package me.wisbycraft.perks;

public class PerkUtils {

	static public void DebugConsole(String messsage) {
		System.out.println("[Perks] " + messsage + ".");
	}

	static public void OutputToPlayer(PerkPlayer player, String messsage) {
		player.getPlayer().sendMessage("\247b[Perks] \247f" + messsage + ".");
	}
}
