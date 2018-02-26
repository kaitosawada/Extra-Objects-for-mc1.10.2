package com.mito.exobj.common.item;

import com.mito.exobj.client.BB_Key;
import com.mito.exobj.utilities.MitoMath;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemWall extends ItemSet {


	public ItemWall() {
		super();
		//this.setTextureName("exobj:wall");
	}

	public int getColor(ItemStack itemstack) {
		return itemstack.getItemDamage();
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, RayTraceResult mop, BB_Key key, boolean p_77663_5_) {
		RayTraceResult mop1 = this.getMovingOPWithKey(itemstack, world, player, key, mop, 1.0);
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}
		if (!world.isRemote) {
			if (mop != null && (MyUtil.canClick(world, key, mop1))) {
				if (nbt.getBoolean("activated")) {
					Vec3d end = MitoMath.copyVec3(mop.hitVec);
					Vec3d set = new Vec3d(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
					this.onActiveClick(world, player, itemstack, mop1, set, end, nbt);
					this.nbtInit(nbt, itemstack);
				} else {
					nbt.setDouble("setX", mop.hitVec.xCoord);
					nbt.setDouble("setY", mop.hitVec.yCoord);
					nbt.setDouble("setZ", mop.hitVec.zCoord);
					nbt.setBoolean("activated", true);
					this.activate(world, player, itemstack, mop1, nbt, key);
				}
			}
		}
	}

}
