package xu_mod.SSCXuAddon.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.cca.AddonDataComponent;

public class Init_CCA implements EntityComponentInitializer {
    public static ComponentKey<AddonDataComponent> AddonData = ComponentRegistry.getOrCreate(SSCXuAddon.identifier("addon_data"), AddonDataComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(
                AddonData,
                AddonDataComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
