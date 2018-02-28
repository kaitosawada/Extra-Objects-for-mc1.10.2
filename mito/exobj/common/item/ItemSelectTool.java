package com.mito.exobj.common.item;

import java.util.List;

import com.mito.exobj.BraceBase.ChunkAndWorldManager;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.client.BB_SelectedGroup;
import com.mito.exobj.client.render.RenderHighLight;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.entity.EntityWrapperBB;
import com.mito.exobj.network.GroupPacketProcessor;
import com.mito.exobj.network.GroupPacketProcessor.EnumGroupMode;
import com.mito.exobj.network.PacketHandler;
import com.mito.exobj.utilities.MitoMath;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSelectTool extends ItemBraceBase implements IMouseWheel {

	public ItemSelectTool() {
		super();
		this.setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			NBTTagCompound nbt = getNBT(itemStack);
			RayTraceResult movingOP = Minecraft.getMinecraft().objectMouseOver;
			boolean flag = MyUtil.isBrace(movingOP);
			BB_SelectedGroup sel = Main.proxy.sg;
			RayTraceResult mop = this.getMovingOPWithKey(itemStack, world, player, Main.proxy.getKey(), Minecraft.getMinecraft().objectMouseOver, 1.0);
			BB_Key key = Main.proxy.getKey();

			//遘ｻ蜍輔Δ繝ｼ繝峨�ｮ縺ｨ縺�
			if (sel.modeMove) {
				//繧ｷ繝輔ヨ繧ｯ繝ｪ繝�繧ｯ縺ｧ繝｢繝ｼ繝芽ｧ｣髯､
				if (player.isSneaking() || key.isControlPressed()) {
					sel.delete();
					//遘ｻ蜍募�ｦ逅�
				} else {
					if (!sel.getList().isEmpty()) {
						Vec3d pos = sel.getDistance(mop);
						double yaw = 0;
						PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList(), pos, yaw));
						sel.breakGroup();
						sel.delete();
					}
				}
				sel.setmove(false);
				sel.activated = false;
				//繧ｳ繝斐�ｼ繝｢繝ｼ繝峨�ｮ縺ｨ縺�
			} else if (sel.modeCopy()) {
				//繧ｷ繝輔ヨ繧ｯ繝ｪ繝�繧ｯ縺ｧ繝｢繝ｼ繝芽ｧ｣髯､
				if (player.isSneaking() || key.isControlPressed()) {
					sel.delete();
					//繧ｳ繝斐�ｼ蜃ｦ逅�
				} else {
					if (!sel.getList().isEmpty()) {
						Vec3d pos = sel.getDistance(mop);
						double yaw = 0;
						PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList(), pos, yaw));
					}
				}
				sel.setcopy(false);
				sel.activated = false;
				//繝悶Ο繝�繧ｯ蛹悶Δ繝ｼ繝峨�ｮ縺ｨ縺�
			} else if (sel.modeBlock) {
				//隗｣髯､
				if (player.isSneaking() || key.isControlPressed()) {
					sel.delete();
					//繝悶Ο繝�繧ｯ蛹門�ｦ逅�
				} else {
					int x = mop.sideHit.getFrontOffsetX();
					int y = mop.sideHit.getFrontOffsetY();
					int z = mop.sideHit.getFrontOffsetZ();
					Vec3d v = new Vec3d(mop.getBlockPos().getX() + x, mop.getBlockPos().getY() + y, mop.getBlockPos().getZ() + z);
					PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.SETBLOCK, sel.getList(), v));
					sel.breakGroup();
					sel.delete();
				}
				sel.setblock(false);
				sel.activated = false;

				//騾壼ｸｸ繧ｻ繝ｬ繧ｯ繝亥�ｦ逅�
			} else {
				//遏ｩ蠖｢繧ｻ繝ｬ繧ｯ繝�
				if (sel.activated) {
					Vec3d set = mop.hitVec;
					if (MitoMath.subAbs(sel.set, set) < 500000) {
						AxisAlignedBB aabb = MyUtil.createAABBByVec3d(sel.set, set);
						List<ExtraObject> list = ChunkAndWorldManager.getWorldData(world).getExtraObjectWithAABB(aabb);
						//繧ｷ繝輔ヨ蜷梧凾謚ｼ縺励〒繧ｻ繝ｬ繧ｯ繝郁ｿｽ蜉�
						if (player.isSneaking() || key.isControlPressed()) {
							sel.addShift(list);
						} else {
							sel.replace(list);
						}
					}
					sel.activated = false;
					//逶ｴ謗･繧ｻ繝ｬ繧ｯ繝�
				} else {
					if (flag) {
						ExtraObject base = ((EntityWrapperBB) movingOP.entityHit).base;
						//繧ｷ繝輔ヨ蜷梧凾謚ｼ縺励〒繧ｻ繝ｬ繧ｯ繝郁ｿｽ蜉�
						if (player.isSneaking() || key.isControlPressed()) {
							sel.addShift(base);
							//GUI繧帝幕縺�
						} else {
							if (sel.getList().contains(base)) {
								//GUI
								if (key.isAltPressed()) {
									sel.set = mop.hitVec;
									sel.setcopy(true);
									//PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList()));
								} else {
									sel.set = mop.hitVec;
									PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.GUI, sel.getList()));
								}
							} else {
								sel.replace(base);
							}
						}
						sel.activated = false;
						//繝悶Ξ繝ｼ繧ｹ莉･螟悶ｒ繧ｯ繝ｪ繝�繧ｯ
					} else {
						if (player.isSneaking() || key.isControlPressed()) {
							sel.delete();
						} else {
							Vec3d set = mop.hitVec;
							sel.set = set;
							sel.activated = true;
						}
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}


	@Override
	public boolean drawHighLightBox(ItemStack itemStack, EntityPlayer player, float partialticks, RayTraceResult mop) {
		if (Minecraft.getMinecraft().currentScreen == null) {
			BB_SelectedGroup sel = Main.proxy.sg;
			if (mop == null)
				return false;
			Vec3d set = mop.hitVec;
			RenderHighLight rh = RenderHighLight.INSTANCE;
			if (sel.modeCopy() || sel.modeMove) {
				sel.drawHighLightCopy(player, partialticks, mop);
			} else if (sel.modeBlock) {
				int x = mop.sideHit.getFrontOffsetX();
				int y = mop.sideHit.getFrontOffsetY();
				int z = mop.sideHit.getFrontOffsetZ();
				Vec3d v = new Vec3d(0.5 + mop.getBlockPos().getX() + x, 0.5 + mop.getBlockPos().getY() + y, 0.5 + mop.getBlockPos().getZ() + z);
				rh.drawBox(player, v, 0.95, partialticks);
			}
			sel.drawHighLightGroup(player, partialticks);
			if (sel.activated && MyUtil.canClick(player.worldObj, Main.proxy.getKey(), mop)) {
				Vec3d end = sel.set;
				rh.drawBox(player, set, end, partialticks);
				return true;
			} else {
				return this.drawHighLightBrace(player, partialticks, mop);
			}
		}
		return false;
	}

	@Override
	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		BB_SelectedGroup sel = Main.proxy.sg;
		if (sel.modeCopy() && key.isShiftPressed()) {
			int w = dwheel / 120;
			double div = sel.pasteNum + w;
			if (sel.pasteNum < 0) {
				sel.pasteNum = 50000;
			}
			return true;
		}
		return false;
	}

	public void snapDegree(RayTraceResult mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key) {}
}
