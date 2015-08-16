package com.lothrazar.samshorsefood;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Logger; 





import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
	
	public static IAttribute horseJumpStrength = null;
    @EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{  
		ItemRegistry.registerItems();
		
		logger = event.getModLog();  
		
		//cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));

		FMLCommonHandler.instance().bus().register(instance); 
		MinecraftForge.EVENT_BUS.register(instance); 
		
		//version 1.1.0
		//new item for speed
		//new item for jump
		
		// TODO: new config for recipe expense - surround or single
		//new config for speed/jump/health the max value of each. 
		
	    for (Field f : EntityHorse.class.getDeclaredFields()) 
	    {
	     
	        try 
	        { 
            	f.setAccessible(true);
	        	
	        	if(f.get(null) instanceof IAttribute)
	        		System.out.println("__ "+f.getName());//then printdebug it
	        	
	            if (f.getName().equals("horseJumpStrength") || f.getName().equals("field_76425_a")) // TODO: this key
	            {
	            	//save pointer to the obj so we can reference it later
	            	ModHorseFood.horseJumpStrength = (IAttribute)f.get(null);
	            }
	        }
	        catch (Exception e) 
	        {
	            System.err.println("Severe error, please report this to the mod author:");
	            System.err.println(e);
	        }
	    }
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
