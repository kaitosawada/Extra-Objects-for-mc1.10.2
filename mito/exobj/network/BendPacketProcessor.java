package com.mito.exobj.network;

import com.mito.exobj.BraceBase.ChunkAndWorldManager;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.client.render.exorender.BezierCurve;
import com.mito.exobj.common.Main;
import com.mito.exobj.utilities.Line;
import com.mito.exobj.utilities.MitoMath;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BendPacketProcessor implements IMessage, IMessageHandler<BendPacketProcessor, IMessage> {

	public int id;
	public double x;
	public double y;
	public double z;
	public boolean isSetCP;
	public boolean line;

	public BendPacketProcessor() {
	}

	public BendPacketProcessor(Brace brace, Vec3d v, boolean isSet) {

		this.id = brace.BBID;
		this.x = v.xCoord;
		this.y = v.yCoord;
		this.z = v.zCoord;
		this.isSetCP = isSet;
		this.line = false;
	}

	public BendPacketProcessor(Brace brace) {
		this.id = brace.BBID;
		this.line = true;
	}

	@Override
	public IMessage onMessage(BendPacketProcessor message, MessageContext ctx) {
		ExtraObject base = ChunkAndWorldManager.getWorldData(Main.proxy.getClientWorld()).getBraceBaseByID(message.id);
		if (base != null && base instanceof Brace) {
			Brace brace = (Brace) base;
			if (message.line) {
				if (brace.line instanceof BezierCurve) {
					BezierCurve b = (BezierCurve) brace.line;
					brace.line = new Line(b.points[0], b.points[3]);
				}
			} else {
				Vec3d end = new Vec3d(message.x, message.y, message.z);
				if (message.isSetCP) {
					if (brace.line instanceof Line) {
						brace.line = new BezierCurve(brace.line.getStart(), end, MitoMath.ratio_vector(brace.line.getEnd(), end, 0.8), brace.line.getEnd());
					} else if (brace.line instanceof BezierCurve) {
						BezierCurve b = (BezierCurve) brace.line;
						b.points[1] = end;
					}
				} else {
					if (brace.line instanceof Line) {
						brace.line = new BezierCurve(brace.line.getStart(), MitoMath.ratio_vector(brace.line.getStart(), end, 0.8), end, brace.line.getEnd());
					} else if (brace.line instanceof BezierCurve) {
						BezierCurve b = (BezierCurve) brace.line;
						b.points[2] = end;
					}
				}
			}
			ChunkAndWorldManager.getWorldData(Main.proxy.getClientWorld()).shouldUpdateRender = true;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		this.line = buf.readBoolean();
		if (!line) {
			this.x = buf.readDouble();
			this.y = buf.readDouble();
			this.z = buf.readDouble();
			this.isSetCP = buf.readBoolean();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeBoolean(line);
		if (!line) {
			buf.writeDouble(x);
			buf.writeDouble(y);
			buf.writeDouble(z);
			buf.writeBoolean(this.isSetCP);
		}
	}

}
