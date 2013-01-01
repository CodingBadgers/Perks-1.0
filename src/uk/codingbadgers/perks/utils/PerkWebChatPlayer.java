package uk.codingbadgers.perks.utils;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkWebChatPlayer {

	private String m_name;
	private PermissionGroup m_group;
	private PermissionUser m_user;
	
	public PerkWebChatPlayer(String name) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		m_name = name;
		m_user = pex.getUser(name);
		m_group = m_user.getGroups()[0];
	}
	
	public String getName() {
		return m_name;
	}
	
	public String getPrefix() {
		return m_user.getPrefix();
	}
	
	public PermissionGroup getGroup() {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		return m_group == null ? pex.getDefaultGroup() : m_group ;
	}
	
	public PermissionUser getUser() {
		return m_user;
	}
}
