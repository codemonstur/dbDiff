package dbdiff.util;

import java.util.Collection;

public enum Validation {;

    public static boolean isNullOrEmpty(final String input) {
        return input == null || input.isEmpty();
    }

    public static <T> boolean isEmpty(final Collection<T> list) {
        return list == null || list.isEmpty();
    }

}
