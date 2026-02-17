package xu_mod.SSCXuAddon.utils.Inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class NBTInventory extends SimpleInventory {
    public int SlotCount;
    public final Inventory ItemInventory;
    public final ItemStack itemStack;
    public final List<ItemStack> ExtraSlot = new ArrayList<>();

    public NBTInventory(Inventory ItemInventory, ItemStack itemStack, int DefaultCount) {
        // 为什么这么写 因为super只能写在第一行
        super(itemStack.getOrCreateNbt().getInt("slot_count") == 0 ? DefaultCount : itemStack.getOrCreateNbt().getInt("slot_count"));
        this.itemStack = itemStack;
        this.ItemInventory = ItemInventory;
        this.SlotCount = itemStack.getOrCreateNbt().getInt("slot_count") == 0 ? DefaultCount : itemStack.getOrCreateNbt().getInt("slot_count");
        NbtList Slots = itemStack.getOrCreateNbt().getList("slots", 10);
        if (Slots != null) {
            int SlotLength = Slots.size();
            for (int i = 0; i < this.SlotCount; i++) {
                NbtCompound ItemNBT = Slots.getCompound(i);
                if (!ItemNBT.isEmpty()) {
                    this.setStack(i, ItemStack.fromNbt(ItemNBT));
                }
            }
            // 防止小天才修改NBT 导致吞物品
            for (int i = this.SlotCount; i < SlotLength; i++) {
                NbtCompound ItemNBT = Slots.getCompound(i);
                if (!ItemNBT.isEmpty()) {
                    this.ExtraSlot.add(ItemStack.fromNbt(ItemNBT));
                }
            }
        }
    }

    public void SaveBag() {
        NbtCompound nbtCompound = this.itemStack.getOrCreateNbt();
        nbtCompound.putInt("slot_count", this.SlotCount);
        NbtList Slots = new NbtList();
        for (int i = 0; i < this.SlotCount; i++) {
            ItemStack itemStack = this.getStack(i);
            Slots.add(itemStack.writeNbt(new NbtCompound()));
        }
        for (ItemStack itemStack : this.ExtraSlot) {
            Slots.add(itemStack.writeNbt(new NbtCompound()));
        }
        nbtCompound.put("slots", Slots);
    }

    @Override
    public void markDirty() {
        SaveBag();
        super.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (ItemInventory == null) {
            return true;
        }
        for (int slotIndex = 0; slotIndex < ItemInventory.size(); slotIndex++) {
            ItemStack itemStack = ItemInventory.getStack(slotIndex);
            if (itemStack == this.itemStack) {
                return true;
            }
        }
        return false;
    }
}
