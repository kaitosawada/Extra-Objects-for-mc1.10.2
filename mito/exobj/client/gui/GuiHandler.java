package com.mito.exobj.client.gui;

import com.mito.exobj.common.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Main.GUI_ID_BBSetter) {
			return new ContainerItemBlockSetter(player.inventory);
		} else if (ID == Main.GUI_ID_BBSelect) {
			return new ContainerItemSelectTool(player.inventory);
		} else if (ID == Main.GUI_ID_FakeBlock) {
			return new ContainerItemFakeBlock(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Main.GUI_ID_BBSetter) {
			return new GuiItemBlockSetter(player.inventory);
		} else if (ID == Main.GUI_ID_BBSelect) {
			return new GuiItemSelectTool();
		} else if (ID == Main.GUI_ID_FakeBlock) {
			return new GuiItemFakeBlock(player.inventory);
		}
		return null;
	}

}
