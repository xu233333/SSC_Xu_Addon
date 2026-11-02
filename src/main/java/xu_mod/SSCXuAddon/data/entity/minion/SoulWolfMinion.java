package xu_mod.SSCXuAddon.data.entity.minion;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.minion.IMinion;
import net.onixary.shapeShifterCurseFabric.minion.IPlayerEntityMinion;
import net.onixary.shapeShifterCurseFabric.perk.PerkUtils;
import net.onixary.shapeShifterCurseFabric.util.Accessory.AccessoryUtils;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_Item;
import xu_mod.SSCXuAddon.init.Init_Perk;

import java.util.List;
import java.util.UUID;

public class SoulWolfMinion extends WolfEntity implements IMinion<SoulWolfMinion> {
    public static final Identifier MinionID = SSCXuAddon.identifier("soul_wolf_minion");

    private static final TrackedData<Integer> LEVEL = DataTracker.registerData(SoulWolfMinion.class, TrackedDataHandlerRegistry.INTEGER);

    public int level = 1;
    public int lifeTicks = 1200;
    public int dodgeCooldown = 0;
    public double cachedSoul = 0.0d;
    public boolean isAccessoryWolf = false;

    private int soulUpdateTimer = 0;
    private int healTimer = 0;
    private boolean naturalDeath = false;

    public SoulWolfMinion(EntityType<? extends SoulWolfMinion> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        PassiveData data;
        if (entityData instanceof PassiveData passiveData) {
            passiveData.babyAllowed = false;
            data = passiveData;
        } else {
            data = new PassiveData(false);
        }
        return super.initialize(world, difficulty, spawnReason, data, entityNbt);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new WolfEscapeDangerGoal(1.5));
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, false, this::isTargetNearOwner));
    }

    public static DefaultAttributeContainer.Builder createSoulWolfAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.applyLevelAttributes(false);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LEVEL, 1);
    }

    public int getLevel() {
        return this.dataTracker.get(LEVEL);
    }

    public void setLevel(int level) {
        int clamped = Math.max(1, Math.min(level, 4));
        this.level = clamped;
        this.dataTracker.set(LEVEL, clamped);
    }

    public void setMinionLevel(int level) {
        this.setLevel(level);
        this.applyLevelAttributes(true);
        this.updateSoulAttributes();
    }

    public void setAccessoryWolf(boolean value) {
        this.isAccessoryWolf = value;
        this.lifeTicks = this.getTotalLifeTicks();
        this.updateSoulAttributes();
    }

    public int getTotalLifeTicks() {
        if (this.isAccessoryWolf) {
            return 2400 + (this.level - 1) * 600;
        }
        return 1200 + (this.level - 1) * 300;
    }

    @Override
    public void InitMinion(PlayerEntity player) {
        if (player instanceof IPlayerEntityMinion iPlayerEntityMinion) {
            iPlayerEntityMinion.shape_shifter_curse$addMinion(this);
        } else {
            SSCXuAddon.LOGGER.error("PlayerEntity is not IPlayerEntityMinion, It Shouldn't Happen!");
            this.setHealth(0.0f);
            return;
        }
    }

    @Override
    public UUID getMinionOwnerUUID() {
        return super.getOwnerUuid();
    }

    @Override
    public void setMinionOwnerUUID(UUID uuid) {
        this.setOwnerUuid(uuid);
    }

    @Override
    public void setOwner(PlayerEntity player) {
        super.setOwner(player);
    }

    @Override
    public Identifier getMinionTypeID() {
        return MinionID;
    }

    @Override
    public SoulWolfMinion getSelf() {
        return this;
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        StatusEffect statusEffect = effect.getEffectType();
        return statusEffect != StatusEffects.REGENERATION && statusEffect != StatusEffects.POISON;
    }

    protected int getMaxLevel(PlayerEntity owner) {
        if (owner == null) {
            return 3;
        }
        List<Identifier> perks = PerkUtils.getPlayerPerks(owner, Init_Perk.T_AnubisWolfPerkTree);
        if (perks == null) {
            return 3;
        }
        if (perks.contains(Init_Perk.P_AnubisWolfSoulWolf4)) {
            return 4;
        }
        return 3;
    }

    protected int getKillRefund(PlayerEntity owner, int level) {
        return 0;
    }

    private void applyLevelAttributes(boolean fullHeal) {
        EntityAttributeInstance health = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (health == null) {
            SSCXuAddon.LOGGER.error("soul wolf health attribute missing");
            return;
        }
        double maxHealth = switch (this.level) {
            case 2 -> 15.0d;
            case 3 -> 18.0d;
            case 4 -> 20.0d;
            default -> 12.0d;
        };
        health.setBaseValue(maxHealth);
        if (fullHeal || this.getHealth() > (float) maxHealth) {
            this.setHealth((float) maxHealth);
        }
    }

    private double getEffectiveSoul() {
        double cap = this.level * 4.0d;
        return Math.max(0.0d, Math.min(this.cachedSoul, cap));
    }

    private void updateSoulAttributes() {
        PlayerEntity owner = this.getOwner();
        if (owner == null) {
            this.cachedSoul = 0.0d;
        } else if (this.isAccessoryWolf) {
            this.cachedSoul = this.level * 4.0d;
        } else {
            this.cachedSoul = ManaUtils.getPlayerMana(owner) - 6.0d;
        }
        double effective = this.getEffectiveSoul();

        EntityAttributeInstance armor = this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (armor != null) {
            armor.setBaseValue(Math.min(12.0d, this.level * 2.0d + effective * 0.25d));
        }

        EntityAttributeInstance attack = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attack != null) {
            double base = this.isAccessoryWolf ? 6.0d : 5.0d;
            attack.setBaseValue(base + effective * 0.25d);
        }

        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.3d * (1.0d + effective * 0.01d));
        }
    }

    public void levelUp(boolean paid) {
        PlayerEntity owner = this.getOwner();
        if (owner == null) {
            return;
        }
        int maxLevel = this.getMaxLevel(owner);
        if (this.level >= maxLevel) {
            return;
        }
        if (paid) {
            if (ManaUtils.getPlayerMana(owner) - 6.0d < 1.0d) {
                return;
            }
            ManaUtils.consumePlayerMana(owner, 1.0d);
        }
        this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 0.6f, 1.2f);
        this.setLevel(this.level + 1);
        this.lifeTicks = this.getTotalLifeTicks();
        this.applyLevelAttributes(paid);
        this.updateSoulAttributes();
    }

    // 抢头有点严重 给个补偿吧 但远程依然拿不到
    public void onKillEntity(LivingEntity target) {
        PlayerEntity owner = this.getOwner();
        if (owner == null) {
            return;
        }
        List<Identifier> perks = PerkUtils.getPlayerPerks(owner, Init_Perk.T_AnubisWolfPerkTree);
        if (perks == null) {
            return;
        }
        int maxLevel = this.getMaxLevel(owner);
        boolean canUpgrade = this.level < maxLevel;

        boolean upgraded = false;
        if (canUpgrade && perks.contains(Init_Perk.P_AnubisWolfSoulEcho)) {
            if (this.random.nextInt(100) < 20) {
                this.levelUp(false);
                upgraded = true;
            }
        }
        if (canUpgrade && this.isAccessoryWolf && !upgraded) {
            if (this.random.nextInt(100) < 10) {
                this.levelUp(false);
                upgraded = true;
            }
        }
        if (upgraded) {
            return;
        }
        double gain;
        if (this.isAccessoryWolf) {
            gain = 0.6d;
        } else {
            double squared = this.squaredDistanceTo(owner);
            if (squared > 25.0d) {
                return;
            }
            double distance = Math.sqrt(squared);
            double factor = ((5.0d - distance) / 5.0d) * 0.5d + 0.5d;
            factor = Math.max(0.5d, Math.min(1.0d, factor));
            gain = 0.6d * factor;
        }
        ManaUtils.gainPlayerMana(owner, gain);
    }

    @Override
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        float afterArmor = super.modifyAppliedDamage(source, amount);
        if (!this.getWorld().isClient && this.dodgeCooldown <= 0 && afterArmor > 0.0f) {
            float health = this.getHealth();
            float threshold = health * 0.25f;
            if (afterArmor > threshold) {
                this.triggerDodge();
                return threshold + (afterArmor - threshold) * 0.25f;
            }
        }
        return afterArmor;
    }

    private void triggerDodge() {
        this.dodgeCooldown = 200;
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 80, 0, false, false));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 0, false, false));
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 80, 0, false, false));

        PlayerEntity owner = this.getOwner();
        boolean ownerValid = owner != null && !owner.isDead() && this.squaredDistanceTo(owner) <= 16.0d * 16.0d;
        for (MobEntity mob : this.getWorld().getEntitiesByClass(MobEntity.class, this.getBoundingBox().expand(16.0d), m -> true)) {
            if (mob.getTarget() == this) {
                mob.setTarget(ownerValid ? owner : null);
                if (!ownerValid) {
                    mob.targetSelector.getGoals().forEach(PrioritizedGoal::stop);
                    mob.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
                }
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (success && !this.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            double perSoul = this.isAccessoryWolf ? 0.15d : 0.1d;
            double heal = this.getEffectiveSoul() * perSoul;
            if (heal > 0.0d) {
                this.heal((float) heal);
            }
        }
        // 凑活整个抑制爆炸吧 不同步到客户端了
        if (target instanceof CreeperEntity creeper) {
            creeper.currentFuseTime = 0;
        }
        return success;
    }

    @Override
    public void applyDamageEffects(LivingEntity attacker, Entity target) {
        if (attacker instanceof SoulWolfMinion minion && target instanceof LivingEntity livingEntity) {
            if (!minion.hasStatusEffect(StatusEffects.INVISIBILITY)) {
                int amplifier = (int) Math.min(4.0d, Math.floor(minion.getEffectiveSoul() / 4.0d));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 65, amplifier));
            }
        }
        super.applyDamageEffects(attacker, target);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }
        if (!this.isOwner(player)) {
            return ActionResult.PASS;
        }
        this.levelUp(true);
        return ActionResult.SUCCESS;
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.isAlive()) {
            if (!this.shouldExist()) {
                this.naturalDeath = true;
                this.kill();
            } else {
                if (this.dodgeCooldown > 0) {
                    this.dodgeCooldown--;
                }
                if (this.lifeTicks > 0) {
                    PlayerEntity owner = this.getOwner();
                    int decay = (owner == null) ? 10 : 1;
                    this.lifeTicks -= decay;
                    if (this.lifeTicks <= 0) {
                        if (owner != null && !this.isAccessoryWolf) {
                            ManaUtils.gainPlayerMana(owner, this.level);
                        }
                        this.naturalDeath = true;
                        this.kill();
                    }
                }
                if (this.isAccessoryWolf) {
                    this.healTimer++;
                    if (this.healTimer >= 40) {
                        this.healTimer = 0;
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(1.0f);
                        }
                    }
                }
                this.soulUpdateTimer++;
                if (this.soulUpdateTimer >= 20) {
                    this.soulUpdateTimer = 0;
                    this.updateSoulAttributes();
                }
            }
        }
        super.tick();
    }

    public boolean shouldExist() {
        if (this.getWorld().isClient) {
            return true;
        }
        if (this.getMinionOwnerUUID() == null) {
            return false;
        }
        PlayerEntity owner = this.getWorld().getPlayerByUuid(this.getMinionOwnerUUID());
        if (owner == null) {
            return true;
        }
        if (this.squaredDistanceTo(owner) > this.getMinionDisappearRange()) {
            if (this.isAccessoryWolf && this.isEquipAccessory(owner)) {
                if (this.tryTeleportToOwner(owner)) {
                    return true;
                }
                double ratio = (double) this.lifeTicks / this.getTotalLifeTicks();
                if (ratio > 0) {
                    ManaUtils.gainPlayerMana(owner, this.level * ratio);
                }
                return false;
            }
            return false;
        }
        if (owner instanceof IPlayerEntityMinion iPlayerEntityMinion) {
            if (!iPlayerEntityMinion.shape_shifter_curse$minionExist(this.getMinionTypeID(), this.getUuid())) {
                iPlayerEntityMinion.shape_shifter_curse$getMinionsByMinionID(this.getMinionTypeID()).add(this.getUuid());
            }
            return true;
        }
        return false;
    }

    private boolean tryTeleportToOwner(PlayerEntity owner) {
        for (int i = 0; i < 10; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2;
            double dist = 1.0 + this.random.nextDouble() * 3.0;
            double tx = owner.getX() + Math.cos(angle) * dist;
            double tz = owner.getZ() + Math.sin(angle) * dist;
            double ty = owner.getY();
            BlockPos pos = BlockPos.ofFloored(tx, ty, tz);
            if (this.getWorld().getBlockState(pos).isAir() && this.getWorld().getBlockState(pos.up()).isAir()) {
                this.requestTeleport(tx, ty, tz);
                return true;
            }
        }
        return false;
    }

    public double getMinionDisappearRange() {
        return 32.0d * 32.0d;
    }

    private boolean isTargetNearOwner(LivingEntity target) {
        PlayerEntity owner = this.getOwner();
        if (owner == null) {
            return false;
        }
        return owner.squaredDistanceTo(target) <= 144.0d;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Level", this.level);
        nbt.putInt("LifeTicks", this.lifeTicks);
        nbt.putInt("DodgeCooldown", this.dodgeCooldown);
        nbt.putFloat("MinionHealth", this.getHealth());
        nbt.putBoolean("AccessoryWolf", this.isAccessoryWolf);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setLevel(nbt.getInt("Level"));
        this.lifeTicks = nbt.getInt("LifeTicks");
        this.dodgeCooldown = nbt.getInt("DodgeCooldown");
        this.isAccessoryWolf = nbt.getBoolean("AccessoryWolf");
        this.applyLevelAttributes(false);
        this.setHealth(nbt.getFloat("MinionHealth"));
        this.soulUpdateTimer = 20;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }

    @Override
    public void onDeath(DamageSource source) {
        PlayerEntity owner = this.getOwner();
        if (!this.naturalDeath && owner != null) {
            int refund = this.getKillRefund(owner, this.level);
            if (refund != 0) {
                ManaUtils.gainPlayerMana(owner, refund);
            }
        }
        if (this.getMinionOwnerUUID() != null && this.getWorld().getPlayerByUuid(this.getMinionOwnerUUID()) instanceof IPlayerEntityMinion iPlayerEntityMinion) {
            iPlayerEntityMinion.shape_shifter_curse$removeMinion(this.getMinionTypeID(), this.getUuid());
        }
        this.setOwnerUuid(null);
        super.onDeath(source);
    }

    @Override
    public boolean shouldDropXp() {
        return false;
    }

    @Override
    public World method_48926() {
        return super.getWorld();
    }

    @Override
    @Nullable
    public PlayerEntity getOwner() {
        LivingEntity owner = super.getOwner();
        return owner instanceof PlayerEntity player ? player : null;
    }

    public boolean isEquipAccessory(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        List<ItemStack> mainHand = AccessoryUtils.getEntitySlot(player, "auto", "hand", "ring");
        List<ItemStack> offHand = AccessoryUtils.getEntitySlot(player, "auto", "offhand", "ring");
        if (mainHand != null && mainHand.stream().anyMatch(stack -> stack.isOf(Init_Item.UNDEAD_RING))) return true;
        if (offHand != null && offHand.stream().anyMatch(stack -> stack.isOf(Init_Item.UNDEAD_RING))) return true;
        return false;
    }

    class WolfEscapeDangerGoal extends EscapeDangerGoal {
        public WolfEscapeDangerGoal(double speed) {
            super(SoulWolfMinion.this, speed);
        }

        protected boolean isInDanger() {
            return this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
        }
    }
}