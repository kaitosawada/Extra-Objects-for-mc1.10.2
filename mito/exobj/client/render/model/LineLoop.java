package com.mito.exobj.client.render.model;

import java.util.ArrayList;
import java.util.List;

import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MitoMath;
import com.mito.exobj.utilities.MyUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class LineLoop implements ILineBrace {

	public List<Vec3d> line = new ArrayList<Vec3d>();
	public boolean isLoop = false;

	public LineLoop(Vec3d... list) {
		for (Vec3d v : list) {
			line.add(v);
		}
	}

	@Override
	public void move(Vec3d motion, int command) {
		for (Vec3d v : line) {
			v = MitoMath.vectorSum(v, motion);
		}
	}

	private void setVec3d(NBTTagCompound nbt, String name, Vec3d vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3d getVec3d(NBTTagCompound nbt, String name) {
		return new Vec3d(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	public void writeNBT(NBTTagCompound nbt) {
		//MyLogger.info("brace read line loop " + line.size());
		NBTTagList taglistGroups = new NBTTagList();
		for (Vec3d v : line) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			setVec3d(nbt1, "vec", v);
			taglistGroups.appendTag(nbt1);
		}
		nbt.setTag("line_list", taglistGroups);
		nbt.setInteger("line", 2);
	}

	@Override
	public void rotation(Vec3d cent, double yaw) {
		for (Vec3d v : line) {
			v = MitoMath.vectorSum(MitoMath.rotY(MitoMath.sub_vector(v, cent), yaw), cent);
		}
	}

	@Override
	public void resize(Vec3d cent, double i) {
		for (Vec3d v : line) {
			v = MitoMath.vectorSum(MitoMath.vectorMul(MitoMath.sub_vector(v, cent), i), cent);
		}
	}

	@Override
	public List<Line> getSegments() {
		List<Line> ret = new ArrayList<Line>();
		for (int n = 0; n < line.size() - 1; n++) {
			Vec3d v = MitoMath.copyVec3(line.get(n));
			Vec3d v1 = MitoMath.copyVec3(line.get(n + 1));
			ret.add(new Line(v, v1));
		}
		return ret;
	}

	@Override
	public void addCoordinate(double x, double y, double z) {
		for (Vec3d v : line) {
			v.addVector(x, y, z);
		}
	}

	@Override
	public void snap(RayTraceResult mop, boolean b) {
		List<Line> list = this.getSegments();
		for (Line l : list) {
			if (MitoMath.getLineNearPoint(l.start, l.end, mop.hitVec).getLength() < 0.01) {
				l.snap(mop, b);
				return;
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
		// TODO 自動生成されたメソッド・スタブ
		return Vec3d.createVectorHelper(0, 0, 0);
	}*/

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
		/*if (this.line.isEmpty()) {
			return Vec3d.createVectorHelper(0, 0, 0);
		}*/
		return this.line.get(0);
	}

	@Override
	public Vec3d getEnd() {
		/*if (this.line.isEmpty()) {
			return Vec3d.createVectorHelper(0, 0, 0);
		}*/
		return this.line.get(line.size() - 1);
	}

	@Override
	public LineWithDirection[] getDrawLine() {
		int acc = this.getAccuracy();
		List<Line> list = this.getSegments();
		int num = acc * (list.size() - 1) + list.size();
		LineWithDirection[] ret = new LineWithDirection[num];
		int num1 = 0;
		//MyLogger.info("line loop a " + num);
		for (Line l : list) {
			//MyLogger.info("line loop " + num1);
			ret[num1] = l.getODrawLine();
			//Vec3d a = Vec3d.createVectorHelper(0, 0, 0);
			//ret[num1] = new LineWithDirection(a, a, a, a, a, a);
			num1 = num1 + acc + 1;
		}
		for (int n = 0; n < list.size() - 1; n++) {
			Line l1 = list.get(n);
			Line l2 = list.get(n + 1);
			Vec3d s = l1.getEnd();
			Vec3d sn = l1.start.subtract(l1.end).normalize();
			Vec3d en = l2.start.subtract(l2.end).normalize();
			Mat4 m1 = MyUtil.getRotationMatrix(sn);
			Mat4 m2 = MyUtil.getRotationMatrix(en);
			for (int n1 = 0; n1 < acc; n1++) {
				double t = (double) n / (double) acc;
				double t1 = (double) (n + 1) / (double) acc;
				/*Vec3d sn1 = MitoMath.ratio_vector(sn, en, t).normalize();
				Vec3d sn2 = MitoMath.ratio_vector(sn, en, t1).normalize();*/
				//MyLogger.info("line loop " + (n * 6 + n1 + 1));
				ret[n * (acc + 1) + n1 + 1] = new LineWithDirection(s, s, m1, m2);
			}
		}
		return ret;
	}

	public int getAccuracy() {
		return 1;
	}

	@Override
	public List<Vec3d> getLine() {
		return line;
	}

}
