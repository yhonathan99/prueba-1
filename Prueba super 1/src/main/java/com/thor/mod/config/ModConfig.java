package com.thor.mod.config;

import net.neoforged.bus.api.IEventBus;

public class ModConfig {
   public static final ModConfig.Common COMMON = new ModConfig.Common();

   public static void register(IEventBus modEventBus) {
   }

   public static class Common {
      public double mjolnirDamage = 8.0;
      public double throwSpeed = 1.5;
      public double returnSpeed = 2.0;
      public double maxDistance = 30.0;
      public int returnDelay = 20;
   }
}
