package xu_mod.SSCXuAddon.data.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class AddonDataComponent implements AutoSyncedComponent {
    private final PlayerEntity componentOwner;
    private int manaLevel = 1;

    public int getManaLevel() {
        return manaLevel;
    }

    public void setManaLevel(int manaLevel) {
        this.manaLevel = manaLevel;
    }

    public AddonDataComponent(PlayerEntity player) {
        this.componentOwner = player;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        if (nbtCompound.contains("manaLevel")) {
            manaLevel = nbtCompound.getInt("manaLevel");
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("manaLevel", manaLevel);
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
