package com.mito.exobj.client.render.exorender;

import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.render.model.IDrawable;
import net.minecraft.util.math.Vec3d;

public interface IJoint {
	IDrawable getModel(Brace brace, Vec3d pos);

}
