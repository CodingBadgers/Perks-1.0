package me.wisbycraft.perks.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerkArgSet {

	private List<String> m_flags = new ArrayList<String>();
	private List<String> m_args = new ArrayList<String>();
	
	public PerkArgSet(String[] args) {
		parseArgSet(args);
	}
	
	public PerkArgSet(String[] args, List<String> flags) {
		m_flags = flags;
		
		for (int i = 0; i < args.length; i ++) {
			m_args.add(args[i]);
		}
		
		for (int i = 0; i < m_args.size(); i ++) {
			if (m_args.get(i) == null) 
				m_args.remove(i);
		}
	}
	
	public void parseArgSet(String[] input) {		
		
		List<String> flags = new ArrayList<String>();
		String[] args = new String[input.length];

		// get the flags provided
		int k = 0;
		for (int i = 0; i < input.length; i ++) {	
			if (input[i].startsWith("-") && !input[i].matches("^[\\-0-9\\.]+,[\\-0-9\\.]+,[\\-0-9\\.]+(?:.+)?$")) {
				for (int x = 0; x < input[i].length(); x++) {
					if (input[i].charAt(x) == '+') 
						continue;
					flags.add(String.valueOf(input[i].charAt(x)));
				}
			} else if (input[i].length() > 0 && input[i] != null){
				args[k] = input[i];
				k++;
			}
		}
		
		for (int i = 0; i < args.length; i ++) {
			m_args.add(args[i]);
		}
		
		for (int i = 0; i < m_args.size(); i ++) {
			if (m_args.get(i) == null) 
				m_args.remove(i);
		}
		
		m_flags = flags;
	
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
