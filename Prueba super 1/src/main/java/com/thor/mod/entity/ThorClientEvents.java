package com.thor.mod.entity;

import com.thor.mod.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "thor_mod", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ThorClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MJOLNIR.get(), MjolnirEntityRenderer::new);
    }
}
