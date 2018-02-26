package com.mito.exobj.client.render.model;

import com.mito.exobj.BraceBase.Brace.Brace;
import net.minecraft.util.math.Vec3d;

public class Pattern implements IDrawBrace {

	public IDrawable model;
	public double length;

	public Pattern(double length, IDrawable model) {
		this.model = model;
		this.length = length;
	}

	@Override
	public BB_Model getModel(Brace brace, Vec3d pos) {
		return null;
	}

}
