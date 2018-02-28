package com.mito.exobj.network;

import com.mito.exobj.BraceBase.BB_DataWorld;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.LoadClientWorldHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AdjustPacketProcessor implements IMessage, IMessageHandler<AdjustPacketProcessor, IMessage> {

	public int id;
	public Vec3d coord;

	public AdjustPacketProcessor() {
	}

	public AdjustPacketProcessor(int id, Vec3d v) {
		this.id = id;
		this.coord = v;
	}

	@Override
	public IMessage onMessage(AdjustPacketProcessor message, MessageContext ctx) {
		BB_DataWorld dataworld = LoadClientWorldHandler.INSTANCE.data;
		ExtraObject base4 = dataworld.getBraceBaseByID(message.id);
		if (base4 != null) {
			base4.setPos(message.coord);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		this.coord = new Vec3d(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.id);
		buf.writeDouble(this.coord.xCoord);
		buf.writeDouble(this.coord.yCoord);
		buf.writeDouble(this.coord.zCoord);
	}

}
