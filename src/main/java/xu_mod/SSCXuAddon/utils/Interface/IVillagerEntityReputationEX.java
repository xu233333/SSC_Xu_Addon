package xu_mod.SSCXuAddon.utils.Interface;

import xu_mod.SSCXuAddon.utils.ExtraReputationTypes;

import java.util.UUID;

public interface IVillagerEntityReputationEX {
    public double SSC_Xu_Addon$getExtraReputation(UUID playerUUID, ExtraReputationTypes rt);

    public void SSC_Xu_Addon$setExtraReputation(UUID playerUUID, ExtraReputationTypes rt, double reputation);
}
