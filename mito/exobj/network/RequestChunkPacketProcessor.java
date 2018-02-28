package com.mito.exobj.network;

import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.BraceBase.ChunkAndWorldManager;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.common.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;

public class RequestChunkPacketProcessor implements IMessage, IMessageHandler<RequestChunkPacketProcessor, IMessage> {

	public int xChunkCoord;
	public int zChunkCoord;

	public RequestChunkPacketProcessor() {
	}

	public RequestChunkPacketProcessor(int i, int j) {
		this.xChunkCoord = i;
		this.zChunkCoord = j;
	}

	@Override
	public IMessage onMessage(RequestChunkPacketProcessor message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		World world = DimensionManager.getWorld(player.dimension);
		if (ChunkAndWorldManager.isChunkExist(world, message.xChunkCoord, message.zChunkCoord)) {
			Iterator iterator = ChunkAndWorldManager.getChunkDataNew(world, message.xChunkCoord,
					message.zChunkCoord).braceList.iterator();
			while (iterator.hasNext()) {
				ExtraObject base = (ExtraObject) iterator.next();
				PacketHandler.INSTANCE.sendTo(new AddPacketProcessor(base), player);
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.xChunkCoord = buf.readInt();
		this.zChunkCoord = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.xChunkCoord);
		buf.writeInt(this.zChunkCoord);
	}

}
