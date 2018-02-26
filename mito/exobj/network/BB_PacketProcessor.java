package com.mito.exobj.network;

import java.io.IOException;
import java.util.Iterator;

import com.mito.exobj.BraceBase.*;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.MyLogger;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class BB_PacketProcessor implements IMessage, IMessageHandler<BB_PacketProcessor, IMessage> {

	public enum Mode {
		REQUEST, REQUEST_CHUNK, ADD, INFORM, INFORM_COMP, DELETE, SUGGEST, SYNC, ADJUST;

		Mode() {
		}
	}

	public ExtraObject base;
	public int id;
	public int id2;
	public int location;
	public NBTTagCompound nbt;
	public int xChunkCoord;
	public int zChunkCoord;
	public Mode mode;
	public int DimensionID;
	public Vec3d coord;

	public BB_PacketProcessor() {
	}

	public BB_PacketProcessor(Mode mode, int id, Vec3d coord) {
		this.id = id;
		this.mode = mode;
		this.coord = coord;
	}

	// mode : ADD DELETE INFORM INFORM_COMP
	public BB_PacketProcessor(Mode mode, ExtraObject base) {
		this.base = base;
		this.id = base.BBID;
		this.mode = mode;
		this.xChunkCoord = MathHelper.floor_double(base.getPos().xCoord / 16.0D);
		this.zChunkCoord = MathHelper.floor_double(base.getPos().zCoord / 16.0D);
		this.DimensionID = base.worldObj.provider.getDimension();
	}

	// REQUEST_CHUNK
	public BB_PacketProcessor(Mode mode, int i, int j) {
		this.mode = Mode.REQUEST_CHUNK;
		this.xChunkCoord = i;
		this.zChunkCoord = j;
	}

	// REQUEST DELETE
	public BB_PacketProcessor(Mode mode, int id) {
		this.mode = mode;
		this.id = id;
	}

	@Override
	public IMessage onMessage(BB_PacketProcessor message, MessageContext ctx) {

		if (ctx.side == Side.CLIENT) {
			World world = Main.proxy.getClientWorld();
			BB_DataWorld dataworld = LoadClientWorldHandler.INSTANCE.data;
			switch (message.mode) {
				case REQUEST:
					break;
				case REQUEST_CHUNK:
					break;
				case ADD:
					ExtraObject base1 = message.base;
					if (base1 != null) {
						int i = MathHelper.floor_double(base1.getPos().xCoord / 16.0D);
						int j = MathHelper.floor_double(base1.getPos().zCoord / 16.0D);
						BB_DataChunk chunkData = BB_DataLists.getChunkDataNew(world, i, j);
						Iterator iterator = chunkData.braceList.iterator();
						boolean flag = true;
						while (iterator.hasNext()) {
							ExtraObject fobj = (ExtraObject) iterator.next();
							if (fobj.BBID == message.id) {
								flag = false;
								break;
							}
						}
						if (flag) {
							base1.addToWorld();
							int k1 = MathHelper.floor_double(base1.getPos().xCoord);
							int k2 = MathHelper.floor_double(base1.getPos().yCoord);
							int k3 = MathHelper.floor_double(base1.getPos().zCoord);
							// world.func_147451_t(k1, k2, k3);
							world.markBlockRangeForRenderUpdate(new BlockPos(k1, k2, k3), new BlockPos(k1, k2, k3));
						}
					}
					break;
				case INFORM:
					break;
				case INFORM_COMP:
					break;
				case DELETE:
					ExtraObject base = dataworld.getBraceBaseByID(message.id);
					if (base != null) {
						base.removeFromWorld();
					}
					break;
				// suggest隕√ｉ縺ｪ縺�縺九ｂ
				case SUGGEST:
					if (world.provider.getDimension() == message.DimensionID/*
						&& world.chunk(message.xChunkCoord, message.zChunkCoord);*/) {
						PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.REQUEST, message.id));
					}
					break;
				case SYNC:
					if (message.nbt != null) {
						message.base = BB_ResisteredList.syncBraceBaseFromNBT(message.nbt, world, message.id);
						if (message.base == null)
							MyLogger.info("brace sync null");
					} else {
						MyLogger.info("brace sync skipped");
					}
					dataworld.shouldUpdateRender = true;
					break;
				case ADJUST:
					ExtraObject base4 = dataworld.getBraceBaseByID(message.id);
					if (base4 != null) {
						base4.setPos(message.coord);
					}
					break;
				default:
					break;
			}
		} else {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			World world = DimensionManager.getWorld(player.dimension);
			switch (message.mode) {
				case REQUEST:
					if (BB_DataLists.getWorldData(world).BBIDMap.containsItem(message.id)) {
						PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD,
								(ExtraObject) BB_DataLists.getWorldData(world).BBIDMap.lookup(message.id)), player);
					}
					break;
				case REQUEST_CHUNK:
					if (BB_DataLists.isChunkExist(world, message.xChunkCoord, message.zChunkCoord)) {
						Iterator iterator = BB_DataLists.getChunkDataNew(world, message.xChunkCoord,
								message.zChunkCoord).braceList.iterator();
						while (iterator.hasNext()) {
							ExtraObject base = (ExtraObject) iterator.next();
							PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD, base), player);
						}
					}
					break;
				case ADD:
					break;
				case INFORM:
					break;
				case INFORM_COMP:
					break;
				case DELETE:
					break;
				case SUGGEST:
					break;
				case SYNC:
					if (message.nbt != null) {
						message.base = BB_ResisteredList.syncBraceBaseFromNBT(message.nbt, world, message.id);

						if (message.base == null)
							MyLogger.info("brace sync null");
					} else {
						MyLogger.info("brace sync skipped");
					}
					PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.ADD, message.base));
					break;
				case ADJUST:
					break;
				default:
					break;
			}
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = Mode.values()[(int) buf.readByte()];

		switch (this.mode) {
			case REQUEST:
				this.id = buf.readInt();
				break;
			case REQUEST_CHUNK:
				this.xChunkCoord = buf.readInt();
				this.zChunkCoord = buf.readInt();
				break;
			case ADD:
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
				break;
			case INFORM:
				break;
			case INFORM_COMP:
				break;
			case DELETE:
				this.id = buf.readInt();
				break;
			case SUGGEST:
				this.id = buf.readInt();
				this.DimensionID = buf.readInt();
				this.xChunkCoord = buf.readInt();
				this.zChunkCoord = buf.readInt();
				break;
			case SYNC:
				this.id = buf.readInt();
				try {
					PacketBuffer pb = new PacketBuffer(buf);
					this.nbt = pb.readNBTTagCompoundFromBuffer();
				} catch (IOException e) {
					MyLogger.info("brace sync error");
				}
				break;
			case ADJUST:
				this.id = buf.readInt();
				double x = buf.readDouble();
				double y = buf.readDouble();
				double z = buf.readDouble();
				this.coord = new Vec3d(x, y, z);
				break;
			default:
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.ordinal());
		switch (this.mode) {
			case REQUEST:
				buf.writeInt(this.id);
				break;
			case REQUEST_CHUNK:
				buf.writeInt(this.xChunkCoord);
				buf.writeInt(this.zChunkCoord);
				break;
			case ADD:
				buf.writeInt(this.base.BBID);
				PacketBuffer pb = new PacketBuffer(buf);
				NBTTagCompound nbt = this.base.getNBTTagCompound();
				Iterator iterator = nbt.getKeySet().iterator();
				pb.writeNBTTagCompoundToBuffer(nbt);
				break;
			case INFORM:
				buf.writeInt(this.id);
				break;
			case INFORM_COMP:
				buf.writeInt(this.id);
				break;
			case DELETE:
				buf.writeInt(this.id);
				break;
			case SUGGEST:
				buf.writeInt(this.id);
				buf.writeInt(this.DimensionID);
				buf.writeInt(this.xChunkCoord);
				buf.writeInt(this.zChunkCoord);
				break;
			case SYNC:
				buf.writeInt(this.base.BBID);
				PacketBuffer pb1 = new PacketBuffer(buf);
				NBTTagCompound nbt1 = new NBTTagCompound();
				this.base.writeToNBTOptional(nbt1);
				pb1.writeNBTTagCompoundToBuffer(nbt1);
				break;
			case ADJUST:
				buf.writeInt(this.id);
				buf.writeDouble(this.coord.xCoord);
				buf.writeDouble(this.coord.yCoord);
				buf.writeDouble(this.coord.zCoord);
				break;
			default:
				break;
		}
	}

}
