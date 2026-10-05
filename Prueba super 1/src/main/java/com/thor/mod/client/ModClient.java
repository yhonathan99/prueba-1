package com.thor.mod.client;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class ModClient {
   public static KeyMapping THROW_MJOLNIR_KEY;

   public static void register(IEventBus modEventBus) {
      modEventBus.addListener(ModClient::registerKeyMappings);
   }

   private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
      THROW_MJOLNIR_KEY = new KeyMapping("key.thor.throw_mjolnir", Type.KEYSYM, 82, "key.categories.thor");
      event.register(THROW_MJOLNIR_KEY);
   }
}
