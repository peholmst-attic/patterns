/*
 * Copyright (c) 2013 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.peholmst.patterns.entities;

import java.lang.reflect.ParameterizedType;

/**
 * Base class for entity builders. Entity builders can be used if the entities
 * should be treated as immutable objects and changes should be made to copies
 * instead of the entities themselves.
 *
 * @param <E> the type of entity built by this builder.
 * @param <B> the type of this builder.
 * @author Petter Holmström
 */
public abstract class AbstractEntityBuilder<E extends AbstractEntity, B extends AbstractEntityBuilder<E, B>> {

    /**
     * The new entity that this builder is building.
     */
    protected E entity;

    /**
     * Creates a new builder working on an empty entity.
     */
    @SuppressWarnings("unchecked")
    public AbstractEntityBuilder() {
        try {
            entity = ((Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Could not create new instance of entity class", ex);
        }
    }

    /**
     * Creates a new builder working on a copy of the original entity.
     *
     * @see #makeTransient()
     *
     * @param original the original entity to copy, is ignored if {@code null}.
     */
    public AbstractEntityBuilder(E original) {
        this();
        if (original != null) {
            entity.setId(original.getId());
            entity.setOptLockVersion(original.getOptLockVersion());
        }
    }

    /**
     * Makes the built entity transient by setting its ID and version number to
     * {@code null}. For new entities, this method need never be called since
     * new entities are transient by default. However, if the builder is working
     * on a copy of an existing entity, this method may come in handy.
     */
    public B makeTransient() {
        entity.setId(null);
        entity.setOptLockVersion(null);
        return myself();
    }

    /**
     * Wrapper method for {@code this} cast to the {@code B} parameter type.
     */
    @SuppressWarnings("unchecked")
    protected B myself() {
        return (B) this;
    }

    /**
     * Returns the built entity. Multiple calls of this method on the same
     * builder instance will return the same entity instance.
     */
    public E build() {
        return entity;
    }
}
