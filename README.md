DbDiff
========================

I need a tool that can generate MySQL / MariaDB migration scripts.
Looking around on the internet I found some database diffing code by Vecna Technologies.
This code needed significant cleanup to make work but it was a start.

The code doesn't work yet.
Migration script generation is in early stages.
Known issues:
- Only code for CREATE TABLE has been written
- Collate and charset information is not available in JDBC metadata, currently the generator hard codes utf8mb4
- Foreign key info is not included in the CREATE TABLE statement
- Some data types show the wrong size, not clear why this is
- No support for extending beyond MySQL

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
