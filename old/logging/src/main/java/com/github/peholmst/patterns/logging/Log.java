package com.github.peholmst.patterns.logging;

import java.text.MessageFormat;

/**
 * Generic logger interface to be used together with CDI. Beans that require
 * access to the logger simply inject it. How the different logging levels are
 * handled depends entirely on the implementation. All levels can use
 * {@link MessageFormat} strings in their messages.
 *
 * @author Petter Holmström
 */
public interface Log {

    void security(String message, Object... arguments);

    void debug(String message, Object... arguments);

    void debug(Throwable throwable, String message, Object... arguments);

    void info(String message, Object... arguments);

    void warning(String message, Object... arguments);

    void warning(Throwable throwable, String message, Object... arguments);

    void error(String message, Object... arguments);

    void error(Throwable throwable, String message, Object... arguments);
}
