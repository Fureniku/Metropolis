package com.fureniku.metropolis.enums;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum DirectionFloorWall implements StringRepresentable {
    FLOOR_NORTH,
    FLOOR_EAST,
    FLOOR_SOUTH,
    FLOOR_WEST,
    WALL_NORTH,
    WALL_EAST,
    WALL_SOUTH,
    WALL_WEST;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static DirectionFloorWall getWallDirection(Direction dir) {
        return getDirection(dir, WALL_NORTH, WALL_EAST, WALL_SOUTH, WALL_WEST);
    }

    public static DirectionFloorWall getFloorDirection(Direction dir) {
        return getDirection(dir, FLOOR_NORTH, FLOOR_EAST, FLOOR_SOUTH, FLOOR_WEST);
    }

    private static DirectionFloorWall getDirection(Direction dir, DirectionFloorWall north, DirectionFloorWall east, DirectionFloorWall south, DirectionFloorWall west) {
        switch (dir) {
            case NORTH -> {
                return north;
            }
            case EAST -> {
                return east;
            }
            case SOUTH -> {
                return south;
            }
            case WEST -> {
                return west;
            }
        }
        return FLOOR_NORTH;
    }

    public int toXRot() {
        if (this == FLOOR_NORTH || this == FLOOR_EAST || this == FLOOR_SOUTH || this == FLOOR_WEST) {
            return 0;
        }
        return 90;
    }

    public int toYRot() {
        switch (this) {
            case FLOOR_NORTH:
            case WALL_NORTH:
                return 0;
            case FLOOR_EAST:
            case WALL_EAST:
                return 90;
            case FLOOR_SOUTH:
            case WALL_SOUTH:
                return 180;
            case FLOOR_WEST:
            case WALL_WEST:
                return 270;
        }
        return 0;
    }
}
