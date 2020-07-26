package dbdiff;

import dbdiff.pojos.compare.Difference;

import java.util.List;

public enum MysqlMigration {;

    public static String newMigrationScript(final List<Difference> differences) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nSET FOREIGN_KEY_CHECKS = 0;");


        for (final var diff : differences) {
            builder.append(diff.migrationQuery);
            System.out.println(diff.location + " - " + diff.errorType + " - " + diff.message);
        }

        builder.append("SET FOREIGN_KEY_CHECKS = 1;\n");
        return builder.toString();
    }

}
