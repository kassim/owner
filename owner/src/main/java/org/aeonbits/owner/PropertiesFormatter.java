package org.aeonbits.owner;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * An enum of possible formatters that can be used to apply arguments to a format string specified in a property.
 * By default Owner uses a {@link #StringFormatter}.
 *
 * @author Kassim Maguire
 */
public enum PropertiesFormatter {
    /**
     * A formatter that uses the {@link java.lang.String#format(String, Object...)} method in which to produce the formatted output.
     * This uses the notation specified by the {@link java.util.Formatter} class
     */
    StringFormatter(new StringFormatter()),
    /**
     * A formatter that uses the {@link java.text.MessageFormat#format(String, Object...)} method in which to produce the formatted output.
     * This uses the notation specified by the {@link java.text.MessageFormat} class
     */
    MessageFormatter(new MessageFormatter());


    private final Formatter formatter;

    PropertiesFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public String format(String pattern, Object... args) {
        return formatter.format(pattern, args);
    }

    private static class StringFormatter implements Formatter {
        public String format(String format, Object... args) {
            return String.format(format, args);
        }
    }

    private static class MessageFormatter implements Formatter {
        public String format(String format, Object... args) {
            return MessageFormat.format(format, args);
        }
    }

    private interface Formatter {
        String format(String pattern, Object... args);
    }

    static PropertiesFormatter getPropertiesFormatter(Method method) {
        Class<Config.Formatter> annotation = Config.Formatter.class;

        Config.Formatter methodFormatter = method.getAnnotation(annotation);
        if (methodFormatter != null) return methodFormatter.value();

        Config.Formatter classFormatter = method.getDeclaringClass().getAnnotation(annotation);
        if (classFormatter != null) return classFormatter.value();

        return StringFormatter;
    }
}
