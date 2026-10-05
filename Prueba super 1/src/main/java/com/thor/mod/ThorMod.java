package com.thor.mod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thor.mod.ability.ModAbilities;
import com.thor.mod.client.ModClient;
import com.thor.mod.config.ModConfig;
import com.thor.mod.effect.ModEffects;
import com.thor.mod.entity.ModEntities;
import com.thor.mod.item.MjolnirItem;
import com.thor.mod.item.ModItems;
import com.thor.mod.network.ModNetwork;
import com.thor.mod.render.ModRender;
import com.thor.mod.sound.ModSounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod("thor_mod")
public class ThorMod {
   public static final String MOD_ID = "thor_mod";

   public ThorMod(IEventBus modEventBus) {
      modEventBus.addListener(this::commonSetup);
      ModItems.register(modEventBus);
      ModEntities.register(modEventBus);
      ModAbilities.register(modEventBus);
      ModEffects.register(modEventBus);
      ModSounds.register(modEventBus);
      ModNetwork.register(modEventBus);
      ModConfig.register(modEventBus);
      ModClient.register(modEventBus);
      ModRender.register(modEventBus);
      NeoForge.EVENT_BUS.addListener(this::onServerStarting);
      NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
   }

   private void onServerStarting(ServerStartingEvent event) {
   }

   private void onRegisterCommands(RegisterCommandsEvent event) {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      dispatcher.register((LiteralArgumentBuilder)Commands.literal("thor").then(Commands.literal("mjolnir").executes(context -> {
         if (((CommandSourceStack)context.getSource()).getEntity() instanceof ServerPlayer player) {
            player.getInventory().add(((MjolnirItem)ModItems.MJOLNIR.get()).getDefaultInstance());
            player.sendSystemMessage(Component.literal("You have received Mjolnir!"));
            return 1;
         } else {
            return 0;
         }
      })));
   }

   public static ResourceLocation modResourceLocation(String path) {
      return ResourceLocation.fromNamespaceAndPath("thor_mod", path);
   }
}
