package com.mito.exobj.network;

import com.mito.exobj.BraceBase.ChunkAndWorldManager;
import com.mito.exobj.BraceBase.BB_DataWorld;
import com.mito.exobj.BraceBase.ExtraObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BB_ClickPacketProcessor implements IMessage, IMessageHandler<BB_ClickPacketProcessor, IMessage> {

	public int id;
	public Vec3d pos;
	public EnumHand mode;

	public BB_ClickPacketProcessor(){}

	public BB_ClickPacketProcessor(EnumHand mode, int id, Vec3d pos) {
		this.mode = mode;
		this.id = id;
		this.pos = pos;
	}

	@Override
	public IMessage onMessage(BB_ClickPacketProcessor message, MessageContext ctx) {

		try {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			World world = DimensionManager.getWorld(player.dimension);
			BB_DataWorld data = ChunkAndWorldManager.getWorldData(world);
			ExtraObject base = data.getBraceBaseByID(message.id);
			if (base != null && player != null) {
				switch (message.mode) {
					case MAIN_HAND:
						base.rightClick(player, message.pos, player.getHeldItemMainhand());
						break;
					case OFF_HAND:
						base.leftClick(player, player.getHeldItemMainhand());
						break;
					default:
						break;
				}
			}
		} finally {
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = EnumHand.values()[buf.readByte()];
		this.id = buf.readInt();
		this.pos = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.ordinal());
		buf.writeInt(this.id);
		buf.writeFloat((float) this.pos.xCoord);
		buf.writeFloat((float) this.pos.yCoord);
		buf.writeFloat((float) this.pos.zCoord);
	}

}
