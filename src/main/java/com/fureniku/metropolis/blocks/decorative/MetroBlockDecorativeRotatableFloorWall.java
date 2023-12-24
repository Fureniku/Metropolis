/*package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.enums.DirectionFloorWall;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorativeRotatableFloorWall extends MetroBlockDecorative {

    public static final EnumProperty<DirectionFloorWall> DIRECTION = EnumProperty.create("direction", DirectionFloorWall.class);
    private final VoxelShape BLOCK_SHAPE_FLOOR;
    private final VoxelShape BLOCK_SHAPE_NORTH;
    private final VoxelShape BLOCK_SHAPE_EAST;
    private final VoxelShape BLOCK_SHAPE_SOUTH;
    private final VoxelShape BLOCK_SHAPE_WEST;

    public MetroBlockDecorativeRotatableFloorWall(Properties props, VoxelShape shapeFloor, VoxelShape shapeWall, String modelDir, String modelName, BlockOffsetDirection offsetDirection, TextureSet... textures) {
        super(props, shapeFloor, modelDir, modelName, offsetDirection, textures);
        BLOCK_SHAPE_FLOOR = shapeFloor;
        BLOCK_SHAPE_NORTH = shapeWall;
        BLOCK_SHAPE_EAST = SimpleUtils.rotateVoxelShape(shapeWall, Direction.EAST);
        BLOCK_SHAPE_SOUTH = SimpleUtils.rotateVoxelShape(shapeWall, Direction.SOUTH);
        BLOCK_SHAPE_WEST = SimpleUtils.rotateVoxelShape(shapeWall, Direction.WEST);
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, DirectionFloorWall.FLOOR_NORTH));
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder bmb;
        if (_modelName == null || _resources == null) {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", _modelDir + block.getName(), blockStateProvider.modLoc("blocks/decorative/" + block.getName()));
        } else {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", _modelDir + _modelName, _resources[0].getTexture());
            if (_resources.length > 1) {
                for (int i = 1; i < _resources.length; i++) {
                    bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
                }
            }
        }

        blockStateProvider.horizontalFloorWallBlock(block, bmb);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate;
        if (context.getClickedFace().getAxis() != Direction.Axis.Y) {
            blockstate = this.defaultBlockState().setValue(DIRECTION, DirectionFloorWall.getWallDirection(context.getHorizontalDirection()));
        } else {
            blockstate = this.defaultBlockState().setValue(DIRECTION, DirectionFloorWall.getFloorDirection(context.getHorizontalDirection()));
        }

        return blockstate;
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState pState) {
        switch (pState.getValue(DIRECTION)) {
            case WALL_NORTH:
                return BLOCK_SHAPE_NORTH;
            case WALL_EAST:
                return BLOCK_SHAPE_EAST;
            case WALL_SOUTH:
                return BLOCK_SHAPE_SOUTH;
            case WALL_WEST:
                return BLOCK_SHAPE_WEST;
        }
        return BLOCK_SHAPE_FLOOR;
    }
}
*/