package com.mito.exobj.common.item;

import com.mito.exobj.BraceBase.Brace.Tofu;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.client.render.RenderHighLight;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.entity.EntityWrapperBB;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTofu extends ItemSet {

	public byte key = 0;

	public ItemTofu() {
		super();
		this.setMaxDamage(0);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		NBTTagCompound nbt = getNBT(itemstack);
		return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".list")).trim();
	}

	public Block getMaterial(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			if (itemstack.getTagCompound().hasKey("material")) {
				Block b = Block.getBlockById(itemstack.getTagCompound().getInteger("material"));
				return b;

			}
		}
		return Blocks.STONE;

	}

	public ItemStack setMaterial(ItemStack itemstack, Block e) {
		NBTTagCompound nbt = getNBT(itemstack);
		nbt.setInteger("material", Block.getIdFromBlock(e));
		return itemstack;
	}

	public int getColor(ItemStack itemstack) {
		return itemstack.getItemDamage() & (16 - 1);
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
		super.nbtInit(nbt, itemstack);
		nbt.setInteger("brace", -1);
	}

	public void snapDegree(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key){}

	public boolean activate(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult mop,
							NBTTagCompound nbt, BB_Key key) {
		if (mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null
				&& mop.entityHit instanceof EntityWrapperBB) {
			nbt.setInteger("brace", ((EntityWrapperBB) mop.entityHit).base.BBID);
		}
		return true;
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult movingOP,
							  Vec3d set, Vec3d end, NBTTagCompound nbt) {
		int color = this.getColor(itemstack);
		Tofu tofu = new Tofu(world, set, end, this.getMaterial(itemstack), color);
		tofu.addToWorld();

		if (!player.capabilities.isCreativeMode) {
			itemstack.stackSize--;
			if (itemstack.stackSize == 0) {
				//player.destroyCurrentEquippedItem();
			}
		}
	}

	@Override
	public void clientProcess(RayTraceResult mop, ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.getBoolean("activated")) {
			Vec3d pos = mop.hitVec;
			Block texture = getMaterial(itemstack);
			/*Main.proxy.playSound(new ResourceLocation(texture.stepSound.getBreakSound()), texture.stepSound.volume,
					texture.stepSound.frequency, (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);*/
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemStack, EntityPlayer player, float partialticks,
									RayTraceResult mop) {
		if (Minecraft.getMinecraft().currentScreen == null) {
			if (mop == null)
				return false;
			Vec3d set = mop.hitVec;
			RenderHighLight rh = RenderHighLight.INSTANCE;
			NBTTagCompound nbt = getNBT(itemStack);
			if (nbt.getBoolean("activated") && MyUtil.canClick(player.worldObj, Main.proxy.getKey(), mop)) {
				Vec3d end = new Vec3d(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
				rh.drawBox(player, set, end, partialticks);
				return true;
			} else {
				return this.drawHighLightBrace(player, partialticks, mop);
			}
		}
		return false;
	}

}
