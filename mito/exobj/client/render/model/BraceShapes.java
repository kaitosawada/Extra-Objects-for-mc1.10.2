package com.mito.exobj.client.render.model;

import com.mito.exobj.BraceBase.Brace.Brace;
import net.minecraft.util.math.Vec3d;

public class BraceShapes implements IDrawBrace {

	public IDrawBrace[] planes;

	public BraceShapes(IDrawBrace... list) {
		planes = list;
	}

	@Override
	public IDrawable getModel(Brace brace, Vec3d pos) {
		BB_ModelGroup ret = new BB_ModelGroup();
		for (IDrawBrace plane : planes) {
			if (plane != null)
				ret.add(plane.getModel(brace, pos));
		}
		return ret;
	}

}
