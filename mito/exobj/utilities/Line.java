package com.mito.exobj.utilities;

import com.mito.exobj.client.render.model.ILineBrace;
import com.mito.exobj.client.render.model.LineWithDirection;
import com.mito.exobj.client.render.model.Mat4;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Line implements ILineBrace {

	public Vec3d start;
	public Vec3d end;

	public Line(Vec3d s, Vec3d e) {
		this.start = s;
		this.end = e;
	}

	public double getLength() {
		return MitoMath.subAbs(this.start, this.end);
	}

	@Override
	public void move(Vec3d motion, int command) {
		this.start = MitoMath.vectorSum(this.start, motion);
		this.end = MitoMath.vectorSum(this.end, motion);
	}

	private void setVec3(NBTTagCompound nbt, String name, Vec3d vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3d getVec3d(NBTTagCompound nbt, String name) {
		return new Vec3d(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	public void writeNBT(NBTTagCompound nbt) {
		setVec3(nbt, "start", start);
		setVec3(nbt, "end", end);
		nbt.setInteger("line", 0);
	}

	public boolean interactWithAABB(AxisAlignedBB aabb, double size) {
		boolean ret = false;
		if (aabb.expand(size, size, size).calculateIntercept(start, this.end) != null
				|| (aabb.expand(size, size, size).isVecInside(start) && aabb.expand(size, size, size).isVecInside(this.end))) {
			ret = true;
		}
		return ret;
	}

	@Override
	public void rotation(Vec3d cent, double yaw) {
		start = MitoMath.vectorSum(MitoMath.rotY(MitoMath.sub_vector(start, cent), yaw), cent);
		end = MitoMath.vectorSum(MitoMath.rotY(MitoMath.sub_vector(end, cent), yaw), cent);

	}

	@Override
	public void resize(Vec3d cent, double i) {
		start = MitoMath.vectorSum(MitoMath.vectorMul(MitoMath.sub_vector(start, cent), i), cent);
		end = MitoMath.vectorSum(MitoMath.vectorMul(MitoMath.sub_vector(end, cent), i), cent);

	}

	@Override
	public void addCoordinate(double x, double y, double z) {
		this.start = this.start.addVector(x, y, z);
		this.end = this.end.addVector(x, y, z);

	}

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity, double size) {
		Vec3d v3 = MitoMath.sub_vector(this.end, this.start);
		int div = size > 0 ? (int) Math.floor(MitoMath.abs(v3) / size) + 1 : 1;
		Vec3d part = MitoMath.vectorDiv(MitoMath.sub_vector(v3, MitoMath.vectorMul(v3.normalize(), size)), div);
		Vec3d offset = MitoMath.vectorMul(v3.normalize(), size / 2);
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		for (int n = 0; n <= div; n++) {
			Vec3d v = MitoMath.vectorSum(this.start, offset, MitoMath.vectorMul(part, (double) n));
			AxisAlignedBB aabb1 = MyUtil.createAabbBySize(v, size);
			if (aabb1 != null && aabb1.intersectsWith(aabb)) {
				//list.add(aabb1);
				collidingBoundingBoxes.add(aabb1);
			}
		}
	}

	@Override
	public void snap(RayTraceResult mop, boolean b) {
		if (b) {
			double leng = MitoMath.subAbs(start, end);
			if (leng < 1.5) {
				double r = MitoMath.subAbs(start, mop.hitVec) / leng;
				//absは絶対値なので厳密ではない
				if (r < 0.3333) {
					mop.hitVec = start;
				} else if (r > 0.6666) {
					mop.hitVec = end;
				} else {
					mop.hitVec = MitoMath.ratio_vector(start, end, 0.5);
				}
			} else {
				if (MitoMath.subAbs(start, mop.hitVec) < 0.5) {
					mop.hitVec = start;
				} else if (MitoMath.subAbs(end, mop.hitVec) < 0.5) {
					mop.hitVec = end;
				} else if (MitoMath.subAbs(MitoMath.ratio_vector(start, end, 0.5), mop.hitVec) < 0.25) {
					mop.hitVec = MitoMath.ratio_vector(start, end, 0.5);
				}
			}
		} else {
			double r = MitoMath.subAbs(start, mop.hitVec) / MitoMath.subAbs(start, end);
			if (r < 0.5) {
				mop.hitVec = start;
			} else if (r > 0.5) {
				mop.hitVec = end;
			}
		}
	}

	@Override
	public Vec3d getStart() {
		return MitoMath.copyVec3(this.start);
	}

	@Override
	public Vec3d getEnd() {
		return MitoMath.copyVec3(this.end);
	}

	public LineWithDirection getODrawLine() {
		Vec3d s = this.start;
		Vec3d e = this.end;
		Vec3d sn = start.subtract(end).normalize();
		Mat4 m = MyUtil.getRotationMatrix(sn);
		return new LineWithDirection(s, e, m, m);
	}

	@Override
	public LineWithDirection[] getDrawLine() {
		return new LineWithDirection[]{getODrawLine()};
	}

	@Override
	public List<Line> getSegments() {
		List<Line> ret = new ArrayList<Line>();
		ret.add(this);
		return ret;
	}

	@Override
	public List<Vec3d> getLine() {
		List<Vec3d> ret = new ArrayList<Vec3d>();
		ret.add(start);
		ret.add(end);
		return ret;
	}

}