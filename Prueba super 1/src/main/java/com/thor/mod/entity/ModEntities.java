package com.thor.mod.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
   public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "thor_mod");
   public static final DeferredHolder<EntityType<?>, EntityType<MjolnirEntity>> MJOLNIR = ENTITIES.register(
      "mjolnir", () -> Builder.<MjolnirEntity>of(MjolnirEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).fireImmune().build("mjolnir")
   );

   public static void register(IEventBus modEventBus) {
      ENTITIES.register(modEventBus);
   }
}
