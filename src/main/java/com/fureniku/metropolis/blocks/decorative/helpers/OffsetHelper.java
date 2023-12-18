package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.enums.HelperType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class OffsetHelper extends HelperBase {

    BlockOffsetDirection _offsetDirection;

    public OffsetHelper(BlockOffsetDirection offsetDirection) {
        _offsetDirection = offsetDirection;
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        return state;
    }

    @Override
    public HelperType getType() {
        return HelperType.OFFSET;
    }

    public Vec3 getOffset(BlockGetter level, BlockPos pos) {
        double x = 0;
        double y = 0;
        double z = 0;
        switch (_offsetDirection) {
            case NONE -> {
                return Vec3.ZERO;
            }
            case DOWN -> {
                y = getOffsetBlockPosValue(level, pos.below(), Direction.Axis.Y, true);
            }
            case BACK -> {
            }
            case LEFT -> {
            }
            case FORWARD -> {
            }
            case RIGHT -> {
            }
            case UP -> {
                y = getOffsetBlockPosValue(level, pos.above(), Direction.Axis.Y, false);
            }
        }
        double finalX = x;
        double finalY = y;
        double finalZ = z;
        return new Vec3(x, y, z);
    }

    private double getOffsetBlockPosValue(BlockGetter level, BlockPos pos, Direction.Axis axis, boolean positive) {
        if (level.getBlockState(pos).getBlock() == Blocks.AIR) {
            return 0;
        }
        VoxelShape shape = level.getBlockState(pos).getShape(level, pos);
        return positive ? (1 - shape.max(axis)) * -1 : shape.min(axis);
    }
}
