package com.mito.exobj.network;

import com.mito.exobj.BraceBase.*;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.MyLogger;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.Iterator;

public class AddPacketProcessor implements IMessage, IMessageHandler<AddPacketProcessor, IMessage> {

	public ExtraObject base;
	public int id;
	public NBTTagCompound nbt;

	public AddPacketProcessor() {
	}

	public AddPacketProcessor(ExtraObject base) {
		this.base = base;
	}

	@Override
	public IMessage onMessage(AddPacketProcessor message, MessageContext ctx) {
		World world = Main.proxy.getClientWorld();
		ExtraObject base1 = message.base;
		if (base1 != null) {
			if (!ChunkAndWorldManager.getWorldData(world).BBIDMap.containsItem(message.id)) {
				base1.addToWorld();
			}
		}
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
		if (this.nbt != null) {
			this.base = BB_ResisteredList.createExObjFromNBT(nbt, Main.proxy.getClientWorld(), this.id);
			if (this.base == null)
				MyLogger.info("brace sync null");
		} else {
			MyLogger.info("brace sync skipped");
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.base.BBID);
		new PacketBuffer(buf).writeNBTTagCompoundToBuffer(this.base.getNBTTagCompound());
	}

}
