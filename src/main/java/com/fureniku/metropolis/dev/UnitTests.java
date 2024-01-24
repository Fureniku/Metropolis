package com.fureniku.metropolis.dev;

import com.fureniku.metropolis.Metropolis;
import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    @Test
    public void isTestContentDisabled() {
        Assert.assertFalse(Metropolis.ENABLE_DEBUG);
    }
}
