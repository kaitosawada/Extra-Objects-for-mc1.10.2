package com.mito.exobj.client;

import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import com.mito.exobj.client.render.BB_Render;
import com.mito.exobj.BraceBase.BB_ResisteredList;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.VBOList;
import com.mito.exobj.common.item.ItemBraceBase;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BraceHighLightHandler {

	mitoClientProxy proxy;
	public Object key = null;
	public VBOList buffer = new VBOList();

	public BraceHighLightHandler(mitoClientProxy p) {

		this.proxy = p;
	}

	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		ItemStack stack = e.getPlayer().getHeldItemMainhand();
		if (stack != null && (stack.getItem() instanceof ItemBraceBase)) {
			ItemBraceBase itembrace = (ItemBraceBase) stack.getItem();
			RayTraceResult mop = itembrace.getMovingOPWithKey(stack, e.getPlayer().worldObj, e.getPlayer(), proxy.getKey(), e.getTarget(), e.getPartialTicks());
			boolean flag = itembrace.drawHighLightBox(stack, e.getPlayer(), e.getPartialTicks(), mop);
			if (flag) {
				if (e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		} else if (e.getPlayer().capabilities.isCreativeMode) {
			if (MyUtil.isBrace(e.getTarget())) {
				GL11.glPushMatrix();
				drawHighLightBrace(e.getPlayer(), MyUtil.getBrace(e.getTarget()), e.getPartialTicks());
				GL11.glPopMatrix();
				if (e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		}
	}

	public void drawHighLightBrace(EntityPlayer player, ExtraObject base, float partialticks) {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
		BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
		render.drawHighLight(base, partialticks);
		GL11.glPopMatrix();
	}

}
