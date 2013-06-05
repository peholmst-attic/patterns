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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A support class designed to be used by {@link Adaptable} classes
 * as a delegate. This class is serializable if all the registered adapters
 * are serializable.
 *
 * @author petter@vaadin.com
 */
public class AdaptableSupport implements Adaptable, Serializable {

    private final Map<Class<?>, Object> adapters = new HashMap<>();

    @Override
    public boolean supportsAdapter(Class<?> adapterClass) {
        assert adapterClass != null : "adapterClass must not be null";
        return adapters.containsKey(adapterClass);
    }

    @Override
    public <T> T adapt(Class<T> adapterClass) {
        assert adapterClass != null : "adapterClass must not be null";
        final T adapter = adapterClass.cast(adapters.get(adapterClass));
        if (adapter == null) {
            throw new UnsupportedAdapterException(adapterClass);
        }
        return adapter;
    }

    /**
     * Adds an {@code adapter} to the specified {@code adapterClass}.
     */
    public <T> void addAdapter(Class<? super T> adapterClass, T adapter) {
        assert adapterClass != null : "adapterClass must not be null";
        assert adapter != null : "adapter must not be null";
        adapters.put(adapterClass, adapter);
    }

    /**
     * Removes the adapter to the specified {@code adapterClass}.
     */
    public void removeAdapter(Class<?> adapterClass) {
        assert adapterClass != null : "adapterClass must not be null";
        adapters.remove(adapterClass);
    }
}
