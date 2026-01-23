package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.entity.projectiles.BloodThornEntity;

public class Init_Entity {
    public static final EntityType<BloodThornEntity> BLOOD_THORN = Registry.register(
            Registries.ENTITY_TYPE,
            SSCXuAddon.identifier("blood_thorn"),
            FabricEntityTypeBuilder.<BloodThornEntity>create(SpawnGroup.MISC, BloodThornEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(10).trackedUpdateRate(1).build()
    );

    public static void init() {

    }

}
