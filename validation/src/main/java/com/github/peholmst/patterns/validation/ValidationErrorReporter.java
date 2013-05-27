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
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.Serializable;
import java.util.*;

/**
 * This class is used to interpolate and report validation errors to the user. The interpolated messages
 * are sent to all registered {@link Target}s, who in turn display them to the user in some way. Both the
 * locale and the {@link MessageInterpolator} can be customized.
 *
 * @author petter@vaadin.com
 */
public class ValidationErrorReporter implements Serializable {

    private Locale locale = Locale.getDefault();
    private transient MessageInterpolator messageInterpolator;
    private Set<Target> targets = new HashSet<>();

    /**
     * Interface defining an error reporter target that is used to
     * notify the user of the validation errors.
     */
    public interface Target extends Serializable {
        /**
         * Clears all validation errors from the target.
         */
        void clearValidationErrors();

        /**
         * Shows the specified validation errors in the target.
         */
        void setValidationErrors(Collection<ValidationErrorMessage> errorMessages);
    }

    /**
     * Data structure for the validation error messages of a single property.
     */
    public static class ValidationErrorMessage implements Serializable {
        private final String propertyPath;
        private final List<String> messages = new ArrayList<>();
        private final List<String> unmodifiableMessages = Collections.unmodifiableList(messages);

        private ValidationErrorMessage(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        /**
         * Returns the full path of the property containing the invalid value.
         */
        public String getPropertyPath() {
            return propertyPath;
        }

        /**
         * The interpolated error messages.
         */
        public List<String> getMessages() {
            return unmodifiableMessages;
        }

        private void addMessage(String message) {
            messages.add(message);
        }
    }

    /**
     * Creates error messages for the specified {@code violations} using {@link #getMessageInterpolator()}
     * and forwards them to all registered targets.
     */
    public void setValidationErrors(Set<? extends ConstraintViolation<?>> violations) {
        assert violations != null : "violations must not be null";
        clearValidationErrors();
        final Map<String, ValidationErrorMessage> validationErrorMessageMap = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            ValidationErrorMessage errorMessage = validationErrorMessageMap.get(propertyPath);
            if (errorMessage == null) {
                errorMessage = new ValidationErrorMessage(propertyPath);
                validationErrorMessageMap.put(propertyPath, errorMessage);
            }
            errorMessage.addMessage(interpolateMessage(violation));
        }

        for (Target target : new LinkedList<>(targets)) {
            target.setValidationErrors(new HashSet<>(validationErrorMessageMap.values()));
        }
    }

    private String interpolateMessage(final ConstraintViolation<?> violation) {
        return getMessageInterpolator().interpolate(violation.getMessageTemplate(), new MessageInterpolator.Context() {
            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return violation.getConstraintDescriptor();
            }

            @Override
            public Object getValidatedValue() {
                return violation.getInvalidValue();
            }
        }, locale);
    }

    /**
     * Returns the {@link MessageInterpolator} used when creating the validation error messages.
     * If no interpolator has been explicitly set, the default is used.
     *
     * @see javax.validation.ValidatorFactory#getMessageInterpolator()
     */
    public MessageInterpolator getMessageInterpolator() {
        if (messageInterpolator == null) {
            messageInterpolator = createDefaultMessageInterpolator();
        }
        return messageInterpolator;
    }

    /**
     * Sets the {@link MessageInterpolator} to use when creating the validation error messages.
     * Please note that the interpolator is stored in a transient field, so it needs to be reset
     * after deserialization.
     *
     * @param messageInterpolator the message interpolator to use, or {@code null} to use the default.
     */
    public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
    }

    private MessageInterpolator createDefaultMessageInterpolator() {
        return Validation.buildDefaultValidatorFactory().getMessageInterpolator();
    }

    /**
     * Adds the specified {@code target} to this error reporter. The target will
     * get notified the next time any error messages are set or cleared.
     */
    public void addTarget(Target target) {
        assert target != null : "target must not be null";
        targets.add(target);
    }

    /**
     * Removes the specified {@code target} from this error reporter.
     */
    public void removeTarget(Target target) {
        assert target != null : "target must not be null";
        targets.remove(target);
    }

    /**
     * Same as calling {@link #setValidationErrors(java.util.Set)} and passing in
     * {@link com.github.peholmst.patterns.validation.ValidationException#getViolations()} as the parameter.
     */
    public void setValidationErrors(ValidationException exception) {
        assert exception != null : "exception must not be null";
        setValidationErrors(exception.getViolations());
    }

    /**
     * Clears all the validation errors from all the targets.
     */
    public void clearValidationErrors() {
        for (Target target : new LinkedList<>(targets)) {
            target.clearValidationErrors();
        }
    }

    /**
     * Sets the locale to use when creating the error messages. Please note that this
     * will not affect already generated error messages.
     *
     * @param locale the locale, or {@code null} to use the default locale.
     */
    public void setLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        this.locale = locale;
    }

    /**
     * Returns the locale to use when creating the error messages (never {@code null}).
     */
    public Locale getLocale() {
        return locale;
    }

}
