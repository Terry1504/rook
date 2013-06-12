# rook <a href="http://travis-ci.org/shyiko/rook"><img src="https://secure.travis-ci.org/shyiko/rook.png" alt="Build Status" style="max-width:100%;"></a>

Change Data Capture (CDC) toolkit for keeping system layers in sync with the database.

Out-of-box rook includes support for MySQL as a source and Hibernate cache (query & 2nd level) as a target.

Currently rook-source-mysql contains code of [open-replicator](https://code.google.com/p/open-replicator). This is going to be changed after v0.1.0 release.

Examples
---------------

Automatic eviction of Hibernate Query & Second Level caches in case of MySQL Master-Slave setup<br/>
[supplement/integration-testing/hibernate-cache-over-mysql](https://github.com/shyiko/rook/tree/master/supplement/integration-testing/hibernate-cache-over-mysql)

License
---------------

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)