package com.lothrazar.samshorsefood;

import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry; 

public class ItemRegistry 
{ 
	public static ArrayList<Item> items = new ArrayList<Item>();
 
	public static ItemHorseFood emeraldCarrot; 
	public static ItemHorseFood lapisCarrot; 
	public static ItemHorseFood diamondCarrot; 
	public static ItemHorseFood horse_upgrade_health;
	public static ItemHorseFood horse_upgrade_speed;
	public static void registerItems()
	{    
		emeraldCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(emeraldCarrot, "horse_upgrade_type");
		
		lapisCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(lapisCarrot, "horse_upgrade_variant");
		
		diamondCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(diamondCarrot, "horse_upgrade_health"); 
		
		horse_upgrade_speed = new ItemHorseFood();
		ItemRegistry.registerItem(horse_upgrade_speed, "horse_upgrade_speed"); 
		
		horse_upgrade_health = new ItemHorseFood();
		ItemRegistry.registerItem(horse_upgrade_health, "horse_upgrade_health"); 
		
		ItemHorseFood.addRecipes(); 
	}
	
	
	public static void registerItem(Item item, String name)
	{ 
		 item.setUnlocalizedName(name);
		 
		 GameRegistry.registerItem(item, name);
		 
		 items.add(item);
	}
}
