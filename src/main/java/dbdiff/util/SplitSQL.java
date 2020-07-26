package dbdiff.util;

import java.util.ArrayList;
import java.util.List;

public enum SplitSQL {;

    public static List<String> splitSQL(final String input, final char delimiter) {
        final var list = new ArrayList<String>();

        final char[] data = input.toCharArray();
        int offset = -1;
        while (true) {
            int next = indexOfFirstNotWrapped(data, offset+1, delimiter);
            if (next == -1) {
                list.add(input.substring(offset+1));
                break;
            }
            list.add(input.substring(offset+1, next));
            offset = next;
        }

        return list;
    }

    private static int indexOfFirstNotWrapped(final char[] input, final int offset, final char c) {
        for (int i = offset; true; i++) {
            if (i >= input.length) return -1;
            if (input[i] == c) return i;

            if (input[i] == '\'') i = indexOfUnescaped(input, i+1, '\'');
            else if (input[i] == '"') i = indexOfUnescaped(input, i+1, '"');
            else if (input[i] == '`') i = indexOfUnescaped(input, i+1, '`');
            if (i == -1) return -1;
        }
    }

    private static int indexOfUnescaped(final char[] input, final int offset, final char quoteChar) {
        for (int i = offset; true; i++) {
            if (i >= input.length) return -1;
            if (input[i] == '\\') {
                i++;
                continue;
            }
            if (input[i] == quoteChar) return i;
        }
    }

}
