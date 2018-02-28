package com.mito.exobj.asm;

import com.mito.exobj.common.MyLogger;
import com.mito.exobj.common.main.ConfigManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.DeobfuscationTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static com.mito.exobj.common.Main.debug;

public class ChunkUpdateClassAdapter extends ClassVisitor implements Opcodes {


	public ChunkUpdateClassAdapter(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		String srgMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, name, desc);
		String srgDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		//MyLogger.warn("method : " + srgMethod + "  desc : " + srgDesc);

		if (("setNeedsUpdate".equals(srgMethod) || "a".equals(srgMethod)) && "(Z)V".equals(srgDesc)) {

			return new MethodAdapter(super.visitMethod(access, name, desc, signature, exceptions));
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	/*@Override
	public FieldVisitor visitField(int access, String list, String desc,
								   String signature, Object value) {
		MyLogger.warn("method : " + list + "  desc : " + desc + "  signature : " + signature);
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
			//MyLogger.warn("opcode : " + opcode);
			if (opcode == RETURN) {
				//LoadClientWorldHandler.onChunkUpdate(world, position)
				super.visitVarInsn(ALOAD, 0);
				super.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", debug ? "world" : "field_178588_d", debug ? "Lnet/minecraft/world/World;" : "Laid;");
				super.visitVarInsn(ALOAD, 0);
				super.visitFieldInsn(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", debug ? "position" : "field_178586_f", debug ? "Lnet/minecraft/util/math/BlockPos$MutableBlockPos;" : "Lcm$a;");
				super.visitMethodInsn(INVOKESTATIC, "com/mito/exobj/BraceBase/LoadClientWorldHandler", "onChunkUpdate", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", false);
			}
			if (mv != null) {
				mv.visitInsn(opcode);
			}
		}
	}

}
