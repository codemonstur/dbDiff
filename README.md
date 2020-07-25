DbDiff
========================

Original code by Vecna Technologies.
After a bunch of cleanup and rewriting I got it to work.
Only to discover that the code doesn't generate migration scripts.
It can only report differences in human readable form.

I've been rewriting the code some more.
Its getting to a point where I could add the migration logic.


Features
-----------------
* Can compare differences between database schemas:
 - Missing or unexpected tables
 - Missing or unexpected columns
 - Checks and compares column names, types, nullability, defaults, and sizes
 - Missing or unexpected foreign keys
 - Missing or unexpected primary keys/indices/unique constraints
* Database Independent
  - Uses JDBC MetaData class - as long as you have a JDBC driver and a solid MetaData implementation, you should be fine.

Core API
-----------------

See the following classes:

* dbdiff.Analyzer: reads the schema from a live database.
* dbdiff.Comparator: compares two database schemas.
