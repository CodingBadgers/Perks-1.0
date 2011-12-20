package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkPlayer {

	private Player m_player = null;					//!< store the bukkit player
	private SpoutPlayer m_spoutPlayer = null;		//!< store the spout player (spout has more commands so if we can use them why not :P)

	// Fly related members //
	private boolean m_flying = false;				//!< is the player flying?
	private boolean m_jumping = false;				//!< has the player got jump pressed (spout only)
	private boolean m_sneaking = false;				//!< has the player got sneak pressed (spout only)
	private PerkMagicCarpet m_magicCarpet = null;	//!< A players magic carpet object (Non spout only)
	// end fly related members //
	
	// hunger related members //
	private float m_hungerRate = 0.25f;				//!< means hunger goes down at 1/4 the normal rate <- will load from config when thats done
	private float m_hungerCounter = 0.0f;			//!< stores the last hunger counter, if this is equal to 1.0 (100%) let a hunger event work as normal
	// end hunger related members //

	public PerkPlayer(Player player) {
		m_player = player;
		m_spoutPlayer = (SpoutPlayer) player;

		// if the player isnt using spout make a magic carpet
		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			m_magicCarpet = new PerkMagicCarpet();
		}
	}

	// called when a player is kicked or leaves...
	// all cleanups should be done in here
	public void remove() {
		if (m_magicCarpet != null) {
			m_magicCarpet.destroy();
		}
	}

	// returns the bukkit player
	public Player getPlayer() {
		return m_player;
	}

	// returns the spout player
	public SpoutPlayer getSpoutPlayer() {
		return m_spoutPlayer;
	}

	public void setFlying(boolean flying) {
		
		// output a message to the user
		if (flying) {
			PerkUtils.OutputToPlayer(this, "Fly mode is now enabled");
		} else {
			PerkUtils.OutputToPlayer(this, "Fly mode is now disabled");
		}
		
		// if player isnt using spout create or destroy there magic carpet
		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			if (flying) {
				m_magicCarpet.create(m_player);
			} else {
				m_magicCarpet.destroy();
			}
		}

		// store whether we're flying or not
		m_flying = flying;
	}

	public boolean isFlying() {
		return m_flying;
	}

	public boolean isJumping() {
		return m_jumping;
	}

	public void setJumping(boolean jumping) {
		m_jumping = jumping;
	}

	public boolean isSneaking() {
		return m_sneaking;
	}

	public void setSneaking(boolean sneaking) {
		m_sneaking = sneaking;
	}

	public PerkMagicCarpet getMagicCarpet() {
		return m_magicCarpet;
	}

	// checks whether a player has permission to do something or not
	// uses PEX
	public boolean hasPermission(String permission) {
		PermissionManager permissions = PermissionsEx.getPermissionManager();
		if (permissions.has(m_player, permission) || m_player.isOp())
			return true;

		return false;
	}

	// checks to see if a hunger event should be cancelled or not
	public boolean shouldLowerHunger() {
		
		// increase the hunger counter
		m_hungerCounter += m_hungerRate;
		
		// if the counter is at 100%, reset the counter and let the hunger event fire
		if (m_hungerCounter == 1.0f) {
			m_hungerCounter = 0.0f;
			return true;
		}
		
		// cancel the hunger event
		return false;
	}

}