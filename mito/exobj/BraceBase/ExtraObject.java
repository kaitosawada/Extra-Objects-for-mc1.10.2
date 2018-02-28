package com.mito.exobj.BraceBase;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mito.exobj.client.render.model.IDrawable;
import com.mito.exobj.common.MyLogger;
import com.mito.exobj.network.PacketHandler;
import com.mito.exobj.network.SuggestPacketProcessor;
import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ExtraObject {

	public static int nextID;

	public boolean isDead = false;
	protected Vec3d pos;
	//public Vec3d motion;
	public World worldObj;
	public UUID uuid;
	//public int fire;
	public int BBID;
	private NBTTagCompound customFixedObjectData;

	public BB_DataWorld dataworld;
	public BB_DataChunk datachunk;

	public Random random = new Random();

	public ExtraObject(World world, Vec3d pos) {
		this(world);
		this.pos = pos;
	}

	public ExtraObject(World world) {
		this.worldObj = world;
		this.BBID = nextID++;
		this.dataworld = ChunkAndWorldManager.getWorldData(worldObj);
		if (dataworld == null) {
			MyLogger.warn("bracebase data world is null");
		}
		this.uuid = UUID.randomUUID();
	}

	public boolean addToWorld() {
		if (worldObj == null) {
			return false;
		}
		if (worldObj.isRemote) {
			return dataworld.addBraceBase(this, true);
		} else {
			boolean ret;
			if (pos != null) {
				ret = dataworld.addBraceBase(this, true);
			} else {
				ret = dataworld.addBraceBase(this, false);
			}
			if (ret) {
				PacketHandler.INSTANCE.sendToAll(new SuggestPacketProcessor(this));
			}
			return ret;
		}
	}

	public boolean removeFromWorld() {
		if (this.datachunk != null) {
			if (datachunk.braceList.isEmpty())
				dataworld.removeDataChunk(datachunk);
		}
		return dataworld.removeBrace(this);
	}

	public void changeId(int id) {
		if (id != this.BBID) {
			this.dataworld.BBIDMap.removeObject(this.BBID);
			this.BBID = id;
			this.dataworld.BBIDMap.addKey(this.BBID, this);
		} else {
			return;
		}
	}

	public void setDead() {
		this.isDead = true;
	}

	public void onUpdate() {
	}

	public NBTTagCompound getNBTTagCompound() {
		if (customFixedObjectData == null) {
			customFixedObjectData = new NBTTagCompound();
			this.writeToNBTOptional(customFixedObjectData);
		}
		return customFixedObjectData;
	}

	protected NBTTagList newDoubleNBTList(double... p_70087_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		double[] adouble = p_70087_1_;
		int i = p_70087_1_.length;

		for (int j = 0; j < i; ++j) {
			double d1 = adouble[j];
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}

		return nbttaglist;
	}

	protected NBTTagList newFloatNBTList(float... p_70049_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		float[] afloat = p_70049_1_;
		int i = p_70049_1_.length;

		for (int j = 0; j < i; ++j) {
			float f1 = afloat[j];
			nbttaglist.appendTag(new NBTTagFloat(f1));
		}

		return nbttaglist;
	}

	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		//MyLogger.info("write opt brace id " + this.BBID);
		String s = BB_ResisteredList.getBraceBaseString(this);

		if (!this.isDead && s != null) {
			p_70039_1_.setString("id", s);
			this.writeToNBT(p_70039_1_);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		try {
			nbt.setTag("Pos", this.newDoubleNBTList(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord));
			//nbt.setShort("Fire", (short) this.fire);
			if (this.uuid != null) {
				nbt.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
				nbt.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
			}

			this.writeExtraObjectToNBT(nbt);

		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving extra object NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Extra object being saved");
			//this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	private UUID getUniqueID() {
		return this.uuid;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList nbttaglist = nbt.getTagList("Pos", 6);
		NBTTagList nbttaglist2 = nbt.getTagList("Rotation", 6);

		this.pos = new Vec3d(0, 0, 0);
		double x1 = nbttaglist.getDoubleAt(0);
		double y1 = nbttaglist.getDoubleAt(1);
		double z1 = nbttaglist.getDoubleAt(2);
		this.pos = new Vec3d(x1, y1, z1);

		//this.fire = nbt.getShort("Fire");

		if (nbt.hasKey("UUIDMost", 4) && nbt.hasKey("UUIDLeast", 4)) {
			this.uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
		}

		this.setPosition(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord);

		if (nbt.hasKey("PersistentIDMSB") && nbt.hasKey("PersistentIDLSB")) {
			this.uuid = new UUID(nbt.getLong("PersistentIDMSB"), nbt.getLong("PersistentIDLSB"));
		}
		this.readExtraObjectFromNBT(nbt);

		/*if (this.shouldSetPosAfterLoading()) {
			this.setPosition(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord);
		}*/
	}

	private void setPosition(double xCoord, double yCoord, double zCoord) {
		this.pos = new Vec3d(xCoord, yCoord, zCoord);

	}

	/*private boolean shouldSetPosAfterLoading() {
		return true;
	}*/

	/**
	 * (abstract) Protected helper method to read subclass braceBase data from NBT.
	 */
	public abstract void readExtraObjectFromNBT(NBTTagCompound nbt);

	/**
	 * (abstract) Protected helper method to write subclass braceBase data to NBT.
	 */
	public abstract void writeExtraObjectToNBT(NBTTagCompound nbt);

	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		boolean ret = false;
		if (boundingBox.isVecInside(pos)) {
			ret = true;
		}
		return ret;
	}

	/*public BB_ObjectsBinder getBinder() {
		if (this.braces == null) {
			this.braces = new BB_ObjectsBinder(this);
		}
		return this.braces;
	}*/

	//RayTrace
	public Line interactWithRay(Vec3d set, Vec3d end) {
		return null;
	}

	public boolean rightClick(EntityPlayer player, Vec3d pos, ItemStack itemstack) {
		if (player.capabilities.isCreativeMode) {
		}
		return false;
	}

	public void updateRenderer() {
		if (datachunk != null)
			this.datachunk.updateRenderer();
	}

	public boolean leftClick(EntityPlayer player, ItemStack itemStack) {
		if (player.capabilities.isCreativeMode) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public AxisAlignedBB getBoundingBox() {
		return MyUtil.createAabbBySize(pos, 1.0);
	}

	public void addCoordinate(Vec3d v) {
		this.addCoordinate(v.xCoord, v.yCoord, v.zCoord);
	}

	public void addCoordinate(double x, double y, double z) {
		this.pos = this.pos.addVector(x, y, z);
	}

	public Vec3d getPos() {
		return pos;
	}

	public void breakBrace(EntityPlayer player) {
		this.setDead();
	}

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity) {

	}

	public void rotation(Vec3d cent, double yaw) {
	}

	public void resize(Vec3d c, double d) {
	}

	public ExtraObject copy() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBTOptional(nbt);
		return BB_ResisteredList.createExObjFromNBT(nbt, null);
	}

	@SideOnly(Side.CLIENT)
	public void particle() {

	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float i, double x, double y, double z) {

		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(MathHelper.floor_double(x), 0, MathHelper.floor_double(z));

		if (this.worldObj.isBlockLoaded(blockPos))
		{
			blockPos.setY(MathHelper.floor_double(y));
			return this.worldObj.getCombinedLight(blockPos, 0);
		}
		else
		{
			return 0;
		}
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float i) {
		return getBrightnessForRender(i, pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public boolean isBind(ExtraObject brace) {
		return true;
	}

	public IDrawable getModel() {
		return null;
	}

	public void setPos(Vec3d coord) {
		this.pos = coord;
	}

}
