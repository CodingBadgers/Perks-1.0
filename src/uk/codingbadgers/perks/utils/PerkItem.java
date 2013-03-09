package uk.codingbadgers.perks.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PerkItem {

    private final Material material;
    private final short subTypeId;
    private final String name;
	private final String[][] search;
    
    public PerkItem(String name, String[][] search, Material material) {
        this.material = material;
        this.name = name;
        this.subTypeId = 0;
        this.search = search.clone();
    }

    public PerkItem(String name, String[][] search, Material material, short subTypeId) {
        this.name = name;
        this.material = material;
        this.subTypeId = subTypeId;
        this.search = search.clone();
    }

    public Material getType() {
        return material;
    }

    public short getSubTypeId() {
        return subTypeId;
    }

    public int getStackSize() {
        return material.getMaxStackSize();
    }

    public int getId() {
        return material.getId();
    }

	public String[][] getSearch() {
		return search;
	}

	public boolean isEdible() {
        return material.isEdible();
    }
    
    public boolean isBlock() {
        return material.isBlock();
    }
    
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.getId();
        hash = 17 * hash + this.subTypeId;
        return hash;
    }

    public boolean isDurable() {
        return (material.getMaxDurability() > 0);
    }

    public ItemStack toStack() {
        return new ItemStack(this.material, 1, subTypeId);
    }
    
    public ItemStack toStack(short dv) {
        return new ItemStack(this.material, 1, dv);
    }

    @Override
    public String toString() {
        return String.format("%s[%d:%d]", name, material.getId(), subTypeId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof PerkItem)) {
            return false;
        } else {
            return ((PerkItem) obj).material == this.material && ((PerkItem) obj).subTypeId == this.subTypeId;
        }
    }
}