package com.thor.mod.entity;

import com.thor.mod.config.ModConfig;
import com.thor.mod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MjolnirEntity extends Projectile {
   private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(MjolnirEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(MjolnirEntity.class, EntityDataSerializers.INT);
   private int returnTimer = 0;

   public MjolnirEntity(EntityType<? extends MjolnirEntity> type, Level level) {
      super(type, level);
   }

   public MjolnirEntity(EntityType<? extends MjolnirEntity> type, LivingEntity owner, Level level) {
      super(type, level);
      this.setOwner(owner);
      this.entityData.set(OWNER_ID, owner.getId());
   }

   protected void defineSynchedData(Builder builder) {
      builder.define(RETURNING, false);
      builder.define(OWNER_ID, 0);
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.spawnParticles();
      }

      if (!(Boolean)this.entityData.get(RETURNING)) {
         HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
         if (hit.getType() != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hit);
         }
      }

      Vec3 motion = this.getDeltaMovement();
      this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
      if (!this.isNoGravity()) {
         this.setDeltaMovement(motion.scale(0.99).add(0.0, -0.03, 0.0));
      }

      LivingEntity owner = this.getOwner();
      if (owner != null && owner.isAlive()) {
         if (!(Boolean)this.entityData.get(RETURNING)) {
            this.returnTimer++;
            double maxDistance = ModConfig.COMMON.maxDistance;
            int returnDelay = ModConfig.COMMON.returnDelay;
            if (this.returnTimer >= returnDelay || this.distanceToSqr(owner) > maxDistance * maxDistance) {
               this.startReturning();
            }
         } else {
            this.returnToOwner(owner);
         }
      } else if (!this.level().isClientSide) {
         this.discard();
      }
   }

   private void startReturning() {
      this.entityData.set(RETURNING, true);
      this.setNoGravity(true);
      this.noPhysics = true;
   }

   private void returnToOwner(LivingEntity owner) {
      Vec3 ownerPos = owner.getEyePosition();
      Vec3 currentPos = this.position();
      Vec3 direction = ownerPos.subtract(currentPos).normalize();
      double returnSpeed = ModConfig.COMMON.returnSpeed;
      this.setDeltaMovement(direction.scale(returnSpeed));
      if (this.distanceToSqr(ownerPos) < 4.0) {
         this.returnToHand(owner);
      }
   }

   private void returnToHand(LivingEntity owner) {
      if (!this.level().isClientSide) {
         ItemStack stack = new ItemStack(ModItems.MJOLNIR.get());
         if (!(owner instanceof Player player) || !player.getInventory().add(stack)) {
            owner.spawnAtLocation(stack);
         }
         this.discard();
      }
   }

   protected void onHitEntity(EntityHitResult result) {
      super.onHitEntity(result);
      Entity entity = result.getEntity();
      if (entity instanceof LivingEntity) {
         double damage = ModConfig.COMMON.mjolnirDamage;
         entity.hurt(this.level().damageSources().thrown(this, this.getOwner()), (float)damage);
      }

      this.startReturning();
   }

   protected void onHitBlock(BlockHitResult result) {
      super.onHitBlock(result);
      this.startReturning();
   }

   protected void onHit(HitResult result) {
      if (!this.level().isClientSide && !(Boolean)this.entityData.get(RETURNING)) {
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
         if (bolt != null) {
            bolt.moveTo(result.getLocation());
            this.level().addFreshEntity(bolt);
         }
      }

      super.onHit(result);
      this.startReturning();
   }

   private void spawnParticles() {
      for (int i = 0; i < 2; i++) {
         double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
         double offsetY = (this.random.nextDouble() - 0.5) * 0.5;
         double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
         this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0.0, 0.0, 0.0);
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Returning", (Boolean)this.entityData.get(RETURNING));
      compound.putInt("OwnerId", (Integer)this.entityData.get(OWNER_ID));
      compound.putInt("ReturnTimer", this.returnTimer);
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(RETURNING, compound.getBoolean("Returning"));
      this.entityData.set(OWNER_ID, compound.getInt("OwnerId"));
      this.returnTimer = compound.getInt("ReturnTimer");
   }

   public LivingEntity getOwner() {
      int ownerId = (Integer)this.entityData.get(OWNER_ID);
      if (ownerId == 0) {
         return null;
      } else {
         Entity entity = this.level().getEntity(ownerId);
         return entity instanceof LivingEntity ? (LivingEntity)entity : null;
      }
   }
}
