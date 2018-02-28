package com.mito.exobj.network;

import com.mito.exobj.BraceBase.*;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.render.exorender.BezierCurve;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.MyLogger;
import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MitoMath;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class SyncPacketProcessor implements IMessage, IMessageHandler<SyncPacketProcessor, IMessage> {

	public ExtraObject base;
	public int id;
	public NBTTagCompound nbt;

	public SyncPacketProcessor() {
	}

	public SyncPacketProcessor(ExtraObject eo) {
		base = eo;
	}

	@Override
	public IMessage onMessage(SyncPacketProcessor message, MessageContext ctx) {
		World world = Main.proxy.getClientWorld();
		BB_DataWorld dataworld = LoadClientWorldHandler.INSTANCE.data;
		if (message.nbt != null) {
			message.base = BB_ResisteredList.syncBraceBaseFromNBT(message.nbt, world, message.id);
			if (message.base == null)
				MyLogger.info("brace sync null");
		} else {
			MyLogger.info("brace sync skipped");
		}
		dataworld.shouldUpdateRender = true;
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		try {
			PacketBuffer pb = new PacketBuffer(buf);
			this.nbt = pb.readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			MyLogger.info("brace sync error");
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.base.BBID);
		PacketBuffer pb1 = new PacketBuffer(buf);
		NBTTagCompound nbt1 = new NBTTagCompound();
		this.base.writeToNBTOptional(nbt1);
		pb1.writeNBTTagCompoundToBuffer(nbt1);
	}

}
