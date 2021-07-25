package trapcraft.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftItems;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.IgniterBlock;
import trapcraft.inventory.IgniterContainer;
import trapcraft.inventory.IgniterInventory;

import javax.annotation.Nonnull;

/**
 * @author ProPercivalalb
 **/
public class IgniterTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventoryChangedListener {


    public IgniterInventory inventory;
    public int lastUpgrades;

    public IgniterTileEntity() {
        super(TrapcraftTileEntityTypes.IGNITER.get());
        this.inventory = new IgniterInventory(6);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        final ListNBT nbttaglist = nbt.getList("Items", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            final CompoundNBT nbttagcompound1 = nbttaglist.getCompound(i);
            final byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.getContainerSize()) {
                this.inventory.setItem(b0, ItemStack.of(nbttagcompound1));
            }
        }
        this.inventory.addListener(this);
        this.lastUpgrades = this.getRangeUpgrades();
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ListNBT nbttaglist = new ListNBT();

        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            if (this.inventory.getItem(i) != null) {
                CompoundNBT nbttagcompound1 = new CompoundNBT();
                nbttagcompound1.putByte("Slot", (byte)i);
                this.inventory.getItem(i).save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        compound.put("Items", nbttaglist);

        return compound;
    }


    public int getRangeUpgrades() {
        int upgrades = 0;
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            final ItemStack stack = this.inventory.getItem(i);
            if (stack.getItem() == TrapcraftItems.IGNITER_RANGE.get()) {
                upgrades += stack.getCount();
            }
        }

        return Math.min(upgrades, 100);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            ((IgniterBlock)TrapcraftBlocks.IGNITER.get()).updateIgniterState(this.level, this.worldPosition);
        }
    }

    @Override
    public void containerChanged(IInventory invBasic) {
        if (!this.level.isClientSide) {
            final int newUpgrades = this.getRangeUpgrades();
            if (newUpgrades != this.lastUpgrades) {
                ((IgniterBlock)TrapcraftBlocks.IGNITER.get()).updateIgniterState(this.level, this.worldPosition);
            }
        }
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new IgniterContainer(windowId, playerInventory, this.inventory);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.trapcraft.igniter");
    }

}
