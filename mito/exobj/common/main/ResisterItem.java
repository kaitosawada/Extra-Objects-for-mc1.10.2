package com.mito.exobj.common.main;

import com.mito.exobj.common.Main;
import com.mito.exobj.common.MitoShapedRecipe;
import com.mito.exobj.common.MitoShapelessRecipe;
import com.mito.exobj.common.item.ItemBar;
import com.mito.exobj.common.item.ItemBender;
import com.mito.exobj.common.item.ItemBrace;
import com.mito.exobj.common.item.ItemRuler;
import com.mito.exobj.common.item.ItemSelectTool;
import com.mito.exobj.common.item.ItemTofu;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

public class ResisterItem {

	public static Item ItemBrace;
	public static Item ItemBar;
	public static Item ItemBender;
	//public static Item ItemWall;
	public static Item ItemRuler;
	//public static Item ItemLinearMotor;
	public static Item ItemBlockSetter;
	public static Item ItemSelectTool;
	public static Item ItemFakeBlock;
	//public static Item ItemRedCable;
	public static Item ItemTofu;

	public static Block BlockObjects;

	static public void preinit() {
		//ItemFakeBlock = new ItemFakeBlock().setUnlocalizedName("ItemFakeBlock");
		ItemSelectTool = new ItemSelectTool().setUnlocalizedName("ItemSelectTool").setCreativeTab(Main.tab);
		ItemBrace = new ItemBrace().setUnlocalizedName("ItemBrace").setCreativeTab(Main.tab);
		ItemBender = new ItemBender().setUnlocalizedName("ItemBender").setCreativeTab(Main.tab);
		ItemBar = new ItemBar().setUnlocalizedName("ItemBar");
		//ItemBlockSetter = new ItemBlockSetter().setUnlocalizedName("ItemBlockSetter").setCreativeTab(Main.tab);
		//ItemWall = new ItemWall().setUnlocalizedName("ItemWall").setCreativeTab(mitomain.tab);
		ItemRuler = new ItemRuler().setUnlocalizedName("ItemRuler").setCreativeTab(Main.tab);
		ItemTofu = new ItemTofu().setUnlocalizedName("ItemTofu").setCreativeTab(Main.tab);

		//BlockObjects = new BlockObjects();

		GameRegistry.registerItem(ItemBar, "ItemBar");
		GameRegistry.registerItem(ItemBender, "ItemBender");
		//GameRegistry.registerItem(ItemWall, "ItemWall");
		GameRegistry.registerItem(ItemRuler, "ItemRuler");
		//GameRegistry.registerItem(ItemBlockSetter, "ItemBlockSetter");
		GameRegistry.registerItem(ItemSelectTool, "ItemSelectTool");
		GameRegistry.registerItem(ItemBrace, "ItemBrace");
		/*GameRegistry.registerItem(ItemFakeBlock, "ItemFakeBlock");
		GameRegistry.registerItem(ItemLinearMotor, "ItemLinearMotor");
		GameRegistry.registerItem(ItemRedCable, "ItemRedCable");*/
		GameRegistry.registerItem(ItemTofu, "ItemTofu");

		//GameRegistry.registerBlock(BlockObjects, "BlockObjects");
	}

	static public void RegisterRecipe() {

		RecipeSorter.register("exobj;shapeless", MitoShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

		GameRegistry.addRecipe(new MitoShapedRecipe());
		GameRegistry.addRecipe(new MitoShapelessRecipe());

		GameRegistry.addRecipe(new ItemStack(ResisterItem.ItemBrace, 4, 0),
				"#  ",
				" # ",
				"  #",
				'#', Blocks.IRON_BARS);

		GameRegistry.addRecipe(new ItemStack(ResisterItem.ItemBender),
				" # ",
				" # ",
				"B B",
				'#', Items.IRON_INGOT,
				'B', ResisterItem.ItemBar);

		GameRegistry.addRecipe(new ItemStack(ResisterItem.ItemBar),
				"I  ",
				" I ",
				"  B",
				'B', Blocks.IRON_BARS,
				'I', Items.IRON_INGOT);
	}

}
