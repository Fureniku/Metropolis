package com.fureniku.metropolis.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtils {

    public static final VoxelShape FULL_BOX = Shapes.box(0, 0, 0, 1, 1, 1);
    private static final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();

    public static VoxelShape makeShape(float size, float height) {
        double x = size/2;
        return Block.box(8-x, 0, 8-x, 8+x, height, 8+x);
    }

    public static VoxelShape makeShape(Vec3 shape) {
        double x = shape.x/2;
        double z = shape.z/2;
        return Block.box(8-x, 0, 8-z, 8+x, shape.y, 8+z);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider. Simple constructor for very simple uniform custom shapes
     * @param size The expanding point from the center for each segment of the block (e.g. 2 will make a segment 4 wide)
     * @param height The height of the block from the floor
     * @return
     */
    public static VoxelShape[] makeShapes(float size, float height) {
        return makeShapes(size, size, height, 0.0f, height);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider. Simple constructor for very simple uniform custom shapes
     * @param size The expanding point from the center for each segment of the block (e.g. 2 will make a segment 4 wide)
     * @param base The starting point from the floor
     * @param height The height of the block from the base
     * @return
     */
    public static VoxelShape[] makeShapes(float size, float base, float height) {
        return makeShapes(size, size, height, base, height);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider
     * @param centerSize The expanding point from the center for the center shape of the block (e.g. 2 will make a segment 4 wide)
     * @param connectionSize The expanding point from the center for the segments of the block (e.g. 2 will make a segment 4 wide)
     * @param cHeight The height of the center of the block
     * @param base The starting point from the floor
     * @param height The height of the segments from the floor
     * @return
     */
    public static VoxelShape[] makeShapes(float centerSize, float connectionSize, float cHeight, float base, float height) {
        double cWidthMin = 8.0F - centerSize;
        double cWidthMax = 8.0F + centerSize;
        VoxelShape shapeCenter = Block.box(cWidthMin,  0,  cWidthMin, cWidthMax, cHeight, cWidthMax);
        return makeShapes(shapeCenter, connectionSize, base, height);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider
     * @param center The shape for the center
     * @param side The shape for the segments. Should be defined as the NORTH SIDE shape, will be auto-rotated for other sides.
     * @return
     */
    public static VoxelShape[] makeShapes(VoxelShape center, VoxelShape side) {
        VoxelShape south = rotateVoxelShape(side, Direction.SOUTH);
        VoxelShape east = rotateVoxelShape(side, Direction.EAST);
        VoxelShape west = rotateVoxelShape(side, Direction.WEST);
        return makeShapes(center, side, east, south, west);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider
     * @param center A VoxelShape to be used for the center of the shape
     * @param size The expanding point from the center for each segment of the block (e.g. 2 will make a segment 4 wide)
     * @param height The height of the block from the floor
     * @return
     */
    public static VoxelShape[] makeShapes(VoxelShape center, float size, float height) {
        return makeShapes(center, size, 0.0f, height);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider
     * @param shapeCenter A VoxelShape to be used for the center of the shape
     * @param size The expanding point from the center for each segment of the block (e.g. 2 will make a segment 4 wide)
     * @param base The starting point from the floor
     * @param height The height of the block from the base
     * @return
     */
    public static VoxelShape[] makeShapes(VoxelShape shapeCenter, float size, float base, float height) {
        double min = 0;
        double max = 16;
        double widthMin = 8.0F - size;
        double widthEnd = 8.0F + size;
        VoxelShape shapeNorth  = Block.box(widthMin,   base, min,       widthEnd,  height,  widthEnd);
        VoxelShape shapeSouth  = Block.box(widthMin,   base, widthMin,  widthEnd,  height,  max);
        VoxelShape shapeWest   = Block.box(min,        base, widthMin,  widthEnd,  height,  widthEnd);
        VoxelShape shapeEast   = Block.box(widthMin,   base, widthMin,  max,       height,  widthEnd);

        return makeShapes(shapeCenter, shapeNorth, shapeEast, shapeSouth, shapeWest);
    }

    /**
     * Create an array of voxelshapes for a non-standard shaped collider, with unique shapes for each sided connection
     * @param center The center shape
     * @param north The north shape
     * @param east The east shape
     * @param south The south shape
     * @param west The west shape
     * @return
     */
    public static VoxelShape[] makeShapes(VoxelShape center, VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        VoxelShape northEast = Shapes.or(north, east);
        VoxelShape southWest = Shapes.or(south, west);
        VoxelShape[] shapeArray = new VoxelShape[]{
                Shapes.empty(),
                south,
                west,
                southWest,
                north,
                Shapes.or(south, north),
                Shapes.or(west, north),
                Shapes.or(southWest, north),
                east,
                Shapes.or(south, east),
                Shapes.or(west, east),
                Shapes.or(southWest, east),
                northEast,
                Shapes.or(south, northEast),
                Shapes.or(west, northEast),
                Shapes.or(southWest, northEast)
        };

        for(int i = 0; i < shapeArray.length; ++i) {
            shapeArray[i] = Shapes.or(center, shapeArray[i]);
        }

        return shapeArray;
    }

    public static VoxelShape[] combineMultiShapes(VoxelShape[] shapesA, VoxelShape[] shapesB) {
        if (shapesA.length != shapesB.length) {
            Debug.LogError("Attempting to combine multi shapes, but arrays were not equal length. A: %s, B: %s", shapesA.length, shapesB.length);
            return shapesA;
        }

        VoxelShape[] newShape = new VoxelShape[shapesA.length];

        for (int i = 0; i < shapesA.length; i++) {
            newShape[i] = Shapes.or(shapesA[i], shapesB[i]);
        }

        return newShape;
    }

    public static VoxelShape getShapeAtIndex(BlockState state, VoxelShape[] shapes) {
        return shapes[getAABBIndex(state)];
    }

    /**
     * Rotate a VoxelShape horizontally to the new direction. Assumes the passed one is facing north,
     * so east is 90, south 180 and west 270 degrees.
     * @param shape Original shape
     * @param newDirection Target direction
     * @return New shape
     */
    public static VoxelShape rotateVoxelShape(VoxelShape shape, Direction newDirection) {
        if (newDirection != Direction.NORTH) {
            double xMin = shape.min(Direction.Axis.X);
            double xMax = 1 - shape.max(Direction.Axis.X);
            double yMin = shape.min(Direction.Axis.Y);
            double yMax = 1 - shape.max(Direction.Axis.Y);
            double zMin = shape.min(Direction.Axis.Z);
            double zMax = 1 - shape.max(Direction.Axis.Z);
            switch (newDirection) {
                case EAST:
                    return createNewShape(zMax, yMin, xMin, zMin, yMax, xMax);
                case SOUTH:
                    return createNewShape(xMax, yMin, zMax, xMin, yMax, zMin);
                case WEST:
                    return createNewShape(zMin, yMin, xMax, zMax, yMax, xMin);
            }
        }
        return shape;
    }

    private static VoxelShape createNewShape(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        return Block.box(xMin * 16, yMin * 16, zMin * 16, 16 - (xMax * 16), 16 - (yMax * 16), 16 - (zMax * 16));
    }

    /**
     * Get a VoxelShape and convert it to a printable string with the min/max points of each axis.
     * @param shape VoxelShape
     * @return A string to print with the logger
     */
    public static String getPrintableShape(VoxelShape shape) {
        return "X: " + shape.min(Direction.Axis.X) + " - " + shape.max(Direction.Axis.X) +
                " Y: " + shape.min(Direction.Axis.Y) + " - " + shape.max(Direction.Axis.Y) +
                " Z: " + shape.min(Direction.Axis.Z) + " - " + shape.max(Direction.Axis.Z);
    }

    //Borrowed from CrossCollisionBox
    private static int getAABBIndex(BlockState state) {
        return stateToIndex.computeIntIfAbsent(state, mappedState -> {
            int i = 0;
            if (mappedState.getValue(BlockStateProperties.NORTH)) {
                i |= indexFor(Direction.NORTH);
            }

            if (mappedState.getValue(BlockStateProperties.EAST)) {
                i |= indexFor(Direction.EAST);
            }

            if (mappedState.getValue(BlockStateProperties.SOUTH)) {
                i |= indexFor(Direction.SOUTH);
            }

            if (mappedState.getValue(BlockStateProperties.WEST)) {
                i |= indexFor(Direction.WEST);
            }

            return i;
        });
    }

    //Borrowed from CrossCollisionBox
    private static int indexFor(Direction p_52344_) {
        return 1 << p_52344_.get2DDataValue();
    }
}
