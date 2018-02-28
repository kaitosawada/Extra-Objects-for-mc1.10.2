package com.mito.exobj.common.entity;

import com.mito.exobj.BraceBase.BB_ResisteredList;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.common.MyLogger;
import com.mito.exobj.network.BB_ClickPacketProcessor;
import com.mito.exobj.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityWrapperBB extends Entity {

	public ExtraObject base;

	public EntityWrapperBB(World world) {
		super(world);
	}

	public EntityWrapperBB(World world, ExtraObject base) {
		this(world);
		this.base = base;
	}

	@Override
	protected void entityInit() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public String getCommandSenderName() {
		String s = null;
		if (this.base != null)
			s = BB_ResisteredList.getBraceBaseString(this.base);

		if (s == null) {
			s = "generic";
		}

		return I18n.translateToLocal("extra_object." + s + ".list");
	}

	public EntityWrapperBB wrap(ExtraObject base) {
		this.base = base;
		return this;
	}

	@Override
	public boolean hitByEntity(Entity player) {
		if (player instanceof EntityPlayer) {
			if (base.leftClick((EntityPlayer) player, ((EntityPlayer) player).getHeldItemMainhand())) {
				PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(EnumHand.OFF_HAND, base.BBID, Minecraft.getMinecraft().objectMouseOver.hitVec));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
		if (base.rightClick(player, Minecraft.getMinecraft().objectMouseOver.hitVec, player.getHeldItemMainhand())) {
			PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(hand, base.BBID, Minecraft.getMinecraft().objectMouseOver.hitVec));
			return true;
		}
		return false;
	}

}
