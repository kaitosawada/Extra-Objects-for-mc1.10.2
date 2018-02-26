package com.mito.exobj.client.render;

import com.mito.exobj.common.block.TileObjects;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileObjectsRenderer extends TileEntitySpecialRenderer {

	public void renderTileEntityAt(TileObjects te, double x, double y, double z, float partialTicks, int destroyStage) {
		/*if (tile instanceof TileObjects) {
			TileObjects to = (TileObjects) tile;
			if (to.name != null) {
				GL11.glPushMatrix();
				GL11.glTranslated(x, y, z);
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				BB_RenderHandler.enableClient();
				List<ExtraObject> list = to.name.list;

				Minecraft.getMinecraft().entityRenderer.enableLightmap((double) part);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				to.buffer.draw();

				BB_RenderHandler.disableClient();
				if (to.shouldUpdateRender) {
					to.buffer.delete();
					CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;
					c.beginRegist(GL15.GL_STATIC_DRAW, GL11.GL_TRIANGLES);
					c.setColor(1.0F, 1.0F, 1.0F, 1.0F);
					for (ExtraObject base : list) {
						BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
						render.updateRender(c, base, tile.xCoord, tile.yCoord, tile.zCoord);
					}
					to.shouldUpdateRender = false;
					VBOHandler vbo = c.end();
					to.buffer.add(vbo);
				}
				Minecraft.getMinecraft().entityRenderer.disableLightmap((double) part);
				GL11.glPopMatrix();
				if (Main.proxy.getClientPlayer().getCurrentEquippedItem() != null && Main.proxy.getClientPlayer().getCurrentEquippedItem().getItem() instanceof ItemSelectTool) {
					GL11.glPushMatrix();
					GL11.glTranslated(x, y, z);
					IIcon boxIIcon = Blocks.lit_redstone_lamp.getBlockTextureFromSide(1);
					RenderBlocks renderer = new RenderBlocks();
					Tessellator t = Tessellator.instance;
					renderInvCuboid(renderer, ResisterItem.BlockObjects, 0.0F / 16.0F, 0.0F / 16.0F, 0.0F / 16.0F, 16.0F / 16.0F, 16.0F / 16.0F, 16.0F / 16.0F, boxIIcon);
					GL11.glLineWidth(3.0F);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					for (ExtraObject base : list) {
						t.startDrawing(GL11.GL_LINE_LOOP);
						t.addVertex(0.5, 0.5, 0.5);
						t.addVertex(base.pos.xCoord, base.pos.yCoord, base.pos.zCoord);
						t.draw();
					}
					GL11.glPopMatrix();
				}
			}

		}*/
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
		// TODO Auto-generated method stub

	}

}
