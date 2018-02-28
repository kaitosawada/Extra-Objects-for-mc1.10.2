package com.mito.exobj.common.item;

import com.mito.exobj.common.MyLogger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
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

import javax.annotation.Nullable;

/*
①onItemUseとonItemRightClickをまとめる
②標準パラメタ設定
*/

public abstract class ItemBraceBase extends Item implements ISnap {

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

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, RayTraceResult mop, BB_Key key) {}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
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

}
