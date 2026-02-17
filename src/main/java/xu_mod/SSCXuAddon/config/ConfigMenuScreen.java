package xu_mod.SSCXuAddon.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import xu_mod.SSCXuAddon.network.ModPacketsClient;

import java.util.function.Supplier;

// 配置菜单Mod 只支持一个配置菜单 添加一个选择菜单
public class ConfigMenuScreen extends Screen {
    private Screen parent;
    public ConfigMenuScreen(Screen parent) {
        super(Text.translatable("text.ssc_xu_addon.config.title"));
    }

    public void init() {
        int Config_BTN_Size_X = 240;  // 按钮长 英文过长时请修改
        int Config_BTN_Size_Y = 20;  // 按钮宽
        int Config_BTN_Interval = 10;  // 按钮Y方向间隔 留空部分
        int Config_Count = 2;  // 配置数量  **** 添加配置时修改 ****
        int Additional_Button_Count = 1;  // 额外按钮数量 [关闭配置界面按钮]
        int Config_BTN_X_Pos = (width - Config_BTN_Size_X) / 2;  // 按钮X坐标
        int Config_BTN_Y_Start_Pos = (height - Config_BTN_Size_Y * (Config_Count + Additional_Button_Count) - Config_BTN_Interval * (Config_Count + Additional_Button_Count - 1)) / 2;  // 按钮Y坐标起始位置
        int Config_BTN_Y_Pos = Config_BTN_Y_Start_Pos;  // 按钮Y坐标

        // 添加按钮
        AddButton(Config_BTN_X_Pos, Config_BTN_Y_Pos, Config_BTN_Size_X, Config_BTN_Size_Y, Text.translatable("text.autoconfig.ssc_xu_addon_server.title"), AutoConfig.getConfigScreen(ServerConfig.class, this));
        Config_BTN_Y_Pos += Config_BTN_Size_Y + Config_BTN_Interval;
        // 自动同步配置
        AddButton(Config_BTN_X_Pos, Config_BTN_Y_Pos, Config_BTN_Size_X, Config_BTN_Size_Y, Text.translatable("text.autoconfig.ssc_xu_addon_custom.title"), AutoConfig.getConfigScreen(PlayerCustomConfig.class, this));
        Config_BTN_Y_Pos += Config_BTN_Size_Y + Config_BTN_Interval;
        // **** 在这里添加配置 ****

        // 关闭配置界面按钮
        AddCloseButton(Config_BTN_X_Pos, Config_BTN_Y_Pos, Config_BTN_Size_X, Config_BTN_Size_Y, Text.translatable("text.shape-shifter-curse.config.close"));
    }

    public void AddButton(int PosX, int PosY, int SizeX, int SizeY, Text text, Supplier<Screen> ConfigScreenSupplier) {
        addDrawableChild(ButtonWidget.builder(text, button -> {
            MinecraftClient.getInstance().setScreen(ConfigScreenSupplier.get());
        }).size(SizeX, SizeY).position(PosX, PosY).build());
    }

    public void AddCloseButton(int PosX, int PosY, int SizeX, int SizeY, Text text) {
        addDrawableChild(ButtonWidget.builder(text, button -> close()).size(SizeX, SizeY).position(PosX, PosY).build());
    }

    @Override
    public void close() {
        try {
            ModPacketsClient.sendUpdateCustomPlayerConfigPacket();
        }
        catch (Exception ignored) {}
        MinecraftClient.getInstance().setScreen(parent);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}
