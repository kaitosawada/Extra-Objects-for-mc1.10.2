package com.mito.exobj.asm;

import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.mito.exobj.common.MyLogger;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class BB_Transformer implements IClassTransformer, Opcodes {

	private static final String TARGET_CLASS_NAME = "net.minecraft.world.World";
	private static final String TARGET_CLASS_NAME2 = "net.minecraft.client.renderer.RenderGlobal";
	public static final String TARGET_CLASS_NAME3 = "net.minecraft.client.renderer.EntityRenderer";
	public static String name1 = "";

	@Override
	public byte[] transform(String name, String transformedName, byte[] data) {

		if (transformedName.equals(TARGET_CLASS_NAME)) {
			try {
				return transformRender(data, name, new TransCollision());
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceCollisionTransformer loading", e);
			}
		} else if (transformedName.equals(TARGET_CLASS_NAME2)) {
			try {
				//return transformRender(data, name, new TransRender());
				name1 = name;
				EntityRenderer r;
				ClassReader cr = new ClassReader(data);
				ClassWriter cw = new ClassWriter(1);
				ClassVisitor cv = new RenderClassAdapter(ASM4, cw);
				cr.accept(cv, 0);
				return cw.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceEntityRendererTransformer loading", e);
			}
		} else if (transformedName.equals(TARGET_CLASS_NAME3)) {
			try {
				//return transformRender(data, name, new TransMouseOver());
				name1 = name;
				EntityRenderer r;
				ClassReader cr = new ClassReader(data);
				ClassWriter cw = new ClassWriter(1);
				ClassVisitor cv = new MouseOverClassAdapter(ASM4, cw);
				cr.accept(cv, 0);
				return cw.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceRayTraceTransformer loading", e);
			}
		}
		return data;
	}

	private byte[] transformRender(byte[] data, String name, TransInfo ti) {
		ClassNode cnode = new ClassNode();
		ClassReader reader = new ClassReader(data);
		reader.accept(cnode, 0);

		MethodNode mnode = null;
		for (MethodNode curMnode : cnode.methods) {
			String srgClass = FMLDeobfuscatingRemapper.INSTANCE.map(name);
			String srgMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, curMnode.name, curMnode.desc);
			String srgDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(curMnode.desc);
			//MyLogger.info("transform" + srgMethod + srgDesc);
			if ((ti.targetMethodName.equals(srgMethod) || ti.targetDeobfMethodName.equals(srgMethod)) && ti.targetMethoddesc.equals(srgDesc)) {
				mnode = curMnode;
				break;
			}
		}

		if (mnode != null) {
			ti.transform(mnode);

			ClassWriter cw = new ClassWriter(0);
			cnode.accept(cw);
			data = cw.toByteArray();
			MyLogger.info("transforming of the code succeeded");
		}

		return data;
	}

}
