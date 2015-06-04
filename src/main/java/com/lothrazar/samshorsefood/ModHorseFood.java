package com.lothrazar.samshorsefood;

import org.apache.logging.log4j.Logger; 

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModHorseFood.MODID, version = ModHorseFood.VERSION,	name = ModHorseFood.NAME, useMetadata = true)
public class ModHorseFood
{
    public static final String MODID = "samshorsefood";
    public static final String VERSION = "1.0";
    public static final String NAME = "Horse Upgrade Food";
    @Instance(value = MODID)
	public static ModHorseFood instance;
	public static Logger logger; 
    
    @EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 

		ItemRegistry.registerItems();
		
		logger = event.getModLog();  
		
		//cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));
	  
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
 
		FMLCommonHandler.instance().bus().register(instance); 
		MinecraftForge.EVENT_BUS.register(instance); 
 
   

    }
    
 
    
    
    @SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
  	{
    	ItemStack held = event.entityPlayer.getCurrentEquippedItem(); 
    	
    	if(held != null && held.getItem() instanceof ItemHorseFood)
		{     
			if(event.target instanceof EntityHorse)
			{ 
				ItemHorseFood.onHorseInteract((EntityHorse)event.target,event.entityPlayer,held);  
				
				event.setCanceled(true);//stop the GUI inventory opening
			}  
		}  
  	}
}
