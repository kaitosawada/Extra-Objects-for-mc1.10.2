package com.mito.exobj.client.render.exorender;

import com.mito.exobj.client.render.model.ILineBrace;
import com.mito.exobj.client.render.model.LineWithDirection;
import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MitoMath;
import com.mito.exobj.utilities.MyUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.List;

public class BezierCurve implements ILineBrace {

	public Vec3d[] points;

	public BezierCurve(Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4) {
		this.points = new Vec3d[]{p1, p2, p3, p4};
	}

	public Vec3d getPoint(double t) {
		Vec3d ret = processBezier(points, t);
		return ret;
	}

	public Vec3d getTangent(double t) {
		if (points.length == 3) {
			return MitoMath.normalBezier(points[2], points[1], points[0], t);
		} else if (points.length == 4) {
			return MitoMath.normalBezier(points[3], points[2], points[1], points[0], t);
		} else if (points.length == 2) {
			return MitoMath.sub_vector(points[1], points[0]).normalize();
		}
		return null;
	}

	public Vec3d secondTan(double t) {
		Vec3d ret = MitoMath.vectorSum(MitoMath.vectorMul(points[0], 1 - t), MitoMath.vectorMul(points[1], 3 * t - 2), MitoMath.vectorMul(points[2], 1 - 3 * t), MitoMath.vectorMul(points[3], t));
		return ret.normalize();
	}

	public Vec3d processBezier(Vec3d[] points, double t) {
		if (points.length > 1) {
			Vec3d[] ps = new Vec3d[points.length - 1];
			for (int n = 0; n < points.length - 1; n++) {
				ps[n] = MitoMath.ratio_vector(points[n], points[n + 1], t);
			}
			return processBezier(ps, t);
		} else if (points.length == 1) {
			return points[0];
		}
		return null;
	}

	@Override
	public void move(Vec3d motion, int command) {
		for (int n = 0; n < points.length; n++) {
			points[n] = MitoMath.vectorSum(points[n], motion);
		}
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
		setVec3(nbt, "bezier1", points[0]);
		setVec3(nbt, "bezier2", points[1]);
		setVec3(nbt, "bezier3", points[2]);
		setVec3(nbt, "bezier4", points[3]);
		nbt.setInteger("line", 1);
	}

	@Override
	public void rotation(Vec3d cent, double yaw) {
		for (int n = 0; n < points.length; n++) {
			points[n] = MitoMath.vectorSum(MitoMath.rotY(MitoMath.sub_vector(points[n], cent), yaw), cent);
		}
	}

	@Override
	public void resize(Vec3d cent, double i) {
		for (int n = 0; n < points.length; n++) {
			points[n] = MitoMath.vectorSum(MitoMath.vectorMul(MitoMath.sub_vector(points[n], cent), i), cent);
		}
	}

	/*@Override
	public AxisAlignedBB getBoundingBox(double size) {
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		List<Vec3d> list = this.getLine();
		for (Vec3d v : list) {
			maxX = maxX > v.xCoord ? maxX : v.xCoord;
			maxY = maxY > v.yCoord ? maxY : v.yCoord;
			maxZ = maxZ > v.zCoord ? maxZ : v.zCoord;
			minX = minX < v.xCoord ? minX : v.xCoord;
			minY = minY < v.yCoord ? minY : v.yCoord;
			minZ = minZ < v.zCoord ? minZ : v.zCoord;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ).expand(size, size, size);
	}*/

	public List<Vec3d> getLine() {
		List<Vec3d> ret = new LinkedList<Vec3d>();
		int nm = this.getAccuracy();
		for (int n = 0; n < nm; n++) {
			Vec3d v = this.getPoint((double) n / (double) nm);
			ret.add(v);
		}
		return ret;
	}

	public List<Line> getSegments() {
		List<Line> ret = new LinkedList<Line>();
		int nm = this.getAccuracy();
		for (int n = 0; n < nm - 1; n++) {
			Vec3d v = this.getPoint((double) n / (double) nm);
			Vec3d v1 = this.getPoint((double) (n + 1) / (double) nm);
			ret.add(new Line(v, v1));
		}
		return ret;
	}

	/*@Override
	public double getMinY() {
		double minY = Double.MAX_VALUE;
		List<Vec3d> list = this.getLine();
		for (Vec3d v : list) {
			minY = minY < v.yCoord ? minY : v.yCoord;
		}
		return minY;
	}

	@Override
	public double getMaxY() {
		double maxY = Double.MIN_VALUE;
		List<Vec3d> list = this.getLine();
		for (Vec3d v : list) {
			maxY = maxY > v.yCoord ? maxY : v.yCoord;
		}
		return maxY;
	}

	@Override
	public Vec3d getPos() {
		return this.getPoint(0.5);
	}*/

	@Override
	public void addCoordinate(double x, double y, double z) {
		for (int n = 0; n < points.length; n++) {
			points[n] = points[n].addVector(x, y, z);
		}
	}

	/*@Override
	public boolean interactWithAABB(AxisAlignedBB aabb, double size) {
		boolean ret = false;
		List<Line> list = this.getSegments();
		for (Line line : list) {
			if (line.interactWithAABB(aabb, size)) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public Line interactWithRay(Vec3d set, Vec3d end, double size) {
		if (this.points[0].distanceTo(this.points[3]) < 0.01) {
			Vec3d ve = MitoMath.getNearPoint(set, end, this.points[0]);
			if (ve.distanceTo(this.points[0]) < size / 1.5) {
				return new Line(ve, this.points[0]);
			}
		}
		Line line = null;
		List<Line> list = this.getSegments();
		for (Line l : list) {
			Line line2 = MitoMath.getDistanceLine(set, end, l.start, l.end);
			if (line2.getLength() < size / 1.5 && !(MyUtil.isVecEqual(line2.end, this.points[0]) || MyUtil.isVecEqual(line2.end, this.points[3]))) {
				if (line == null || line2.end.distanceTo(set) < line.end.distanceTo(set)) {
					line = line2;
				}
			}
		}
		return line;
	}
	@Override
	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity, double size) {
		List<Line> list = this.getSegments();
		for (Line line : list) {
			line.addCollisionBoxesToList(world, aabb, collidingBoundingBoxes, entity, size);
		}
		/*Vec3d v3 = MitoMath.sub_vector(this.end, this.start);
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
	}*/


	@Override
	public void snap(RayTraceResult mop) {
		if (points == null || points.length == 0) {
			return;
		}
		Vec3d end = points[points.length - 1];
		double leng = MitoMath.subAbs(points[0], end);
		if (leng < 1.5) {
			double r = MitoMath.subAbs(points[0], mop.hitVec) / leng;
			//absは絶対値なので厳密ではない
			if (r < 0.3333) {
				mop.hitVec = points[0];
			} else if (r > 0.6666) {
				mop.hitVec = end;
			} else {
				mop.hitVec = MitoMath.ratio_vector(points[0], end, 0.5);
			}
		} else {
			if (MitoMath.subAbs(points[0], mop.hitVec) < 0.5) {
				mop.hitVec = points[0];
			} else if (MitoMath.subAbs(end, mop.hitVec) < 0.5) {
				mop.hitVec = end;
			} else if (MitoMath.subAbs(MitoMath.ratio_vector(points[0], end, 0.5), mop.hitVec) < 0.25) {
				mop.hitVec = MitoMath.ratio_vector(points[0], end, 0.5);
			}
		}
	}

	/*@Override
	public double getYaw(Vec3d pos) {
		return 0;
	}

	@Override
	public double getPitch(Vec3d pos) {
		return 0;
	}

	@Override
	public Vec3d getMotion(Vec3d pos, double speed, boolean dir) {
		return Vec3d.createVectorHelper(0, 0, 0);
	}*/

	public int getAccuracy() {
		//int i = (int) (points[0].distanceTo(points[1]) + points[1].distanceTo(points[2]) + points[2].distanceTo(points[3]) * 5.0);
		return 20;//Math.max(i, 20);
	}

	@Override
	public double getLength() {
		double ret = 0;
		List<Line> list = this.getSegments();
		for (Line line : list) {
			ret += line.getLength();
		}
		return ret;
	}

	@Override
	public Vec3d getStart() {
		return this.points[0];
	}

	@Override
	public Vec3d getEnd() {
		return this.points[3];
	}

	@Override
	public LineWithDirection[] getDrawLine() {
		int nmax = this.getAccuracy();
		LineWithDirection[] ret = new LineWithDirection[nmax];
		Vec3d s = this.getPoint(0);
		Vec3d sn = this.getTangent(0);
		Vec3d ms = this.secondTan(0);
		for (int n = 0; n < nmax; n++) {
			double t1 = (double) (n + 1) / (double) nmax;
			Vec3d e = this.getPoint(t1);
			Vec3d en = this.getTangent(t1);
			Vec3d me = this.secondTan(t1);
			ret[n] = new LineWithDirection(s, e, MyUtil.getRotationMatrix(sn, ms), MyUtil.getRotationMatrix(en, me));
			s = MitoMath.copyVec3(e);
			sn = MitoMath.copyVec3(en);
			ms = MitoMath.copyVec3(me);
		}
		return ret;
	}

}
