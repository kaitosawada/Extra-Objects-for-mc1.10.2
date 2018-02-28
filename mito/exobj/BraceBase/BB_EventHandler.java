package com.mito.exobj.BraceBase;

import com.mito.exobj.common.MyLogger;

import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class BB_EventHandler {

	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onLoadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onLoadWorld(e);
		}
	}

	@SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onUnloadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onUnloadWorld(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataSave(ChunkDataEvent.Save e) {
		if (e.getWorld().isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataSave(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataLoad(ChunkDataEvent.Load e) {
		if (e.getWorld().isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataLoad(e);
		}

	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ServerTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.PlayerTickEvent e) {
		if (e.phase == Phase.END) {
			if (e.player.worldObj.isRemote)
				LoadClientWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onWorldTickEvent(TickEvent.WorldTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onWorldTickEvent(e);
		}
		if (e.side == Side.CLIENT)
			MyLogger.info("" + e.getPhase());
	}

	// 驥崎､�縺ｫ縺､縺�縺ｦ縺ｯ譛ｪ蜃ｦ逅�  unload -> save

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkLoad(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkLoad(e);
		}
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload e) {
		if (e.getWorld().isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkUnload(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkUnload(e);
		}
	}

}
