package me.wisbycraft.perks.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerkArgSet {

	private List<String> m_flags = new ArrayList<String>();
	private List<String> m_args = new ArrayList<String>();
	
	public PerkArgSet(String[] args, List<String> flags) {
		m_flags = flags;
		for (int i = 0; i < args.length; i ++) {
			m_args.add(args[i]);
		}
		for (int i = 0; i < args.length; i ++) {
			if (m_args.get(i) == null) 
				m_args.remove(i);
		}
	}
	
	public boolean hasFlag(String ch) {
		Iterator<String> itr = m_flags.iterator();
		while (itr.hasNext()) {
			String current = itr.next();
			if (current.equalsIgnoreCase(ch)) 
				return true;
		}
		return false;
	}
	
	public boolean hasFlag(char ch) {
		return hasFlag(String.valueOf(ch));
	}
	
	public List<String> getFlags() {
		return m_flags;
	}

	public String getString(int index) {
		return m_args.get(index);
	}
	
	public int getInt(int index) throws NumberFormatException{
		return Integer.parseInt(m_args.get(index));
	}
	
	public double getDouble(int index) throws NumberFormatException {
        return Double.parseDouble(m_args.get(index));
    }
	
	public float getFloat(int index) throws NumberFormatException {
		return Float.parseFloat(m_args.get(index));
	}
	
	public int size() {
		return m_args.size();
	}
}
