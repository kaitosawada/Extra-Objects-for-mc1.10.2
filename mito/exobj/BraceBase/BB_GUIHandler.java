package com.mito.exobj.BraceBase;

import net.minecraft.inventory.ContainerPlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BB_GUIHandler {

	public static boolean flag = false;
	public static boolean flag2 = false;

	public static void openEvent(PlayerContainerEvent e) {
		if (flag) {
			if (e.getEntityPlayer().inventoryContainer instanceof ContainerPlayer) {
				if (flag2 == true) {
					flag = false;
					flag2 = false;
				}
			} else {
				flag2 = true;
			}
			e.setResult(Event.Result.ALLOW);
		}
	}

}
