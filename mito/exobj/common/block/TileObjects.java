package com.mito.exobj.common.block;

import com.mito.exobj.BraceBase.*;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileObjects extends TileEntity {
	public List<ExtraObject> list = null;
	@SideOnly(Side.CLIENT)
	public VBOList buffer = new VBOList();
	public boolean shouldUpdateRender = true;

	public TileObjects() {

	}

	public void updateEntity() {
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList taglist = nbt.getTagList("BB_Groups", 10);
		/*for (int i1 = 0; i1 < taglist.tagCount(); ++i1) {
			
		}*/
		NBTTagCompound nbt1 = taglist == null ? null : taglist.getCompoundTagAt(0);
		//list = (BB_GroupBase) BB_ResisteredList.createExObjFromNBT(nbt1, worldObj);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList taglist = new NBTTagList();
		NBTTagCompound nbt1 = new NBTTagCompound();
		if (list != null) {
			//list.writeToNBTOptional(nbt1);
			taglist.appendTag(nbt1);
			nbt.setTag("BB_Groups", taglist);
		}
		return nbt1;
	}
/*
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.pos, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}*/

	public void breakBrace() {
		if (list != null) {
			Vec3d v = new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getX());
			for (ExtraObject base : list) {
				ExtraObject eo1 = base.copy();
				eo1.worldObj = this.worldObj;
				eo1.dataworld = ChunkAndWorldManager.getWorldData(worldObj);
				eo1.addCoordinate(v);
				eo1.addToWorld();
			}
		}
	}
}
