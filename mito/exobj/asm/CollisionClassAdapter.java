package com.mito.exobj.asm;

import com.mito.exobj.common.MyLogger;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CollisionClassAdapter extends ClassVisitor implements Opcodes {


	public CollisionClassAdapter(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		String srgMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, name, desc);
		String srgDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		//MyLogger.warn("method : " + srgMethod + "  desc : " + srgDesc);
		if (("getCollisionBoxes".equals(srgMethod) || "a".equals(srgMethod)) && "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;".equals(srgDesc)) {
			return new MethodAdapter(super.visitMethod(access, name, desc, signature, exceptions));
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
/*
	@Override
	public FieldVisitor visitField(int access, String list, String desc,
								   String signature, Object value) {
		MyLogger.warn("method : " + list + "  desc : " + desc);
		if (cv != null) {
			return cv.visitField(access, list, desc, signature, value);
		}
		return null;
	}*/

	public static class MethodAdapter extends MethodVisitor {
		public MethodAdapter(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == ARETURN) {
				super.visitVarInsn(ALOAD, 0);
				super.visitVarInsn(ALOAD, 2);
				super.visitVarInsn(ALOAD, 3);
				super.visitVarInsn(ALOAD, 1);
				super.visitMethodInsn(INVOKESTATIC, "com/mito/exobj/asm/BraceCoreHooks", "getCollisionHook", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V", false);
			}
			if (mv != null) {
				mv.visitInsn(opcode);
			}
		}
	}

}
