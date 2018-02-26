package com.mito.exobj.BraceBase.Brace;


import com.mito.exobj.client.render.exorender.BB_TypeResister;
import com.mito.exobj.client.render.exorender.IJoint;
import com.mito.exobj.client.render.model.BB_ModelGroup;
import com.mito.exobj.client.render.model.IDrawable;
import com.mito.exobj.utilities.MitoMath;
import net.minecraft.util.math.Vec3d;

public class CubeJoint implements IJoint {

	@Override
	public IDrawable getModel(Brace brace, Vec3d pos) {
		BB_ModelGroup ret = new BB_ModelGroup();
		ret.add(BB_TypeResister.createRectangle(MitoMath.sub_vector(brace.line.getStart(), pos), brace.size * 1.414));
		ret.add(BB_TypeResister.createRectangle(MitoMath.sub_vector(brace.line.getEnd(), pos), brace.size * 1.414));
		return ret;
	}
}
