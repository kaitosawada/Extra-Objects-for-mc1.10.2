package com.mito.exobj.BraceBase.Brace;

import com.mito.exobj.BraceBase.ExtraObject;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Wall extends ExtraObject {

	public Wall(World world) {
		super(world);
	}

	public Wall(World world, Vec3d pos) {
		super(world, pos);
	}

	@Override
	public void readExtraObjectFromNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void writeExtraObjectToNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
