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

/**
 * Exception thrown by an {@link Adaptable} when an attempt is made to adapt it
 * to an unsupported adapter class.
 *
 * @author petter@vaadin.com
 */
public class UnsupportedAdapterException extends RuntimeException {

    private final Class<?> adapterClass;

    public UnsupportedAdapterException(Class<?> adapterClass) {
        this.adapterClass = adapterClass;
    }

    /**
     * Returns the unsupported adapter class.
     */
    public Class<?> getAdapterClass() {
        return adapterClass;
    }
}
