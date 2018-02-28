package com.mito.exobj.BraceBase;

import java.util.HashMap;
import java.util.Map;

import com.mito.exobj.client.render.BB_Render;
import org.apache.logging.log4j.Level;

import com.mito.exobj.BraceBase.Brace.Brace;
import com.mito.exobj.BraceBase.Brace.GuideBrace;
import com.mito.exobj.BraceBase.Brace.Tofu;
import com.mito.exobj.client.render.exorender.RenderObject;
import com.mito.exobj.client.render.exorender.RenderGuideBrace;
import com.mito.exobj.common.MyLogger;
import com.mito.exobj.network.BB_PacketProcessor;
import com.mito.exobj.network.BB_PacketProcessor.Mode;
import com.mito.exobj.network.PacketHandler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class BB_ResisteredList {

	public static int nextID = 0;

	public static Map<String, Class> stringToClassMapping = new HashMap<String, Class>();
	public static Map<Class, String> classToStringMapping = new HashMap<Class, String>();
	public static Map<Class, BB_Render> classToRenderMapping = new HashMap<Class, BB_Render>();

	/**
	 * adds a mapping between FObj classes and both a string representation and an ID
	 */
	public static void addMapping(Class ioclass, String name, int id, BB_Render render) {
		if (stringToClassMapping.containsKey(name)) {
			throw new IllegalArgumentException("ID is already registered: " + name);
		} else {
			stringToClassMapping.put(name, ioclass);
			classToStringMapping.put(ioclass, name);
			classToRenderMapping.put(ioclass, render);
		}
	}

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static ExtraObject createExObjByName(String p_75620_0_, World p_75620_1_) {
		ExtraObject iobj = null;

		try {
			Class oclass = stringToClassMapping.get(p_75620_0_);

			if (oclass != null) {
				iobj = (ExtraObject) oclass.getConstructor(World.class).newInstance(new Object[]{p_75620_1_});
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return iobj;
	}

	/**
	 * create a new instance of an entity from NBT store
	 */

	public static ExtraObject createExObjFromNBT(NBTTagCompound nbt, World world) {
		return createExObjFromNBT(nbt, world, -1);
	}

	public static ExtraObject createExObjFromNBT(NBTTagCompound nbt, World world, int id) {
		ExtraObject exObj = null;

		Class oclass = null;
		try {
			if (nbt.getString("id") == null) {
				MyLogger.info("id is null");
			} else {
				//mitoLogger.info("id is " + nbt.getString("id"));
			}
			oclass = stringToClassMapping.get(nbt.getString("id"));

			if (oclass == null) {
				MyLogger.info("class is null " + nbt.getString("id"));
			}
			if (oclass != null) {
				exObj = (ExtraObject) oclass.getConstructor(World.class).newInstance(new Object[]{world});
				if (id != -1) {
					exObj.BBID = id;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (exObj != null) {
			try {
				exObj.readFromNBT(nbt);
			} catch (Exception e) {
				FMLLog.log(Level.ERROR, e,
						"An Entity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
						nbt.getString("id"), oclass.getName());
				exObj = null;
			}
		} else {
			MyLogger.warn("Skipping Extra Object with id " + nbt.getString("id"));
		}

		return exObj;
	}

	/**
	 * Gets the string representation of a specific entity.
	 */
	public static String getBraceBaseString(ExtraObject p_75621_0_) {
		return classToStringMapping.get(p_75621_0_.getClass());
	}

	public static BB_Render getBraceBaseRender(ExtraObject p_75621_0_) {
		return classToRenderMapping.get(p_75621_0_.getClass());
	}

	static {
		addMapping(Brace.class, "Brace", nextID++, new RenderObject());
		addMapping(GuideBrace.class, "GuideBrace", nextID++, new RenderGuideBrace());
		addMapping(Tofu.class, "Tofu", nextID++, new RenderObject());
	}

	public static ExtraObject syncBraceBaseFromNBT(NBTTagCompound nbt, World world, int id) {
		ExtraObject base = BB_DataLists.getWorldData(world).getBraceBaseByID(id);
		if (base != null) {
			base.readFromNBT(nbt);
			if (base.datachunk != null)
				base.datachunk.modified();
		} else {
			PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.DELETE, base));
			MyLogger.warn("Skipping Entity with id " + nbt.getString("id"));
		}
		return base;
	}

}
