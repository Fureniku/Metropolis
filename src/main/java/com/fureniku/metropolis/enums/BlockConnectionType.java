package com.fureniku.metropolis.enums;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.blocks.decorative.helpers.ConnectHorizontalHelper;

/**
 * Types of connection a connection decorative block can use<br>
 * {@link #SAME}<br>
 * {@link #SAMECLASS}<br>
 * {@link #TAG}<br>
 * {@link #CONNECTING}<br>
 * {@link #METRO}<br>
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
     * Connect to blocks which have the same tag set on the block singleton
     */
    TAG,

    /**
     * Connect to any block that has a {@link ConnectHorizontalHelper}
     */
    CONNECTING,

    /**
     * Connect to any block that extends {@link MetroBlockDecorative}
     */
    METRO,

    /**
     * Connect to anything that isn't air
     */
    ALL;
}
