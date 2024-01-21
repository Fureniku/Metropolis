package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.utils.CreativeTabSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class to create multiple similar blocks quickly. Great for matching decorative blocks... like roads!
 * Doesn't use anything like metadata or blockstates - so no 16 block limit!
 * Creates entirely individual blocks in a rudimentary linked list, so you can cycle them with a tool.
 */
public abstract class BlockSet {

    protected int _blockCount;
    protected RegistryObject<Block>[] _blocks;
    protected RegistryObject<Item>[] _itemBlocks;
    protected String _name;
    protected ResourceLocation _resourceLocation;

    protected BiFunction<BlockAndTintGetter, BlockPos, Integer> tintColorBlock;
    protected Supplier<Integer> tintColorItem;

    /**
     * Create a new blockset. Note all IDs are 1-based as this was taken from roads with the value being height.
     * @param name Name of the blocks in the set. Will have _id appended to it.
     * @param count How many blocks should be created in total
     * @param register Your mods instance of RegistrationBase
     */
    public BlockSet(String name, int count, RegistrationBase register) {
        this(name, null, count, register);
    }

    /**
     * Create a new blockset. Note all IDs are 1-based as this was taken from roads with the value being height.
     * @param name Name of the blocks in the set. Will have _id appended to it.
     * @param texture The texture to use. Pass null to use name texture.
     * @param count How many blocks should be created in total
     * @param register Your mods instance of RegistrationBase
     */
    public BlockSet(String name, ResourceLocation texture, int count, RegistrationBase register) {
        _name = name;
        _resourceLocation = texture;
        _blockCount = count;
        _blocks =  new RegistryObject[_blockCount];
        _itemBlocks = new RegistryObject[_blockCount];

        init(register);
    }

    //Initialize all the blocks, alongside their itemblocks
    private void init(RegistrationBase register) {
        for (int i = 0; i < _blockCount; i++) {
            final int id = i+1;
            _blocks[i] = register.retrieveRegisterBlockSet(_name + "_" + id, getClassSupplier(id));
            _itemBlocks[i] = register.getItem(_name + "_" + id);
        }
    }

    /**
     * Add a block and item tint colour to the blockset. Currently used for grass roads, maybe more later. Use the BiFunction to calculate based on blockpos if needed.
     * This should only ever be called in your registration.
     * @param modEventBus The event bus for your mod's loading stage
     * @param blockColor The block color for your block (as a BiFunction with types BlockAndTintGetter, BlockPos, returning Integer)
     * @param itemColor The item colour for your itemblock (as a Supplier returning Integer)
     * @return An instance of BlockSet for method chaining.
     */
    public BlockSet addColorTints(IEventBus modEventBus, BiFunction<BlockAndTintGetter, BlockPos, Integer> blockColor, Supplier<Integer> itemColor) {
        modEventBus.addListener(this::registerBlockColors);
        modEventBus.addListener(this::registerItemColors);
        tintColorBlock = blockColor;
        tintColorItem = itemColor;
        return this;
    }

    /**
     * Return a supplier for a block class. Use ID for any differentiation you may need.
     * @param id The ID of the individual block this supplier will create. 1-based!
     * @return A block supplier, for example () -> new RoadBlock(id)
     */
    protected abstract Supplier<Block> getClassSupplier(int id);

    /**
     * Get a block in this set from its numerical ID
     * @param id The ID of the block to get
     * @return the block
     */
    public Block getFromId(int id) {
        return _blocks[id-1].get();
    }

    /**
     * Get the name of this blockset - this is the block name without _id appended.
     * @return the blockset name
     */
    public String getSetName() {
        return _name;
    }

    /**
     * Register all the blocks in this set to the provided creative tab
     * @param tab
     */
    public void registerToCreativeTab(CreativeTabSet tab) {
        for (int i = 0; i < _blockCount; i++) {
            tab.addItem(_blocks[i].get().asItem().getDefaultInstance());
        }
    }

    /**
     * Get a specific registry block entry. Can be called before registration finishes.
     * @param id the ID of the block to get
     * @return RegistryObject of the block with the provided ID
     */
    public RegistryObject<Block> getRegistryBlock(int id) {
        return _blocks[id-1];
    }

    /**
     * Get a specific registry item entry. Can be called before registration finishes. Great to get a specific item for a creative tab.
     * @param id the ID of the itemblock to get
     * @return RegistryObject of the itemblock with the provided ID
     */
    public RegistryObject<Item> getRegistryItem(int id) {
        return _itemBlocks[id-1];
    }

    /**
     * Get a list of all registered blocks. Currently used for grass road colouring.
     * @return an array of all registry blocks in this set.
     */
    public RegistryObject<Block>[] getRegistryBlocks() {
        return _blocks;
    }

    /**
     * Get a list of all registered blocks. Currently used for grass road colouring.
     * @return an array of all registry itemblocks in this set.
     */
    public RegistryObject<Item>[] getRegistryItems() {
        return _itemBlocks;
    }

    //Events for handling the block colours
    @SubscribeEvent
    public void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, tint, pos, tintIndex) -> tintColorBlock.apply(tint, pos),
                Stream.of(getRegistryBlocks()).map(RegistryObject::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintColorItem.get(),
                Stream.of(getRegistryItems()).map(RegistryObject::get).toArray(Item[]::new));
    }
}
