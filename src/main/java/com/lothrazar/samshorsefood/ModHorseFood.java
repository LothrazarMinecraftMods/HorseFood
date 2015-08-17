package com.lothrazar.samshorsefood;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import org.apache.logging.log4j.Logger; 

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModHorseFood.MODID, useMetadata=true)
public class ModHorseFood
{
    public static final String MODID = "samshorsefood"; 
    @Instance(value = MODID)
	public static ModHorseFood instance;
	@SidedProxy(clientSide="com.lothrazar.samshorsefood.ClientProxy", serverSide="com.lothrazar.samshorsefood.CommonProxy")
	public static CommonProxy proxy; 
	public static Logger logger; 
	public static ConfigRegistry cfg; 
	public static IAttribute horseJumpStrength = null;
    @EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{  
		logger = event.getModLog();  
		
		ItemRegistry.registerItems();
		
		cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));

		
		//version 1.1.0
		//new item for speed
		//new item for jump
		
		// TODO: new config for recipe expense - surround or single
		//new config for speed/jump/health the max value of each. 
		
	    for (Field f : EntityHorse.class.getDeclaredFields()) 
	    {
	        try 
	        { 
            	//
	        	
	        	//if(f.get(null) instanceof IAttribute)
	        	System.out.println("__ "+f.getName());//then printdebug it
	        	
	            if (f.getName().equals("horseJumpStrength") || f.getName().equals("field_76425_a")) // TODO: this key
	            {
	            	f.setAccessible(true);
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
	    
	    
	    if(ModHorseFood.horseJumpStrength == null)
	    	System.err.println(MODID+":horseJumpStrength: Error - field not found using reflection");
	    
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		FMLCommonHandler.instance().bus().register(instance); 
		MinecraftForge.EVENT_BUS.register(instance); 
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
     
    @SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void addHorseInfo(RenderGameOverlayEvent.Text event )
	{ 
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 
    	if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
		 	if(player.ridingEntity != null && player.ridingEntity instanceof EntityHorse)
		 	{ 
		 		EntityHorse horse = (EntityHorse)player.ridingEntity;
		 
				double speed =  horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() ;
		
				double jump = horse.getHorseJumpStrength() ;
				//convert from scale factor to blocks
				double jumpHeight = 0;
				double gravity = 0.98;
				while (jump > 0)
				{
					jumpHeight += jump;
					jump -= 0.08;
					jump *= gravity;
				}
				
				DecimalFormat df = new DecimalFormat("0.0000");
				
				event.left.add(StatCollector.translateToLocal("debug.horsespeed")+"  "+ df.format(speed)   ); 
				
				df = new DecimalFormat("0.0");
				
				event.left.add(StatCollector.translateToLocal("debug.horsejump") +"  "+ df.format(jumpHeight) );
		 	}
	 	}  
	}
}
