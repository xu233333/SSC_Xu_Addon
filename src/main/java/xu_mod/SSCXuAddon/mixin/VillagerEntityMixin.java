package xu_mod.SSCXuAddon.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xu_mod.SSCXuAddon.utils.ExtraReputationTypes;
import xu_mod.SSCXuAddon.utils.Interface.IVillagerEntityReputationEX;

import java.util.HashMap;
import java.util.UUID;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements IVillagerEntityReputationEX {
    @Unique
    private final HashMap<UUID, HashMap<ExtraReputationTypes, Double>> extraReputation = new HashMap<>();

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeExtraReputation(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound extra_reputation = new NbtCompound();
        for (UUID playerUUID : extraReputation.keySet()) {
            NbtCompound player_reputation = new NbtCompound();
            for (ExtraReputationTypes rt : extraReputation.get(playerUUID).keySet()) {
                player_reputation.putDouble(rt.name(), extraReputation.get(playerUUID).get(rt));
            }
            extra_reputation.put(playerUUID.toString(), player_reputation);
        }
        nbt.put("ssc_xu_addon_extra_reputation", extra_reputation);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readExtraReputation(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound extra_reputation = nbt.getCompound("ssc_xu_addon_extra_reputation");
        for (String playerUUID : extra_reputation.getKeys()) {
            NbtCompound player_reputation = extra_reputation.getCompound(playerUUID);
            for (String rt : player_reputation.getKeys()) {
                extraReputation.computeIfAbsent(UUID.fromString(playerUUID), k -> new HashMap<>()).put(ExtraReputationTypes.valueOf(rt), player_reputation.getDouble(rt));
            }
        }
    }

    @Unique
    private int getExtraReputation(PlayerEntity player) {
        HashMap<ExtraReputationTypes, Double> reputation = extraReputation.get(player.getUuid());
        if (reputation == null) {
            return 0;
        }
        int extraReputation = 0;
        for (ExtraReputationTypes rt : reputation.keySet()) {
            if (rt.isGossipEnable(player)) {
                extraReputation += reputation.get(rt).intValue();
            }
        }
        return extraReputation;
    }

    @Inject(method = "getReputation", at = @At("RETURN"), cancellable = true)
    private void modifyReputation(PlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) (cir.getReturnValue() + getExtraReputation(player)));
    }

    @Override
    public double SSC_Xu_Addon$getExtraReputation(UUID playerUUID, ExtraReputationTypes rt) {
        HashMap<ExtraReputationTypes, Double> reputation = extraReputation.get(playerUUID);
        if (reputation == null) {
            return 0;
        }
        return reputation.getOrDefault(rt, 0.0);
    }

    @Override
    public void SSC_Xu_Addon$setExtraReputation(UUID playerUUID, ExtraReputationTypes rt, double reputation) {
        extraReputation.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(rt, reputation);
    }
}
