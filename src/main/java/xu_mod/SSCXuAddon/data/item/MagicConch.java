package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MagicConch extends Item {
    public MagicConch(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 60;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    public static ArrayList<Text> Messages = new ArrayList<>();  // 注册类型 拓展性强 但是我觉得应该没人会做拓展的拓展
    public static ArrayList<String> SecretMessages = new ArrayList<>();  // 先咕了这个功能 反正是彩蛋 不是主要功能 没有本地化(不准备弄) 所以仅中文环境可见

    static {
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.1").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.2").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.3").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.4").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.5").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.6").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.7").formatted(Formatting.YELLOW));
        Messages.add(Text.translatable("item.ssc_xu_addon.magic_conch.message.8").formatted(Formatting.YELLOW));
    }

    private static Text decodeSecret(String secret) {
        // TODO 完成 Secret 系统
        // 准备整点及其复杂的加密(仅提供所以Const数据的情况下 不看源码+不反编译绝对找不到解法)
        // 整个 AES(多种加密方式) + RSA 运算中混合上异或
        return Text.literal(secret);  // 先咕了
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            Random r = player.getRandom();
            if (r.nextInt(100) >= 0) {  // 由于没写彩蛋 先禁用彩蛋 大约彩蛋概率为1%
                player.sendMessage(Messages.get(r.nextInt(Messages.size())), false);
            }
            else {
                // SecretMessage 需要解密 等我之后有空整彩蛋后再写 加解密我很擅长 整一个稍高难度的很简单 但是也防不住 毕竟是开源的代码
                player.sendMessage(decodeSecret(SecretMessages.get(r.nextInt(SecretMessages.size()))), false);
            }
            if (!player.getAbilities().creativeMode) {
                player.getItemCooldownManager().set(this, 600);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.magic_conch.tooltip.1").formatted(Formatting.YELLOW));  // 一个神奇的海螺 可以告诉你一些世界的秘密
        tooltip.add(Text.translatable("item.ssc_xu_addon.magic_conch.tooltip.2").formatted(Formatting.AQUA));  // 可以在藏宝图宝箱中获得
    }
}
