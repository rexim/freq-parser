# Parser of HTML logs produced by freqbot #

Why? For the glory of [Horta](https://github.com/codingteam/horta-hell/) of course!

# Usage #

Download the logs using `wget -m` and put them in a folder from where
you're going to execture the convertor. Put a prepared h2 database
file in the same folder. The database should contains `log` table
defined like
[this](https://github.com/codingteam/horta-hell/blob/master/src/main/resources/db/log/V2__Create-log-table.sql). And
finally do `sbt run`. The convertor recursively finds all files that
match `\d{2}\.html` and converts them into the database.

**NOTE:** due to naive implementation of /me messages parsing it is
recommended to convert one conference at a time.
