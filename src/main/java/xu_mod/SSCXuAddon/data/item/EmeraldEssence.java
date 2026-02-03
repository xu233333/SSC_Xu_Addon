package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.minion.MinionRegister;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;

import java.util.List;

public class EmeraldEssence extends Item {
    public EmeraldEssence(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            // 我认为就一种形态需要用这种方式变身 就不拆成函数吧
            PlayerFormBase form = RegPlayerFormComponent.PLAYER_FORM.get(user).getCurrentForm();
            if (RegPlayerForms.FERAL_CAT_SP.equals(form)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.emerald_essence.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.handleDirectTransform(player, Init_Form.FeralCatVF, false);
            } else if (Init_Form.FeralCatVF.equals(form)) {
                // 召唤铁傀儡
                BlockPos pos = MinionRegister.getNearbyEmptySpace(player.getWorld(), player.getRandom(), player.getBlockPos(), 7, 5, 3, 8);
                if (pos == null) {
                    pos = player.getBlockPos();
                }
                IronGolemEntity ironGolemEntity = EntityType.IRON_GOLEM.create(player.getWorld());
                if (ironGolemEntity != null) {
                    ironGolemEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    player.getWorld().spawnEntity(ironGolemEntity);
                }
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.getItemCooldownManager().set(this, 1200); // 60s冷却
            } else {
                int emeraldCount = 36;
                int random = player.getRandom().nextInt(8 + 16) - 8;
                emeraldCount += random;
                player.giveItemStack(new ItemStack(Items.EMERALD, emeraldCount));
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.emerald_essence.tooltip").formatted(Formatting.YELLOW));
    }
}
