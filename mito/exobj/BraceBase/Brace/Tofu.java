package com.mito.exobj.BraceBase.Brace;

import java.util.List;

import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.item.ItemBar;
import com.mito.exobj.common.item.ItemBrace;
import com.mito.exobj.common.main.ResisterItem;
import com.mito.exobj.network.BB_PacketProcessor;
import com.mito.exobj.network.BB_PacketProcessor.Mode;
import com.mito.exobj.network.PacketHandler;
import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MitoMath;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Tofu extends ExtraObject {

	//pos > cpoint > end
	public AxisAlignedBB aabb = null;
	public int color = 0;
	public Block texture = Blocks.STONE;

	public Tofu(World world) {
		super(world);
	}

	public Tofu(World world, Vec3d pos) {
		super(world, pos);
	}

	public Tofu(World world, Vec3d pos, Vec3d end, Block material, int tex) {
		this(world, pos);
		double maxX = Math.max(pos.xCoord, end.xCoord) + 0.01;
		double maxY = Math.max(pos.yCoord, end.yCoord) + 0.01;
		double maxZ = Math.max(pos.zCoord, end.zCoord) + 0.01;
		double minX = Math.min(pos.xCoord, end.xCoord) - 0.01;
		double minY = Math.min(pos.yCoord, end.yCoord) - 0.01;
		double minZ = Math.min(pos.zCoord, end.zCoord) - 0.01;
		this.aabb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		this.texture = material;
		this.color = tex;
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void readExtraObjectFromNBT(NBTTagCompound nbt) {
		//this.line.readNBT(nbt);

		double maxX = nbt.getDouble("maxX");
		double maxY = nbt.getDouble("maxY");
		double maxZ = nbt.getDouble("maxZ");
		double minX = nbt.getDouble("minX");
		double minY = nbt.getDouble("minY");
		double minZ = nbt.getDouble("minZ");
		this.aabb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		this.texture = Block.getBlockById(nbt.getInteger("block"));
		this.color = nbt.getInteger("color");

	}

	private void setVec3d(NBTTagCompound nbt, String name, Vec3d vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3d getVec3d(NBTTagCompound nbt, String name) {
		return new Vec3d(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	public void writeExtraObjectToNBT(NBTTagCompound nbt) {
		nbt.setDouble("maxX", aabb.maxX);
		nbt.setDouble("maxY", aabb.maxY);
		nbt.setDouble("maxZ", aabb.maxZ);
		nbt.setDouble("minX", aabb.minX);
		nbt.setDouble("minY", aabb.minY);
		nbt.setDouble("minZ", aabb.minZ);
		nbt.setInteger("block", Block.getIdFromBlock(texture));
		nbt.setInteger("color", this.color);
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		return aabb.intersectsWith(boundingBox);
	}
/*
	@Override
	public Vec3d interactWithLine(Vec3d s, Vec3d e) {
		return aabb.calculateIntercept(s, e).hitVec;
	}*/

	@Override
	public Line interactWithRay(Vec3d set, Vec3d end) {
		RayTraceResult v = aabb.calculateIntercept(set, end);
		if (v != null && v.hitVec != null) {
			return new Line(v.hitVec, v.hitVec);
		}
		return null;
	}

	public void breakBrace(EntityPlayer player) {
		if (!player.worldObj.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				this.dropItem();
			}

			this.setDead();
			/*for (int n = 0; n < this.bindBraces.size(); n++) {
				this.bindBraces.get(n).setDead();
			}*/
		} else {
			//Main.proxy.playSound(new ResourceLocation(this.texture.stepSound.getBreakSound()), this.texture.stepSound.volume, this.texture.stepSound.frequency, (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);
			Main.proxy.particle(this);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void particle() {
		Vec3d center = new Vec3d((aabb.maxX + aabb.minX) / 2, (aabb.maxY + aabb.minY) / 2, (aabb.maxZ + aabb.minZ) / 2);
		int b0 = (int) ((aabb.maxX - aabb.minX) * 4) + 1;
		int b1 = (int) ((aabb.maxY - aabb.minY) * 4) + 1;
		int b2 = (int) ((aabb.maxZ - aabb.minZ) * 4) + 1;

		for (int i1 = 0; i1 < b0; ++i1) {
			for (int j1 = 0; j1 < b1; ++j1) {
				for (int k1 = 0; k1 < b2; ++k1) {
					double d0 = aabb.minX + ((double) i1) / (double) b0 * (aabb.maxX - aabb.minX);
					double d1 = aabb.minY + ((double) j1) / (double) b1 * (aabb.maxY - aabb.minY);
					double d2 = aabb.minZ + ((double) k1) / (double) b2 * (aabb.maxZ - aabb.minZ);
					Main.proxy.addDiggingEffect(worldObj, center, d0, d1, d2, this.texture, color);
				}
			}
		}
	}

	public boolean leftClick(EntityPlayer player, ItemStack itemstack) {
		if (player.capabilities.isCreativeMode) {
			this.breakBrace(player);
			return true;
		} else if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public boolean rightClick(EntityPlayer player, Vec3d pos, ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		} else if (itemstack != null && Block.getBlockFromItem(itemstack.getItem()) != Blocks.AIR) {
			this.texture = Block.getBlockFromItem(itemstack.getItem());
			this.color = itemstack.getItemDamage() % 16;
			dataworld.shouldUpdateRender = true;
			PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this));
		}
		return false;
	}

	public void dropItem() {

		float f = this.random.nextFloat() * 0.2F + 0.1F;
		float f1 = this.random.nextFloat() * 0.2F + 0.1F;
		float f2 = this.random.nextFloat() * 0.2F + 0.1F;

		ItemBrace brace = (ItemBrace) ResisterItem.ItemBrace;
		ItemStack itemstack1 = new ItemStack(ResisterItem.ItemBrace, 1, this.color);

		NBTTagCompound nbt = itemstack1.getTagCompound();
		itemstack1.setTagCompound(nbt);
		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
		nbt.setBoolean("useFlag", false);
		EntityItem entityitem = new EntityItem(worldObj, (double) ((float) this.pos.xCoord + f), (double) ((float) this.pos.yCoord + f1), (double) ((float) this.pos.zCoord + f2), itemstack1);

		float f3 = 0.05F;
		entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
		entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
		entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
		worldObj.spawnEntityInWorld(entityitem);
	}

	/*public int getBrightnessForRender(float partialtick) {
		int i = MathHelper.floor_double((this.pos.xCoord + this.end.xCoord) / 2);
		int k = MathHelper.floor_double((this.pos.yCoord + this.end.yCoord) / 2);
		int j = MathHelper.floor_double((this.pos.zCoord + this.end.zCoord) / 2);
	
		if (this.worldObj.blockExists(i, 0, j)) {
			return this.worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
		} else {
			return 0;
		}
	}*/

	/*@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float partialticks, double x, double y, double z) {
		Vec3d v = this.getPos();
		int i = MathHelper.floor_double(v.xCoord + x);
		int j = MathHelper.floor_double(v.yCoord + y);
		int k = MathHelper.floor_double(v.zCoord + z);

		for (int n1 = 0; n1 < 6; n1++) {
			if (!this.worldObj.getBlock(i + Facing.offsetsXForSide[n1], j + Facing.offsetsYForSide[n1], k + Facing.offsetsZForSide[n1]).isOpaqueCube()) {
				return this.worldObj.getLightBrightnessForSkyBlocks(i + Facing.offsetsXForSide[n1], j + Facing.offsetsYForSide[n1], k + Facing.offsetsZForSide[n1], 0);
			}
		}
		if (this.worldObj.blockExists(i, 0, k)) {
			return this.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
		} else {
			return 0;
		}
	}*/

	public AxisAlignedBB getBoundingBox() {
		return aabb;
	}

	/*public void setRoll(double roll) {
		this.rotationRoll = roll;
		this.prevRotationRoll = roll;
	}*/

	/*public BezierCurve getBezierCurve() {
		BezierCurve bc;
		Vec3d va = this.end;
		Vec3d vb = MitoMath.vectorPul(va, this.offCurvePoints2);
		Vec3d vc = MitoMath.vectorPul(this.pos, this.offCurvePoints1);
		Vec3d vd = this.pos;
		if (this.offCurvePoints1.lengthVector() < 0.001) {
			bc = new BezierCurve(vd, vb, va);
		} else if (this.offCurvePoints2.lengthVector() < 0.001) {
			bc = new BezierCurve(vd, vc, va);
		} else {
			bc = new BezierCurve(vd, vc, vb, va);
		}
		return bc;
	}*/

	public void addCoordinate(double x, double y, double z) {
		this.pos = this.pos.addVector(x, y, z);
		this.aabb.addCoord(x, y, z);
	}

	public double getMinY() {
		return aabb.minY;//Math.min(pos.yCoord, end.yCoord);
	}

	public double getMaxY() {
		return aabb.maxY;//Math.max(pos.yCoord, end.yCoord);
	}

	public Vec3d getPos() {
		return this.pos;
		//return line == null ? null : line.getPos();//MitoMath.vectorRatio(pos, end, 0.5);
	}

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity) {
		if (aabb.intersectsWith(this.aabb)) {
			collidingBoundingBoxes.add(this.aabb);
		}
	}

	public void rotation(Vec3d cent, double yaw) {/*
		Vec3d p = MitoMath.vectorSum(MitoMath.rotY(MitoMath.sub_vector(this.pos, cent), yaw), cent);
		end = MitoMath.vectorPul(MitoMath.rotY(MitoMath.vectorSub(this.end, cent), yaw), cent);
		offCurvePoints1 = MitoMath.rotY(offCurvePoints1, yaw);
		offCurvePoints2 = MitoMath.rotY(offCurvePoints2, yaw);
		if (line != null)
			line.rotation(cent, yaw);
		this.prevPos.xCoord = this.pos.xCoord = p.xCoord;
		this.prevPos.yCoord = this.pos.yCoord = p.yCoord;
		this.prevPos.zCoord = this.pos.zCoord = p.zCoord;*/
	}

	public void resize(Vec3d cent, double i) {/*
		Vec3d p = MitoMath.vectorSum(MitoMath.vectorMul(MitoMath.sub_vector(this.pos, cent), i), cent);
		//end = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(this.end, cent), i), cent);
		if (line != null)
			line.resize(cent, i);
		this.prevPos.xCoord = this.pos.xCoord = p.xCoord;
		this.prevPos.yCoord = this.pos.yCoord = p.yCoord;
		this.prevPos.zCoord = this.pos.zCoord = p.zCoord;*/
	}

	public void snap(RayTraceResult mop, boolean b) {
		//this.line.snap(mop, b);
	}

}
