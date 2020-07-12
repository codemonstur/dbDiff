package dbdiff.business.catalogschema;

import dbdiff.pojos.db.CatalogSchema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL Server catalog/schema resolver. Defaults to dbo/database name.
 * Database name is parsed from the jdbc url.
 */
class SQLServerCatalogSchemaResolver implements CatalogSchemaResolver {

    @Override
    public CatalogSchema resolveCatalogSchema(String jdbcDriver, String jdbcUrl) {
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("jdbc url is not defined");
        }
        Matcher m = Pattern.compile(".*;DatabaseName=(.*?)(;.*)?").matcher(jdbcUrl);
        if (m.matches()) {
            return new CatalogSchema("dbo", m.group(1));
        } else {
            throw new IllegalArgumentException("jdbc url " + jdbcUrl + " doesn't match the SQL server pattern, "
                    + "cannot determine schema name");
        }
    }
}
