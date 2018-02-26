package com.mito.exobj.common.item;

import java.util.List;

import com.mito.exobj.BraceBase.BB_DataLists;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.client.render.RenderHighLight;
import com.mito.exobj.client.render.exorender.BB_TypeResister;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.entity.EntityWrapperBB;
import com.mito.exobj.common.main.ResisterItem;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBrace extends ItemSet {

	public byte key = 0;
	public static int colorMax = 16, sizeMax = 100;


	public ItemBrace() {
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		int isize = nbt.getInteger("size");
		return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".name") + " x" + isize).trim();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean b) {
		super.addInformation(itemstack, player, list, b);
		NBTTagCompound nbt = getTagCompound(itemstack);
		list.add("size : " + nbt.getInteger("size"));
		list.add("type : " + this.getType(itemstack));
		list.add("texture : " + this.getMaterial(itemstack).getLocalizedName());
		if (!this.getJoint(itemstack).equals(""))
			list.add("Joint : " + this.getJoint(itemstack));

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i1 = 0; i1 < BB_TypeResister.shapeList.size(); ++i1) {
			ItemStack itemstack = new ItemStack(item, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.setSize(itemstack, 5);
			this.setType(itemstack, BB_TypeResister.shapeList.get(i1));
			this.setMaterial(itemstack, Blocks.STONE);
			this.setJoint(itemstack, "");
			nbt.setInteger("block", Block.getIdFromBlock(Blocks.WOOL));
			list.add(itemstack);
		}

		for (int i1 = 0; i1 < BB_TypeResister.shapeList.size(); ++i1) {
			ItemStack itemstack = new ItemStack(item, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.setSize(itemstack, 5);
			this.setType(itemstack, BB_TypeResister.shapeList.get(i1));
			this.setMaterial(itemstack, Blocks.IRON_BLOCK);
			nbt.setInteger("block", Block.getIdFromBlock(Blocks.WOOL));
			list.add(itemstack);
		}

		/*for (int i1 = 0; i1 < BB_TypeResister.patternList.size(); ++i1) {
			ItemStack itemstack = new ItemStack(item, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.setSize(itemstack, 5);
			this.setType(itemstack, BB_TypeResister.patternList.get(i1));
			nbt.setInteger("material", 0);
			list.add(itemstack);
		}*/
	}

	public ItemStack setJoint(ItemStack itemstack, String name) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setString("joint", name);
		return itemstack;
	}

	public double getRealSize(ItemStack itemstack) {

		return convToDoubleSize(getSize(itemstack));
	}

	public double convToDoubleSize(int isize) {

		return (double) isize * 0.05;
	}

	public int getSize(ItemStack itemstack) {
		int ret = 1;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("size")) {
			ret = itemstack.getTagCompound().getInteger("size");
		}
		return ret;
	}

	public String getType(ItemStack itemstack) {
		String ret = "square";
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("stype")) {
			ret = itemstack.getTagCompound().getString("stype");
		}
		return ret;
	}

	public Block getMaterial(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			if (itemstack.getTagCompound().hasKey("material")) {
				Block b = Block.getBlockById(itemstack.getTagCompound().getInteger("material"));
				return b;
			}
		}
		return Blocks.WOOL;

	}

	public String getJoint(ItemStack itemstack) {
		String ret = "";
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("joint")) {
			ret = itemstack.getTagCompound().getString("joint");
		}
		return ret;
	}

	public ItemStack setSize(ItemStack itemstack, int i) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setInteger("size", i);
		return itemstack;
	}

	public ItemStack setType(ItemStack itemstack, String i) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setString("stype", i);
		return itemstack;
	}

	public ItemStack setMaterial(ItemStack itemstack, Block e) {
		NBTTagCompound nbt = getTagCompound(itemstack);
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

	public double getRayDistance(BB_Key key) {
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	public void snapDegree(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
		if (nbt.getBoolean("activated")) {
			Vec3d set = new Vec3d(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			//MyUtil.snapByShiftKey(mop, set);
		}
	}

	public boolean activate(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult mop, NBTTagCompound nbt, BB_Key key) {
		if (mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
			nbt.setInteger("brace", ((EntityWrapperBB) mop.entityHit).base.BBID);
		}
		return true;
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, RayTraceResult movingOP, Vec3d set, Vec3d end, NBTTagCompound nbt) {
		int color = this.getColor(itemstack);
		Brace brace = new Brace(world, set, end, this.getType(itemstack), getJoint(itemstack), this.getMaterial(itemstack), color, this.getRealSize(itemstack));
		brace.addToWorld();
		BB_DataLists.getWorldData(world).getBraceBaseByID(nbt.getInteger("brace"));
		if (!player.capabilities.isCreativeMode) {
			itemstack.stackSize--;
			if (itemstack.stackSize == 0) {
				//
			}
		}
	}

	@Override
	public void clientProcess(RayTraceResult mop, ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.getBoolean("activated")) {
			Vec3d pos = mop.hitVec;
			Block texture = getMaterial(itemstack);
			//Main.proxy.playSound(new ResourceLocation(texture.getSoundType().getBreakSound(), texture.getSoundType().volume, texture.getSoundType().getPitch(), (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, float partialTicks, RayTraceResult mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		double size = this.getRealSize(itemstack);
		if (mop == null || !MyUtil.canClick(player.worldObj, Main.proxy.getKey(), mop))
			return false;
		Vec3d set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (nbt.getBoolean("activated")) {
			Vec3d end = new Vec3d(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawFakeBrace(player, set, end, size, partialTicks);
		} else {
			ExtraObject base = null;
			if (mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
				base = ((EntityWrapperBB) mop.entityHit).base;
			}
			if (base != null && base instanceof Brace && size < ((Brace) base).size) {
				rh.drawCenter(player, set, ((Brace) base).size / 2 + 0.1, partialTicks);
			} else {
				rh.drawBox(player, set, size, partialTicks);
			}
		}
		return true;
	}

	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			ItemBrace brace = (ItemBrace) ResisterItem.ItemBrace;
			int w = dwheel / 120;
			int size = brace.getSize(stack) + w;
			if (size > sizeMax) {
				size = sizeMax;
			} else if (size < 1) {
				size = 1;
			}
			brace.setSize(stack, size);
			return true;
		}/* else if (key.isControlPressed()) {
			ItemBrace brace = (ItemBrace) ResisterItem.ItemBrace;
			int w = dwheel / 120;
			int size = BB_TypeResister.typeList.indexOf(brace.getType(stack)) + w;
			if (size >= BB_TypeResister.typeList.size()) {
				size = size - BB_TypeResister.typeList.size();
			} else if (size < 0) {
				size = size + BB_TypeResister.typeList.size();
			}
			brace.setType(stack, BB_TypeResister.typeList.get(size));
			return true;
		}*/
		return false;
	}

}
