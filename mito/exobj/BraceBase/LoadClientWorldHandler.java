package com.mito.exobj.BraceBase;

import java.util.Iterator;

import com.mito.exobj.common.Main;
import com.mito.exobj.network.BB_PacketProcessor;
import com.mito.exobj.network.BB_PacketProcessor.Mode;
import com.mito.exobj.network.PacketHandler;

import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LoadClientWorldHandler {

	public BB_DataWorld data;
	public static LoadClientWorldHandler INSTANCE = new LoadClientWorldHandler();

	public LoadClientWorldHandler() {
	}

	public void onUnloadWorld(WorldEvent.Unload e) {
		this.data = null;
		Main.proxy.sg.init();
	}

	public void onLoadWorld(WorldEvent.Load e) {
		this.data = new BB_DataWorld(e.getWorld());
	}

	public void onUpdate(TickEvent.PlayerTickEvent e) {
		if (this.data != null)
			this.data.onUpDate();
	}

	public void onChunkLoad(ChunkEvent.Load e) {
		PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.REQUEST_CHUNK, e.getChunk().xPosition, e.getChunk().zPosition));
	}

	public void onChunkUnload(ChunkEvent.Unload e) {
		BB_DataChunk chunkData = BB_DataLists.getChunkDataNew(e.getChunk());
		Iterator iterator = chunkData.braceList.iterator();
		while (iterator.hasNext()) {
			ExtraObject fobj = (ExtraObject) iterator.next();
			fobj.datachunk = null;
			fobj.removeFromWorld();
		}
		data.removeDataChunk(chunkData);
	}

}
