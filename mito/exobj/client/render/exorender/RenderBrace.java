package com.mito.exobj.client.render.exorender;

import com.mito.exobj.BraceBase.*;
import com.mito.exobj.BraceBase.Brace.ModelObject;
import com.mito.exobj.client.BraceHighLightHandler;
import com.mito.exobj.client.mitoClientProxy;
import com.mito.exobj.client.render.BB_Render;
import com.mito.exobj.client.render.model.IDrawable;
import com.mito.exobj.common.Main;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class RenderBrace extends BB_Render {

	BlockRendererDispatcher blockrender = Minecraft.getMinecraft().getBlockRendererDispatcher();

	@Override
	public void drawHighLight(ExtraObject base, float partialticks) {
		ModelObject brace = (ModelObject) base;

		BraceHighLightHandler data = ((mitoClientProxy) Main.proxy).bh;
		if (data.key == null || !data.key.equals(base)) {
			data.buffer.delete();
			CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;
			c.beginRegist(GL15.GL_STATIC_DRAW, GL11.GL_LINES);
			c.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			IDrawable model = brace.getModel();
			c.translate(brace.getPos());
			if (model != null)
				model.drawLine(c);
			data.key = base;
			VBOHandler vbo = c.end();
			data.buffer.add(vbo);
		}

		BB_RenderHandler.enableClient();
		data.buffer.draw();
		BB_RenderHandler.disableClient();

	}

	public void doRender(ExtraObject base, float x, float y, float z, float partialTickTime) {
		ModelObject brace = (ModelObject) base;
		//BB_TypeResister.getFigure(brace.shape).drawBraceTessellator(brace, partialTickTime);
	}

	public void updateRender(CreateVertexBufferObject c, ExtraObject base) {
		//MyLogger.info("a");
		float i = base.getBrightnessForRender(0);

		ModelObject brace = (ModelObject) base;
		MapColor j = brace.texture.getMapColor(brace.texture.getStateFromMeta(brace.color));
		//c.setColor(j.colorIndex);
		c.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		TextureAtlasSprite tas = blockrender.getModelForState(brace.texture.getStateFromMeta(brace.color)).getParticleTexture();;
		c.pushMatrix();
		IDrawable model = brace.getModel();
		c.translate(brace.getPos());
		if (model != null)
			model.drawVBOIIcon(c, tas);
		c.popMatrix();

	}
}
