package com.fureniku.metropolis.enums;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.blocks.decorative.MetroBlockConnectingHorizontal;

/**
 * Types of connection a connection decorative block can use<br>
 * {@link #SAME}<br>
 * {@link #SAMECLASS}<br>
 * {@link #TAG}<br>
 * {@link #CONNECTING}<br>
 * {@link #METRO}<br>
 * {@link #SOLID}<br>
 * {@link #SOLID_SAME}<br>
 * {@link #SOLID_SAMECLASS}<br>
 * {@link #SOLID_TAG}<br>
 * {@link #SOLID_CONNECTING}<br>
 * {@link #SOLID_METRO}<br>
 * {@link #ALL}
 */
public enum BlockConnectionType {

    /**
     * Connect to blocks of the same block singleton
     */
    SAME,

    /**
     * Connect to blocks of the same class
     */
    SAMECLASS,

    /**
     * Connect to blocks which have the same tag set on the block singleton ({@link MetroBlockConnectingHorizontal} and subs only)
     */
    TAG,

    /**
     * Connect to any block that extends {@link MetroBlockConnectingHorizontal}
     */
    CONNECTING,

    /**
     * Connect to any block that extends {@link MetroBlockDecorative}
     */
    METRO,

    /**
     * Connect to any solid block face
     */
    SOLID,

    /**
     * Connect to any solid block face, and blocks of the same block singleton
     */
    SOLID_SAME,

    /**
     * Connect to any solid block face, and blocks of the same class
     */
    SOLID_SAMECLASS,

    /**
     * Connect to any solid block face, and blocks which have the same tag set on the block singleton ({@link MetroBlockConnectingHorizontal} and subs only)
     */
    SOLID_TAG,

    /**
     * Connect to any solid block face, and any block that extends {@link MetroBlockConnectingHorizontal}
     */
    SOLID_CONNECTING,

    /**
     * Connect to any solid block face, and any block that extends {@link MetroBlockDecorative}
     */
    SOLID_METRO,

    /**
     * Connect to anything that isn't air
     */
    ALL;

    /**
     * Get the relevant type from a solid check. Use with {@link #isSolid} to handle if a connection is solid (and if not, use this type)
     * @return The adjusted non-solid type, to check.
     */
    public BlockConnectionType getSolidType() {
        switch(this) {
            case SOLID_SAME:
                return SAME;
            case SOLID_SAMECLASS:
                return SAMECLASS;
            case SOLID_TAG:
                return TAG;
            case SOLID_CONNECTING:
                return CONNECTING;
            case SOLID_METRO:
                return METRO;
        }
        return this;
    }

    /**
     * Check if the type should check for a solid connection
     * @return
     */
    public boolean isSolid() {
        if (this == SOLID ||
                this == SOLID_SAME ||
                this == SOLID_SAMECLASS ||
                this == SOLID_TAG ||
                this == SOLID_CONNECTING ||
                this == SOLID_METRO) {
            return true;
        }
        return false;
    }
}
