package com.thor.mod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public class ModItems {
   public static final Items ITEMS = DeferredRegister.createItems("thor_mod");
   public static final DeferredHolder<Item, MjolnirItem> MJOLNIR = ITEMS.register("mjolnir", MjolnirItem::new);

   public static void register(IEventBus modEventBus) {
      ITEMS.register(modEventBus);
   }
}
