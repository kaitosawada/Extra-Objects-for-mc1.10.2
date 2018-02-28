package com.mito.exobj.client.render.model;

import com.mito.exobj.utilities.Line;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public interface ILineBrace {

	void move(Vec3d motion, int command);

	void writeNBT(NBTTagCompound nbt);

	void rotation(Vec3d cent, double yaw);

	void resize(Vec3d cent, double i);

	void addCoordinate(double x, double y, double z);

	void snap(RayTraceResult mop);

	double getLength();

	Vec3d getStart();

	Vec3d getEnd();

	LineWithDirection[] getDrawLine();

	List<Line> getSegments();

	List<Vec3d> getLine();

}
