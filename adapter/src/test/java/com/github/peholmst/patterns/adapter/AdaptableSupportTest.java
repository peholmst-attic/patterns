/*
 * Copyright 2013 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.peholmst.patterns.adapter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for {@link AdaptableSupport}.
 *
 * @author petter@vaadin.com
 */
public class AdaptableSupportTest {

    AdaptableSupport adaptableSupport;

    @Before
    public void setUp() {
        adaptableSupport = new AdaptableSupport();
    }

    @Test
    public void supportsUnsupportedAdapter() {
        adaptableSupport.supportsAdapter(String.class);
    }

    @Test(expected = UnsupportedAdapterException.class)
    public void adaptUnsupportedAdapter() {
        adaptableSupport.adapt(String.class);
    }

    @Test
    public void addAdapter() {
        adaptableSupport.addAdapter(String.class, "Hello World");
        assertTrue(adaptableSupport.supportsAdapter(String.class));
        assertEquals("Hello World", adaptableSupport.adapt(String.class));
    }

    @Test
    public void removeAdapter() {
        adaptableSupport.addAdapter(String.class, "Hello World");
        assertTrue(adaptableSupport.supportsAdapter(String.class));
        adaptableSupport.removeAdapter(String.class);
        assertFalse(adaptableSupport.supportsAdapter(String.class));
    }
}
