package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;

public class OcelotRush1Power extends ActiveCooldownPower {
    // 由于部分技能需要Power驱动(需要持续触发) 用Action极其难处理 所以添加了skills放这种基本上无法复用的能力

    // 冲锋 (原始点) -> (小爆炸) - (小爆炸) - (小爆炸) - (着陆点)(大爆炸(仅落地)) 落地或到最大时间时移除此次触发 长时间飞行就无法触发最后的大爆炸 移动为抛物线 此爆炸非彼爆炸 范围内全部受到全部爆炸伤害
    // 衰竭状态下无法触发技能

    private static final long skillMaxTick = 100; // 5s

    // Config:
    private float smallExplosionDamage = 6.0f;
    private float smallExplosionRange = 1.5f;
    private float bigExplosionDamage = 12.0f;
    private float bigExplosionRange = 2.5f;
    private int smallExplosionTickRate = 20;  // 1s 一次
    private float movementSpeedX = 0.5f;  // 初始移动速度X(视角方向) 未调整
    private float movementSpeedY = 0.2f;  // 初始移动速度Y 未调整
    private Consumer<Entity> onInvokeAction = null;
    private Consumer<Entity> onLandAction = null;

    private long skillRemainTick = 0;
    private long skillTimer = 0;

    public OcelotRush1Power(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender, activeFunction);
    }

    @Override
    public void onUse() {
        if(canUse()) {

        }
    }

}
