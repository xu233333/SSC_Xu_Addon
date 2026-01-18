package xu_mod.SSCXuAddon.data.manaType;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.mana.IManaRender;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.util.UIPositionUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_CCA;

public class FamiliarFoxPurifyManaRender implements IManaRender {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Identifier BarTexID = SSCXuAddon.identifier("textures/gui/familiar_fox_purify_mana_bar.png");

    @Override
    public boolean OverrideInstinctBar() {
        return false;
    }

    public void render(DrawContext context, float tickDelta) {
        if (!mc.options.hudHidden) {
            Pair<Integer, Integer> pos = UIPositionUtils.getCorrectPosition(ShapeShifterCurseFabric.clientConfig.instinctBarPosType, ShapeShifterCurseFabric.clientConfig.instinctBarPosOffsetX, ShapeShifterCurseFabric.clientConfig.instinctBarPosOffsetY);
            this.renderBar(context, tickDelta, (Integer)pos.getLeft(), (Integer)pos.getRight());
        }
    }

    private void renderBar(DrawContext context, float tickDelta, int x, int y) {
        if (mc.player == null) {
            return;
        }
        int manaLevel = Init_CCA.AddonData.get(mc.player).getManaLevel();
        int manaColor = 0xFF505050;
        switch (manaLevel) {
            case 0 -> {
                manaColor = 0xFF505050;
            }
            case 1 -> {
                manaColor = 0xFF00FF00;
            }
            case 2 -> {
                manaColor = 0xFF0000FF;
            }
            case 3 -> {
                manaColor = 0xFF6F00FF;
            }
            default -> {
                manaColor = 0xFFFFFFFF;
            }
        }
        double mana = ManaUtils.getPlayerMana(mc.player);
        double maxMana = ManaUtils.getPlayerMaxMana(mc.player);
        double manaRegen = ManaUtils.getPlayerManaRegen(mc.player);
        int remainTicks = -1;
        if (manaRegen > (double)0.0F) {
            remainTicks = (int)Math.ceil((maxMana - mana) / manaRegen);
        }

        int manaWidth = (int)Math.ceil((double)80.0F * ManaUtils.getManaPercent(mana, maxMana, (double)0.0F));
        context.drawTexture(BarTexID, x, y, 0.0f, 10 * manaLevel, 80, 5, 80, 40);
        context.drawTexture(BarTexID, x, y, 0.0f, 10 * manaLevel + 5, manaWidth, 5, 80, 40);
        StringBuilder manaString = new StringBuilder();
        manaString.append((int)mana).append("/").append((int)maxMana);
        if (remainTicks > 0) {
            manaString.append(" (").append(remainTicks).append(")");
        } else if (remainTicks < 0) {
            manaString.append(" (").append("?").append(")");
        }

        Text manaText = Text.literal(manaString.toString());
        int manaTextWidth = mc.textRenderer.getWidth(manaText);
        context.drawText(mc.textRenderer, manaText, x + 80 + 5, y - 2, manaColor, false);
    }
}
