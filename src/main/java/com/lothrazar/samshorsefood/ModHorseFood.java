package com.lothrazar.samshorsefood;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModHorseFood.MODID, useMetadata = true, updateJSON = "https://raw.githubusercontent.com/LothrazarMinecraftMods/HorseFood/master/update.json")
public class ModHorseFood{

	public static final String MODID = "samshorsefood";
	@Instance(value = MODID)
	public static ModHorseFood instance;
	@SidedProxy(clientSide = "com.lothrazar.samshorsefood.ClientProxy", serverSide = "com.lothrazar.samshorsefood.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;
	public static ConfigRegistry cfg;
	public static IAttribute horseJumpStrength = null;

	public static CreativeTabs tabHorseFood = new CreativeTabs("tabHorseFood") {

		@Override
		public Item getTabIconItem(){

			return ItemRegistry.diamondCarrot;
		}
	};

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){

		logger = event.getModLog();

		ItemRegistry.registerItems();

		cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));

		// version 1.1.0
		// new item for speed
		// new item for jump

		// TODO: new config for recipe expense - surround or single
		// new config for speed/jump/health the max value of each.
		/*
		 * __ field_110276_bu __ field_110276_bu [18:28:18] [Client thread/INFO]
		 * [STDOUT/samshorsefood]: [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __
		 * field_110271_bv [18:28:18] [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110270_bw [18:28:18]
		 * [Client thread/INFO] [STDERR/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:80]: Severe error, please report
		 * this to the mod author: [18:28:18] [Client thread/INFO] [STDERR/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:81]: java.lang.ClassCastException:
		 * [Ljava.lang.String; cannot be cast to net.minecraft.entity.ai.attributes.IAttribute
		 * [18:28:18] [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110273_bx [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110272_by [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110268_bz [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110269_bA [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110291_bB [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110292_bC [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110289_bD [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110290_bE
		 */
		for(Field f : EntityHorse.class.getDeclaredFields()){
			try{
				// if(f.get(null) instanceof IAttribute)
				// System.out.println("== "+f.getName()+" ** "+f.getType());

				// interface net.minecraft.entity.ai.attributes.IAttribute

				if(f.getName().equals("horseJumpStrength") || f.getName().equals("field_110270_bw") || "interface net.minecraft.entity.ai.attributes.IAttribute".equals(f.getType() + "")) // TODO:
																																															// old
																																															// 1.7
																																															// was
																																															// field_76425_a
				{
					f.setAccessible(true);
					// save pointer to the obj so we can reference it later
					ModHorseFood.horseJumpStrength = (IAttribute) f.get(null);
					// System.err.println("FOUND FOUND FOUND");
					break;
				}
			}
			catch (Exception e){
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}

		if(ModHorseFood.horseJumpStrength == null)
			System.err.println(MODID + ":horseJumpStrength: Error - field not found using reflection");
	}

	@EventHandler
	public void init(FMLInitializationEvent event){

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerRenderers();
	}

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event){

		if(event.getEntity() instanceof EntityPlayer == false){
			return;
		}
		EntityPlayer entityPlayer = (EntityPlayer)event.getEntity();
		ItemStack held = entityPlayer.getHeldItemMainhand();

		if(held != null && held.getItem() instanceof ItemHorseFood){
			if(event.getTarget() instanceof EntityHorse){
				ItemHorseFood.onHorseInteract((EntityHorse) event.getTarget(), entityPlayer, held);

				event.setCanceled(true);// stop the GUI inventory opening
			}
		}
	}

	static double getJumpTranslated(double jump){

		// double jump = horse.getHorseJumpStrength();
		// convert from scale factor to blocks
		double jumpHeight = 0;
		double gravity = 0.98;
		while (jump > 0){
			jumpHeight += jump;
			jump -= 0.08;
			jump *= gravity;
		}
		return jumpHeight;
	}

	static double getSpeedTranslated(double speed){

		return speed * 100;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void addHorseInfo(RenderGameOverlayEvent.Text event){

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo){
			if(player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityHorse){
				EntityHorse horse = (EntityHorse) player.getRidingEntity();

				double speed = getSpeedTranslated(horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());

				// double jump = horse.getHorseJumpStrength() ;
				// convert from scale factor to blocks
				double jumpHeight = getJumpTranslated(horse.getHorseJumpStrength());

				DecimalFormat df = new DecimalFormat("0.00");
  
				event.getLeft().add(I18n.translateToLocal("debug.horsespeed") + "  " + df.format(speed));

				df = new DecimalFormat("0.0");

				event.getLeft().add(I18n.translateToLocal("debug.horsejump") + "  " + df.format(jumpHeight));
			}
		}
	}
}
