package com.mito.exobj.client.render.model;

import com.mito.exobj.BraceBase.Brace.Brace;
import net.minecraft.util.math.Vec3d;

public interface IDrawBrace {

	IDrawable getModel(Brace brace, Vec3d pos);

}
