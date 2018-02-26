package com.mito.exobj.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import com.mito.exobj.client.render.BB_Render;
import com.mito.exobj.BraceBase.BB_ResisteredList;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.entity.EntityWrapperBB;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class ItemBraceBase extends Item {

	public ItemBraceBase() {
		super();
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		this.RightClick(stack, worldIn, playerIn, Minecraft.getMinecraft().objectMouseOver, Main.proxy.getKey());
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		this.RightClick(itemStackIn, worldIn, playerIn, Minecraft.getMinecraft().objectMouseOver, Main.proxy.getKey());
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	/*@Nullable
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		MyLogger.info("onItemUseFinish: ");
		return stack;
	}*/

	public boolean isDamageable() {
		return false;
	}

	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}
/*
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

		NBTTagCompound nbt = stack.getTagCompound();
		if (worldIn.isRemote) {
			//PacketHandler.INSTANCE.sendToServer(new ItemUsePacketProcessor(Main.proxy.getKey(), ((EntityPlayer)entityLiving)));
		}
		entityLiving.resetActiveHand();
	}*/

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, RayTraceResult mop, BB_Key key) {

	}

	public RayTraceResult getMovingOPWithKey(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, RayTraceResult mop, double partialticks) {
		NBTTagCompound nbt = this.getNBT(itemstack);

		if (mop != null && MyUtil.canClick(world, key, mop)) {
			if (!key.isControlPressed()) {
				mop = this.snap(mop, itemstack, world, player, key, nbt);
			}
			if (key.isShiftPressed()) {
				this.snapDegree(mop, itemstack, world, player, key, nbt);
			}
		}

		return mop;
	}

	public double getRayDistance(BB_Key key) {
		return 5.0;
	}

	public RayTraceResult snap(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
		if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
			this.snapBlock(mop, itemstack, world, player, key);
		} else if (mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
			this.snapBraceBase(mop, itemstack, world, player, key);
		}
		return mop;
	}

	public void snapBlock(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key) {
		MyUtil.snapBlock(mop);
	}

	public void snapBraceBase(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key) {
		//各BraceBaseに振り分け用関数を用意
		if (((EntityWrapperBB) mop.entityHit).base instanceof Brace) {
			Brace brace = (Brace) ((EntityWrapperBB) mop.entityHit).base;
			brace.snap(mop, this.snapCenter());
		}
	}

	public void snapDegree(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
	}

	public boolean snapCenter() {
		return true;
	}

	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, float partialTick, RayTraceResult mop) {
		return false;
	}

	public NBTTagCompound getNBT(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}

		return nbt;
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
	}

	public boolean drawHighLightBrace(EntityPlayer player, float partialticks, RayTraceResult mop) {
		if (mop != null) {
			if (MyUtil.isBrace(mop)) {
				ExtraObject base = ((EntityWrapperBB) mop.entityHit).base;
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPushMatrix();
				GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
						-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
						-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
				//BB_Render render = BB_ResisteredList.getBraceBaseRender(brace);
				BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
				render.drawHighLight(base, partialticks);
				GL11.glPopMatrix();
				return true;
			}
		}
		return false;
	}

	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		return false;
	}

	public NBTTagCompound getTagCompound(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		return nbt;
	}

}
