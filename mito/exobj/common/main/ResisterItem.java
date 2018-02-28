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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

	static public void preinit(FMLPreInitializationEvent event) {
		//ItemFakeBlock = new ItemFakeBlock().setUnlocalizedName("ItemFakeBlock");
		ItemSelectTool = new ItemSelectTool().setUnlocalizedName("ItemSelectTool").setCreativeTab(Main.tab);
		ItemBrace = new ItemBrace().setUnlocalizedName("ItemBrace").setCreativeTab(Main.tab);
		ItemBender = new ItemBender().setUnlocalizedName("ItemBender").setCreativeTab(Main.tab);
		ItemBar = new ItemBar().setUnlocalizedName("ItemBar");
		//ItemBlockSetter = new ItemBlockSetter().setUnlocalizedName("ItemBlockSetter").setCreativeTab(Main.tab);
		//ItemWall = new ItemWall().setUnlocalizedName("ItemWall").setCreativeTab(mitomain.tab);
		ItemRuler = new ItemRuler().setUnlocalizedName("ItemRuler").setCreativeTab(Main.tab);
		ItemTofu = new ItemTofu().setUnlocalizedName("ItemTofu").setCreativeTab(Main.tab);

		GameRegistry.register(ItemBar, new ResourceLocation(Main.MODID, "ItemBar"));
		GameRegistry.register(ItemBender, new ResourceLocation(Main.MODID, "ItemBender"));
		GameRegistry.register(ItemRuler, new ResourceLocation(Main.MODID, "ItemRuler"));
		GameRegistry.register(ItemSelectTool, new ResourceLocation(Main.MODID, "ItemSelectTool"));
		GameRegistry.register(ItemBrace, new ResourceLocation(Main.MODID, "ItemBrace"));
		GameRegistry.register(ItemTofu, new ResourceLocation(Main.MODID, "ItemTofu"));

		if(event.getSide().isClient()){
			ModelLoader.setCustomModelResourceLocation(ItemBar, 0, new ModelResourceLocation(ItemBar.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(ItemBender, 0, new ModelResourceLocation(ItemBender.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(ItemRuler, 0, new ModelResourceLocation(ItemRuler.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(ItemSelectTool, 0, new ModelResourceLocation(ItemSelectTool.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(ItemBrace, 0, new ModelResourceLocation(ItemBrace.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(ItemTofu, 0, new ModelResourceLocation(ItemTofu.getRegistryName(), "inventory"));
		}
	}

	static public void RegisterRecipe() {

		RecipeSorter.register(Main.MODID + ";shapeless", MitoShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

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
