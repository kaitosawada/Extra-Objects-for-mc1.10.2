package com.mito.exobj.BraceBase.Brace;

import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.client.render.model.IDrawable;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.MyLogger;
import com.mito.exobj.common.item.ItemBar;
import com.mito.exobj.common.item.ItemBraceBase;
import com.mito.exobj.network.BB_PacketProcessor;
import com.mito.exobj.network.BB_PacketProcessor.Mode;
import com.mito.exobj.network.PacketHandler;
import com.mito.exobj.utilities.Line;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelObject extends ExtraObject {

	public int color = 0;
	public Block texture = Blocks.STONE;
	protected IDrawable model = null;

	public ModelObject(World world) {
		super(world);
	}

	public ModelObject(World world, Vec3d
			pos) {
		super(world, pos);
	}

	public ModelObject(World world, Vec3d pos, Block material, int tex) {
		this(world, pos);
		this.color = tex;
		BlockRendererDispatcher blockrender = Minecraft.getMinecraft().getBlockRendererDispatcher();
	}


	@Override
	public void readExtraObjectFromNBT(NBTTagCompound nbt) {
		this.texture = Block.getBlockById(nbt.getInteger("block"));
		this.color = nbt.getInteger("color");
		BlockRendererDispatcher blockrender = Minecraft.getMinecraft().getBlockRendererDispatcher();

	}

	private void setVec3(NBTTagCompound nbt, String name, Vec3d vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3d getVec3(NBTTagCompound nbt, String name) {
		return new Vec3d(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	public void writeExtraObjectToNBT(NBTTagCompound nbt) {
		nbt.setInteger("block", Block.getIdFromBlock(texture));
		nbt.setInteger("color", this.color);
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		return false;
	}

	@Override
	public Line interactWithRay(Vec3d
										set, Vec3d
										end) {
		return null;
	}

	public void breakBrace(EntityPlayer player) {
		if (!player.worldObj.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				this.dropItem();
			}
			this.setDead();
		} else {
			/*Main.proxy.playSound(new ResourceLocation(this.texture.stepSound.getBreakSound()), this.texture.stepSound.volume, this.texture.stepSound.getPitch(), (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);
			Main.proxy.particle(this);*/
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void particle() {

	}

	@Override
	public void updateRenderer() {
		if (datachunk != null)
			this.datachunk.updateRenderer();
		updateModel();

	}

	public boolean leftClick(EntityPlayer player, ItemStack itemstack) {
		//MyLogger.info("left clicked");
		if (player.capabilities.isCreativeMode) {
			if (itemstack == null || itemstack.getItem() instanceof ItemBraceBase) {
				this.breakBrace(player);
			}
			return true;
		} else if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public boolean rightClick(EntityPlayer player, Vec3d pos, ItemStack itemstack) {
		//MyLogger.info("right clicked");
		if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		} else if (Main.proxy.isShiftKeyDown() && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) != Blocks.AIR) {
			MyLogger.info(Block.getBlockFromItem(itemstack.getItem()).toString());
			this.texture = Block.getBlockFromItem(itemstack.getItem());
			this.color = itemstack.getItemDamage() % 16;
			this.updateRenderer();
			PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this));
			return true;
		}
		return false;
	}

	public void dropItem() {
	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	public void addCoordinate(double x, double y, double z) {
		this.pos = this.pos.addVector(x, y, z);
	}

	public Vec3d
	getPos() {
		return this.pos;
	}

	@Override
	public IDrawable getModel() {
		if (model == null) {
			updateModel();
		}
		return model;
	}

	public void updateModel() {
	}

}
