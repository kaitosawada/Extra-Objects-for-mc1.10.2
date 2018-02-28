package com.mito.exobj.common.main;

import com.mito.exobj.client.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mito.exobj.BraceBase.BB_RenderHandler;
import com.mito.exobj.BraceBase.ExtraObject;
import com.mito.exobj.client.render.exorender.BB_TypeResister;
import com.mito.exobj.common.Main;
import com.mito.exobj.common.main.mitoCommonProxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class mitoClientProxy extends mitoCommonProxy {

	public BraceHighLightHandler bh;
	ScrollWheelHandler wh;
	BB_RenderHandler rh;
	private BB_KeyBinding key_ctrl;
	private BB_KeyBinding key_alt;
	private BB_KeyBinding key_shift;

	public mitoClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public BB_Key getKey() {
		return new BB_Key(this.isControlKeyDown(), this.isShiftKeyDown(), this.isAltKeyDown());
	}

	public boolean isControlKeyDown() {
		return Keyboard.isKeyDown(key_ctrl.overrideKeyCode);
	}

	public boolean haswheel() {
		return Mouse.hasWheel();
	}

	public int getwheel() {
		return Mouse.getDWheel();
	}

	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(key_shift.overrideKeyCode);
	}

	public boolean isAltKeyDown() {
		return Keyboard.isKeyDown(key_alt.overrideKeyCode);
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void preInit() {
		resisterEvent();
		this.sg = new BB_SelectedGroup(this);
	}

	public void resisterEvent() {
		this.bh = new BraceHighLightHandler(this);
		MinecraftForge.EVENT_BUS.register(bh);
		FMLCommonHandler.instance().bus().register(bh);

		this.wh = new ScrollWheelHandler(this);
		MinecraftForge.EVENT_BUS.register(wh);
		FMLCommonHandler.instance().bus().register(wh);

		this.rh = new BB_RenderHandler();
		MinecraftForge.EVENT_BUS.register(rh);
		FMLCommonHandler.instance().bus().register(rh);
	}

	@Override
	public void init() {
		BB_TypeResister.loadModels();
		//entity render resist


		//ClientRegistry.registerTileEntity(TileObjects.class, "TileObjects", new TileObjectsRenderer());

		//item renderer


		//key

		this.key_shift = new BB_KeyBinding("Snap Parallel Key", Keyboard.KEY_LSHIFT, Main.MODNAME);
		this.key_alt = new BB_KeyBinding("Air Key", Keyboard.KEY_LMENU, Main.MODNAME);
		this.key_ctrl = new BB_KeyBinding("Off Snap Key", Keyboard.KEY_LCONTROL, Main.MODNAME);
	}

	public void upkey(int count) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (player != null && !mc.isGamePaused() && mc.inGameHasFocus && mc.currentScreen == null) {
			//蜃ｦ逅�縺ｨ縺�
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void playSound(SoundEvent se, SoundCategory sc, float vol, float pitch, float x, float y, float z) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(se, sc, vol, pitch, new BlockPos(x, y, z)));
	}

	@Override
	public void addDiggingEffect(World world, Vec3d center, double d0, double d1, double d2, Block block, int color) {
		/*Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, d0, d1, d2, d0 - center.xCoord, d1 - center.yCoord, d2 - center.zCoord, block, color))
				.applyColourMultiplier((int) center.xCoord, (int) center.yCoord, (int) center.zCoord));
	*/
	}

	public void particle(ExtraObject brace) {
		brace.particle();
	}

	/*@Override
	public void setBrightness(CreateVertexBufferObject c, Vec3d pos) {
		Vec3d pos1 = coord.add(pos);
		if (Main.quality) {
			World world = getClientWorld();
			BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(MathHelper.floor_double(pos1.xCoord), 0, MathHelper.floor_double(pos1.zCoord));

			if (world.isBlockLoaded(blockPos)) {
				blockPos.setY(MathHelper.floor_double(pos1.zCoord));
				c.setBrightness(world.getCombinedLight(blockPos, 0));
			}
		}
	}

	private Vec3d coord;

	@Override
	public void setCoord(Vec3d pos) {
		coord = pos;
	}*/
}
