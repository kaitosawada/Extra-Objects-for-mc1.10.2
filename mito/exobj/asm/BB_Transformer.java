package com.mito.exobj.asm;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;
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
	public static final String TARGET_CLASS_NAME4 = "net.minecraft.client.renderer.chunk.RenderChunk";

	@Override
	public byte[] transform(String name, String transformedName, byte[] data) {

		World w;
		if (transformedName.equals(TARGET_CLASS_NAME)) {
			try {
				ClassReader cr = new ClassReader(data);
				ClassWriter cw = new ClassWriter(1);
				ClassVisitor cv = new CollisionClassAdapter(ASM4, cw);
				cr.accept(cv, 0);
				return cw.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceCollisionTransformer loading", e);
			}
		} else if (transformedName.equals(TARGET_CLASS_NAME2)) {
			try {
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
				ClassReader cr = new ClassReader(data);
				ClassWriter cw = new ClassWriter(1);
				ClassVisitor cv = new MouseOverClassAdapter(ASM4, cw);
				cr.accept(cv, 0);
				return cw.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceRayTraceTransformer loading", e);
			}
		} else if (transformedName.equals(TARGET_CLASS_NAME4)) {
			try {
				ClassReader cr = new ClassReader(data);
				ClassWriter cw = new ClassWriter(1);
				ClassVisitor cv = new ChunkUpdateClassAdapter(ASM4, cw);
				cr.accept(cv, 0);
				return cw.toByteArray();
			} catch (Exception e) {
				throw new RuntimeException("failed : BraceRayTraceTransformer loading", e);
			}
		}
		return data;
	}
}
