package com.thor.mod.entity;

import com.thor.mod.client.ModClient;
import com.thor.mod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = "thor_mod", value = Dist.CLIENT)
public class ThorKeyEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (ModClient.THROW_MJOLNIR_KEY == null || mc.player == null || mc.gameMode == null) {
            return;
        }
        while (ModClient.THROW_MJOLNIR_KEY.consumeClick()) {
            for (InteractionHand hand : InteractionHand.values()) {
                if (mc.player.getItemInHand(hand).is(ModItems.MJOLNIR.get())) {
                    mc.gameMode.useItem(mc.player, hand);
                    break;
                }
            }
        }
    }
}
