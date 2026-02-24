package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.apoli.util.Space;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3f;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.utils.Misc.ExplosionBehaviorExceptBreakBlock;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OcelotRush1Power extends ActiveCooldownPower {
    // 由于部分技能需要Power驱动(需要持续触发) 用Action极其难处理 所以添加了skills放这种基本上无法复用的能力

    // 冲锋 (原始点) -> (小爆炸) - (小爆炸) - (小爆炸) - (着陆点)(大爆炸(仅落地)) 落地或到最大时间时移除此次触发 长时间飞行就无法触发最后的大爆炸 移动为抛物线 此爆炸非彼爆炸 范围内全部受到全部爆炸伤害
    // 衰竭状态下无法触发技能

    private static final long skillMaxTick = 100; // 5s

    // Config:
    private float smallExplosionDamage = 6.0f;
    private float smallExplosionRange = 0.5f;
    private float bigExplosionPower = 6.0f;
    private int smallExplosionTickRate = 20;  // 1s 一次
    private float movementSpeedX = 0.5f;  // 初始移动速度X(视角方向) 未调整
    private float movementSpeedY = 0.2f;  // 初始移动速度Y 未调整
    private Consumer<Entity> onInvokeAction = null;
    private Consumer<Entity> onLandAction = null;
    private Predicate<Entity> triggerCondition = null;

    private long skillRemainTick = 0;
    private long skillTimer = 0;

    public OcelotRush1Power(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender, activeFunction);
    }

    @Override
    public void onUse() {
        if(canUse() && (triggerCondition == null || triggerCondition.test(this.entity))) {
            this.skillRemainTick = skillMaxTick;
            this.setTicking();
            Vector3f vec = new Vector3f(0, 0, movementSpeedX);
            Space.LOCAL.toGlobal(vec, this.entity);
            this.entity.addVelocity(vec.x, vec.y, vec.z);
            this.entity.addVelocity(0, movementSpeedY, 0);
            this.entity.velocityModified = true;
            if (this.onInvokeAction != null) {
                this.onInvokeAction.accept(this.entity);
            }
        }
    }

    @Override
    public void tick() {
        if (this.entity.getWorld() instanceof ServerWorld serverWorld) {
            if (this.skillRemainTick <= 0) {
                this.setTicking(false);
                return;
            }
            if (this.skillTimer >= smallExplosionTickRate) {
                this.skillTimer = 0;
                // 小爆炸
                Box expandedBox = this.entity.getBoundingBox().expand(smallExplosionRange);
                for (ServerPlayerEntity otherPlayer : serverWorld.getPlayers()) {
                    serverWorld.spawnParticles(
                            otherPlayer,
                            ParticleTypes.CRIT,
                            true,
                            this.entity.getX(),
                            this.entity.getY(),
                            this.entity.getZ(),
                            8,
                            0.5f,
                            0.5f,
                            0.5f,
                            0.0f
                    );
                }
                entity.getWorld().playSound(null, this.entity.getX(), this.entity.getY(), this.entity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5f, 1.0f);
                serverWorld.getOtherEntities(this.entity, expandedBox).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(serverWorld.getDamageSources().explosion(this.entity, this.entity), smallExplosionDamage);
                    }
                });
            }
            this.skillTimer++;
            if (this.entity.isOnGround() || !this.entity.getWorld().getBlockState(this.entity.getBlockPos()).isAir()) {
                this.skillRemainTick = 0;
                this.setTicking(false);
                // 只有在着陆时才会大爆炸
                if (this.entity.isOnGround()) {
                    // 大爆炸
                    Explosion explosion = new Explosion(
                            serverWorld,
                            this.entity,
                            serverWorld.getDamageSources().explosion(this.entity, this.entity),
                            new ExplosionBehaviorExceptBreakBlock(),
                            this.entity.getX(), this.entity.getY(), this.entity.getZ(),
                            bigExplosionPower,
                            false,
                            Explosion.DestructionType.KEEP
                    );
                    explosion.collectBlocksAndDamageEntities();
                    explosion.affectWorld(false);
                    if (this.onLandAction != null) {
                        this.onLandAction.accept(this.entity);
                    }
                    for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                        if (serverPlayerEntity.squaredDistanceTo(this.entity.getX(), this.entity.getY(), this.entity.getZ()) < 4096.0) {
                            serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(this.entity.getX(), this.entity.getY(), this.entity.getZ(), bigExplosionPower, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)));
                        }
                    }
                }
            }
            this.skillRemainTick--;
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("ocelot_rush_skill"),
                new SerializableData()
                        .add("small_explosion_damage", SerializableDataTypes.FLOAT, 6.0f)
                        .add("small_explosion_range", SerializableDataTypes.FLOAT, 1.0f)
                        .add("big_explosion_power", SerializableDataTypes.FLOAT, 6.0f)
                        .add("small_explosion_tick_rate", SerializableDataTypes.INT, 20)
                        .add("movement_speed_x", SerializableDataTypes.FLOAT, 0.5f)
                        .add("movement_speed_y", SerializableDataTypes.FLOAT, 0.2f)
                        .add("on_invoke_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("on_land_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("cooldown", SerializableDataTypes.INT, 100)
                        .add("trigger_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("key", ApoliDataTypes.KEY, null),
                data -> (type, entity) -> {
                    Key key = data.get("key");
                    OcelotRush1Power power = new OcelotRush1Power(type, entity, data.getInt("cooldown"), HudRender.DONT_RENDER, null);
                    if (key != null) {
                        power.setKey(key);
                    }
                    power.smallExplosionDamage = data.getFloat("small_explosion_damage");
                    power.smallExplosionRange = data.getFloat("small_explosion_range");
                    power.bigExplosionPower = data.getFloat("big_explosion_power");
                    power.smallExplosionTickRate = data.getInt("small_explosion_tick_rate");
                    power.movementSpeedX = data.getFloat("movement_speed_x");
                    power.movementSpeedY = data.getFloat("movement_speed_y");
                    power.onInvokeAction = data.get("on_invoke_action");
                    power.onLandAction = data.get("on_land_action");
                    power.triggerCondition = data.get("trigger_condition");
                    return power;
                }
        ).allowCondition();
    }
}
