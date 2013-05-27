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

package com.github.peholmst.patterns.validation;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * This is an exception that is thrown when an entity violates one or more validation (JSR-303) constraints.
 * Backend classes may use this to report validation errors to the frontend.
 *
 * @author petter@vaadin.com
 */
public class ValidationException extends RuntimeException {

    private final Set<? extends ConstraintViolation<?>> violations;

    /**
     * Creates a new {@code ValidationException}.
     *
     * @param violations a set containing at least one {@code ConstraintViolation}.
     */
    public <T> ValidationException(Set<ConstraintViolation<T>> violations) {
        assert violations != null : "violations must not be null";
        assert !violations.isEmpty() : "violations must not be empty";
        this.violations = new HashSet<>(violations);
    }

    /**
     * Returns a set of all constraint violations (never empty).
     */
    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }

    /**
     * Throws a new {@link ValidationException} if there are at least one violation in the {@code violations} set.
     * <p/>
     * The idea behind this method is to directly pass in the result of the {@link javax.validation.Validator#validate(Object, Class[])} method.
     * If any constraints were violated, an exception will be thrown. Otherwise, nothing happens.
     *
     * @param violations a possibly empty set of violations.
     */
    public static <T> void throwIfNonEmpty(Set<ConstraintViolation<T>> violations) {
        if (violations.size() > 0) {
            throw new ValidationException(violations);
        }
    }

}
