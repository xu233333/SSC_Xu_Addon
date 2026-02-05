package xu_mod.SSCXuAddon.mixin.integration;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class Plugin implements IMixinConfigPlugin {
    private static final HashMap<String, Supplier<Boolean>> MixinLoadConditionRegistry = new HashMap<>();

    static {
        MixinLoadConditionRegistry.put("xu_mod.SSCXuAddon.mixin.integration.GlowFixMixin", () -> {
            try {
                Optional<ModContainer> SSC_Container = FabricLoader.getInstance().getModContainer("shape-shifter-curse");
                if (SSC_Container.isPresent()) {
                    return !(SSC_Container.get().getMetadata().getVersion().compareTo(Version.parse("1.8.3")) > 0);
                }
                return true;
            } catch (VersionParsingException e) {
                return true;
            }
        });
    }

    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (MixinLoadConditionRegistry.containsKey(mixinClassName)) {
            return MixinLoadConditionRegistry.get(mixinClassName).get();
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

}
