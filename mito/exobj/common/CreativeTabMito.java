package com.mito.exobj.common;

import com.mito.exobj.common.main.ResisterItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabMito extends CreativeTabs {

	public CreativeTabMito(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return ResisterItem.ItemBar;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return "Braces&Oscillators";
	}


}
