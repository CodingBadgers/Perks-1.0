package me.wisbycraft.perks.utils;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class Kit {

	private String m_name = null;
	private ArrayList<ItemStack> m_items = null;
	private int m_timeout = 0;
	
	public Kit(String name, int timeout, ArrayList<ItemStack> items) {
		m_name = name;
		m_items = items;
		m_timeout = timeout;
	}
	
	public ArrayList<ItemStack> getItems() {
		return m_items;
	}
	
	public String getName() {
		return m_name;
	}
	
	public int getTimeout() {
		return m_timeout;
	}
}
