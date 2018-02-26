package com.mito.exobj.asm;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


public class TransCollision extends TransInfo {

	public TransCollision() {
		super();
		this.targetClassName = "net.minecraft.world.World";
		this.targetMethodName = "getCollisionBoxes";
		this.targetDeobfMethodName = "func_72945_a";
		this.targetMethoddesc = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;";
	}

	public void transform(MethodNode mnode) {
		InsnList list = new InsnList();

		//getCollisionHook(this, p_72945_2_, this.collidingBoundingBoxes, p_72945_1_)
		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new VarInsnNode(ALOAD, 2));
		list.add(new VarInsnNode(ALOAD, 3));
		list.add(new VarInsnNode(ALOAD, 1));
		list.add(new MethodInsnNode(INVOKESTATIC, "com/mito/exobj/asm/BraceCoreHooks", "getCollisionHook", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V", false));

		int n = 3;
		mnode.instructions.insert(mnode.instructions.get(n), list);
		/*try {
			File data = new File((File) FMLInjectionData.data()[6], "data.txt");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(data)));
			for (int i = 0; i <= mnode.instructions.indexOf(mnode.instructions.getLast()); i++) {

				pw.println("transform " + mnode.instructions.get(i).toString() + " " + mnode.instructions.get(i).getOpcode());
				if(mnode.instructions.get(i) instanceof VarInsnNode)
				 pw.println("         " + ((VarInsnNode)mnode.instructions.get(i)).var);

			*//*if (mnode.instructions.get(i).toString().equals("org.objectweb.asm.tree.MethodInsnNode@4b20ca2b")) {
				n = i;
				break;
			}*//*
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
