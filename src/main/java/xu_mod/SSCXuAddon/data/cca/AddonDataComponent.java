package xu_mod.SSCXuAddon.data.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class AddonDataComponent implements AutoSyncedComponent {
    private final PlayerEntity componentOwner;
    private final HashMap<Identifier, Long> cooldownData = new HashMap<>();
    private int manaLevel = 1;

    public int getManaLevel() {
        return manaLevel;
    }

    public void setManaLevel(int manaLevel) {
        this.manaLevel = manaLevel;
    }

    public HashMap<Identifier, Long> getCooldownData() {
        return cooldownData;
    }

    public long getCooldown(Identifier id) {
        return cooldownData.getOrDefault(id, 0L);
    }

    public void setCooldown(Identifier id, long cooldown) {
        cooldownData.put(id, cooldown);
    }

    public void triggerCooldown(Identifier id) {
        cooldownData.put(id, componentOwner.getWorld().getTime());
    }

    public boolean isNotInCooldown(Identifier id, long cooldown) {
        return cooldownData.getOrDefault(id, 0L) + cooldown <= componentOwner.getWorld().getTime();
    }

    public AddonDataComponent(PlayerEntity player) {
        this.componentOwner = player;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        if (nbtCompound.contains("manaLevel")) {
            manaLevel = nbtCompound.getInt("manaLevel");
        }
        if (nbtCompound.contains("cooldownData")) {
            this.cooldownData.clear();
            NbtCompound cooldownCompound = nbtCompound.getCompound("cooldownData");
            for (String id : cooldownCompound.getKeys()) {
                cooldownData.put(new Identifier(id), cooldownCompound.getLong(id));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("manaLevel", manaLevel);
        NbtCompound cooldownCompound = new NbtCompound();
        cooldownData.forEach((id, cooldown) -> cooldownCompound.putLong(id.toString(), cooldown));
        nbtCompound.put("cooldownData", cooldownCompound);
    }

    /*
        // 等什么时候用到仅初始化同步的值时再写

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return true;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        AutoSyncedComponent.super.writeSyncPacket(buf, recipient);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }

     */
}
