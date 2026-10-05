package com.thor.mod.item;

import com.thor.mod.config.ModConfig;
import com.thor.mod.entity.MjolnirEntity;
import com.thor.mod.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MjolnirItem extends SwordItem {
   public MjolnirItem() {
      super(Tiers.NETHERITE, new Properties());
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!level.isClientSide) {
         MjolnirEntity mjolnir = new MjolnirEntity((EntityType<? extends MjolnirEntity>)ModEntities.MJOLNIR.get(), player, level);
         Vec3 lookVec = player.getLookAngle();
         double throwSpeed = ModConfig.COMMON.throwSpeed;
         mjolnir.setDeltaMovement(lookVec.scale(throwSpeed));
         mjolnir.setPos(player.getX(), player.getEyeY(), player.getZ());
         level.addFreshEntity(mjolnir);
         player.setItemInHand(hand, ItemStack.EMPTY);
         level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
      }

      return InteractionResultHolder.success(stack);
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (!attacker.level().isClientSide) {
         for (int i = 0; i < 5; i++) {
            double offsetX = (attacker.level().random.nextDouble() - 0.5) * 0.5;
            double offsetY = (attacker.level().random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (attacker.level().random.nextDouble() - 0.5) * 0.5;
            attacker.level()
               .addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX() + offsetX, target.getY() + offsetY + 1.0, target.getZ() + offsetZ, 0.0, 0.0, 0.0);
         }

         attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5F, 1.0F);
      }

      return super.hurtEnemy(stack, target, attacker);
   }
}
