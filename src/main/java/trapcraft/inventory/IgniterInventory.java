package trapcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class IgniterInventory extends Inventory implements INamedContainerProvider {

    public IgniterInventory(int slotCount) {
        super(slotCount);
    }

    @Override
    public int getMaxStackSize() {
        return 8;
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new IgniterContainer(windowId, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.trapcraft.igniter");
    }
}
