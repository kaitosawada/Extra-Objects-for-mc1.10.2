package com.mito.exobj.common.item;

import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.BB_Key;
import com.mito.exobj.common.entity.EntityWrapperBB;
import com.mito.exobj.network.PacketHandler;
import com.mito.exobj.network.SyncPacketProcessor;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemBar extends ItemBraceBase implements IMouseWheel {

	/*int size = 2;
	public double[] sizeArray = {0.2, 1.0, 3.0, 0.005};*/

	public ItemBar() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(3);

	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, float partialticks, RayTraceResult mop) {
		return this.drawHighLightBrace(player, partialticks, mop);
	}

	@Override
	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			RayTraceResult m2 = this.getMovingOPWithKey(stack, player.worldObj, player, key, Minecraft.getMinecraft().objectMouseOver, 1.0);
			if (m2 != null) {
				if (MyUtil.isBrace(m2) && ((EntityWrapperBB) m2.entityHit).base instanceof Brace) {
					Brace brace = (Brace) ((EntityWrapperBB) m2.entityHit).base;
					int w = dwheel / 120;
					double div = brace.getRoll() + (double) w * 15;
					if (div < 0) {
						div = div + 360;
					} else if (div > 360) {
						div = div - 360;
					}
					brace.setRoll(div);
					PacketHandler.INSTANCE.sendToServer(new SyncPacketProcessor(brace));
					brace.dataworld.shouldUpdateRender = true;
					return true;
				}
			}
		}
		return false;
	}

}
