package com.lothrazar.samshorsefood;

import org.apache.logging.log4j.Logger; 


import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModHorseFood.MODID, useMetadata=true)
public class ModHorseFood
{
    public static final String MODID = "samshorsefood"; 
    @Instance(value = MODID)
	public static ModHorseFood instance;
	public static Logger logger; 
	@SidedProxy(clientSide="com.lothrazar.samshorsefood.ClientProxy", serverSide="com.lothrazar.samshorsefood.CommonProxy")
	public static CommonProxy proxy; 
    @EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{  
		ItemRegistry.registerItems();
		
		logger = event.getModLog();  
		
		//cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));

		FMLCommonHandler.instance().bus().register(instance); 
		MinecraftForge.EVENT_BUS.register(instance); 
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
 
 
   

		proxy.registerRenderers();
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
