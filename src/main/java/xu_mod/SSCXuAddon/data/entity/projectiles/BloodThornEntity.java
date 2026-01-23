package xu_mod.SSCXuAddon.data.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.init.Init_Entity;
import xu_mod.SSCXuAddon.utils.Interface.IProjectileEX;

public class BloodThornEntity extends PersistentProjectileEntity {
    public float damage = 5.0f;
    public float recoveryOwnerHPPercent = 0.1f;  // 回复魔力中的10%
    public float recoveryOwnerManaPercent = 8f;  // 造成伤害的10倍

    public static int recoveryManaTime = 20;

    public void setDamage(float damage, float recoveryOwnerHPPercent, float recoveryOwnerManaPercent) {
        this.damage = damage;
        this.recoveryOwnerHPPercent = recoveryOwnerHPPercent;
        this.recoveryOwnerManaPercent = recoveryOwnerManaPercent;
    }

    public BloodThornEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        if (this instanceof IProjectileEX iProjectileEX) {
            iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
        }
    }

    protected BloodThornEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
        this.setNoGravity(true);
        if (this instanceof IProjectileEX iProjectileEX) {
            iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
        }
    }

    public BloodThornEntity(EntityType<? extends PersistentProjectileEntity> type, PlayerEntity owner, World world) {
        super(type, owner, world);
        this.setNoGravity(true);
        if (this instanceof IProjectileEX iProjectileEX) {
            iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
        }
    }

    public BloodThornEntity(PlayerEntity owner, float speed, Vec3d PositionOffset) {
        super(Init_Entity.BLOOD_THORN, owner, owner.getWorld());
        this.setNoGravity(true);
        this.setPos(owner.getX() + PositionOffset.getX(), owner.getY() + owner.getEyeHeight(owner.getPose()) + PositionOffset.getY(), owner.getZ() + PositionOffset.getZ());
        this.setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, speed, 0.0F);
        if (this instanceof IProjectileEX iProjectileEX) {
            iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
        }
    }

    public BloodThornEntity(PlayerEntity owner, float speed, Vec3d PositionOffset, Vec3d TargetPos) {
        super(Init_Entity.BLOOD_THORN, owner, owner.getWorld());
        this.setNoGravity(true);
        this.setPos(owner.getX() + PositionOffset.getX(), owner.getY() + owner.getEyeHeight(owner.getPose()) + PositionOffset.getY(), owner.getZ() + PositionOffset.getZ());
        double vx,vy,vz,ox,oy,oz;
        ox = TargetPos.getX() - this.getX();
        oy = TargetPos.getY() - this.getY();
        oz = TargetPos.getZ() - this.getZ();
        double length = Math.sqrt(
                ox * ox + oy * oy + oz * oz
        );
        if (length < 1e-9f) {
            this.setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, speed, 0.0F);
            return;
        }
        vx = ox / length;
        vy = oy / length;
        vz = oz / length;
        this.setVelocity(vx, vy, vz, speed, 0.0f);
        if (this instanceof IProjectileEX iProjectileEX) {
            iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
        }
    }

    private void spawnParticles(int amount) {
        if (amount > 0) {
            for(int j = 0; j < amount; ++j) {
                this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX((double)0.5F), this.getRandomBodyY(), this.getParticleZ((double)0.5F), 0.65, 0, 0);
            }

        }
    }

    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.spawnParticles(2);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockState blockState = this.getWorld().getBlockState(blockHitResult.getBlockPos());
        blockState.onProjectileHit(this.getWorld(), blockState, blockHitResult, this);
        if (this.getWorld().isClient) {
            spawnParticles(4);
        }
        this.discard();
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource damageSource = entity.getDamageSources().generic();
        if (owner instanceof PlayerEntity player) {
            damageSource = owner.getDamageSources().playerAttack((PlayerEntity) player);
            player.onAttacking(entity);
        }
        if (entity.damage(damageSource, damage)) {
            if (entity instanceof LivingEntity livingEntity) {
                this.onHit(livingEntity);
                if (owner instanceof PlayerEntity player) {
                    player.heal(damage * recoveryOwnerHPPercent);
                    ManaUtils.gainPlayerManaWithTime(player, damage * recoveryOwnerManaPercent/ recoveryManaTime, recoveryManaTime);
                }
            }
        }
        if (this.getWorld().isClient) {
            spawnParticles(4);
        }
        this.discard();
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("blood_thorn_damage", this.damage);
        nbt.putFloat("recoveryOwnerHPPercent", this.recoveryOwnerHPPercent);
        nbt.putFloat("recoveryOwnerManaPercent", this.recoveryOwnerManaPercent);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.damage = nbt.getFloat("blood_thorn_damage");
        this.recoveryOwnerHPPercent = nbt.getFloat("recoveryOwnerHPPercent");
        this.recoveryOwnerManaPercent = nbt.getFloat("recoveryOwnerManaPercent");
    }
}
