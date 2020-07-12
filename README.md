DbDiff
========================

Original code by Vecna Technologies.
After a bunch of cleanup and rewriting I got it to work.
Only to discover that the code doesn't generate migration scripts.
It can only report differences in human readable form.

I've given up on this tool for now.
Maybe later I'll pick it up again to make it generate migration scripts.

About
-----------------

Database Diff Tool is a library for comparing database schemas.

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

* RelationalDatabaseBeanImpl: reads the schema from a live database.
* RdbDiffEngine: compares two database schemas.

Tools
-----------------

There are two runnable classes under the tools module:

* dbdiff.tools.MakeReferenceDatabase
 - Use this tool to create a new reference database file. 
 - The db file is a serialized version of the internal program db model class. 
 - The file to be created is specified with a command-line argument. By default, it is 'myDb.ser'.
 - Refer to main method's javadoc for parameters to supply (eg DB connection params)
* com.vecna.dbDiff.tools.CompareDatabase
 - Use this tool to read in a reference db file created by the first program and compare a live DB to it
 - Specify db connection params for the to-be-tested db using command line (refer to javadoc for paramaters to supply)
 - Any database differences are output to the console in human-readable results

Credits
-----------------

Originally developed by Vecna Technologies, Inc. and open sourced as part of its community service program. See the LICENSE file for more details.
Vecna Technologies encourages employees to give 10% of their paid working time to community service projects. 
To learn more about Vecna Technologies, its products and community service programs, please visit http://www.vecna.com.
