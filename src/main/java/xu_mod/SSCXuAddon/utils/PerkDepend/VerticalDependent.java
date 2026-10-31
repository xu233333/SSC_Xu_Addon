package xu_mod.SSCXuAddon.utils.PerkDepend;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.perk.IDependent;
import net.onixary.shapeShifterCurseFabric.perk.PerkTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;

public class VerticalDependent implements IDependent {

    private static final int LINE_X_OFFSET_1 = -1;
    private static final int LINE_X_OFFSET_2 = 2;
    private static final int ICON_TOP_OFFSET = -9;
    private static final int ICON_BOTTOM_OFFSET = 10;

    private final Identifier dependentPerkID;

    public VerticalDependent(Identifier dependentPerkID) {
        this.dependentPerkID = dependentPerkID;
    }

    @Override
    public boolean isDependentPerk(@NotNull Identifier perk) {
        return dependentPerkID.equals(perk);
    }

    @Override
    public boolean isAllDependentGained(PlayerEntity player, @Nullable List<Identifier> playerGainedPerk) {
        if (playerGainedPerk == null) return false;
        return playerGainedPerk.contains(dependentPerkID);
    }

    @Override
    public void drawDependentLine(DrawContext drawContext, int nowNodeBaseX, Vector2i nodeCenter, PerkTree tree, PerkTree.PerkNode perkNode) {
        PerkTree.PerkNode dependNode = tree.getNode(dependentPerkID);
        if (dependNode == null) return;
        if (perkNode.tier != dependNode.tier) return;

        int nodeX = nowNodeBaseX + posXPerTier * perkNode.tier;
        int x1 = nodeCenter.x + nodeX + LINE_X_OFFSET_1;
        int x2 = nodeCenter.x + nodeX + LINE_X_OFFSET_2;

        int y1;
        int y2;
        if (perkNode.y < dependNode.y) {
            y1 = nodeCenter.y + perkNode.y + ICON_BOTTOM_OFFSET;
            y2 = nodeCenter.y + dependNode.y + ICON_TOP_OFFSET;
        } else {
            y1 = nodeCenter.y + perkNode.y + ICON_TOP_OFFSET;
            y2 = nodeCenter.y + dependNode.y + ICON_BOTTOM_OFFSET;
        }

        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);

        drawContext.fill(x1, top, x1 + 1, bottom + 1, LineColor);
        drawContext.fill(x2, top, x2 + 1, bottom + 1, LineColor);
    }
}