package com.mito.exobj.common.block;

import java.util.List;

import com.mito.exobj.BraceBase.BB_GroupBase;
import com.mito.exobj.common.Main;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockObjects extends BlockContainer {

	public BlockObjects() {
		super(Material.CLOTH);/*
		this.setSoundType(Block.soundTypeCloth);
		this.bou(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);*/
		// this.setBlockTextureName("iron_ore");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int a) {
		TileObjects tile = new TileObjects();
		//tile.name = new BB_GroupBase(world, new Vec3d(1.0, 1.0, 1.0));
		return tile;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float hitX,
									float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			BB_GroupBase go = ((TileObjects) tile).name;
			if (go != null) {
			} else {
			}
		} else {
		}
		return true;
	}

	public int getRenderType() {
		return Main.RenderType_Objects;
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int dir) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null && tile instanceof TileObjects) {
			TileObjects t = (TileObjects) tile;
			t.breakBrace();
		}
		world.removeTileEntity(new BlockPos(x, y, z));
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask,
										List<AxisAlignedBB> list, Entity collidingEntity) {
	}

}
