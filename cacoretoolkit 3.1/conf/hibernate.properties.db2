######################
### Query Language ###
######################

## define query language constants / function names

hibernate.query.substitutions true 1, false 0, yes 'Y', no 'N'



#################
### Platforms ###
#################


## DB2

hibernate.dialect org.hibernate.dialect.DB2Dialect
hibernate.connection.driver_class COM.ibm.db2.jdbc.net.DB2Driver
hibernate.connection.url jdbc:db2:/@SERVERNAME@/@DBNAME@
hibernate.connection.username @DBUSER@
hibernate.connection.password @DBPASSWD@



#################################
### Hibernate Connection Pool ###
#################################

hibernate.connection.pool_size 1



###########################
### C3P0 Connection Pool###
###########################

#hibernate.c3p0.max_size 2
#hibernate.c3p0.min_size 2
#hibernate.c3p0.timeout 5000
#hibernate.c3p0.max_statements 100
#hibernate.c3p0.idle_test_period 3000
#hibernate.c3p0.acquire_increment 2
#hibernate.c3p0.validate false



##############################
### Proxool Connection Pool###
##############################

## Properties for external configuration of Proxool

hibernate.proxool.pool_alias pool1

## Only need one of the following

#hibernate.proxool.existing_pool true
#hibernate.proxool.xml proxool.xml
#hibernate.proxool.properties proxool.properties



#################################
### Plugin ConnectionProvider ###
#################################

## use a custom ConnectionProvider (if not set, Hibernate will choose a built-in ConnectionProvider using hueristics)

#hibernate.connection.provider_class org.hibernate.connection.DriverManagerConnectionProvider
#hibernate.connection.provider_class org.hibernate.connection.DatasourceConnectionProvider
#hibernate.connection.provider_class org.hibernate.connection.C3P0ConnectionProvider
#hibernate.connection.provider_class org.hibernate.connection.DBCPConnectionProvider
#hibernate.connection.provider_class org.hibernate.connection.ProxoolConnectionProvider



#######################
### Transaction API ###
#######################

## the Transaction API abstracts application code from the underlying JTA or JDBC transactions

#hibernate.transaction.factory_class org.hibernate.transaction.JTATransactionFactory
#hibernate.transaction.factory_class org.hibernate.transaction.JDBCTransactionFactory


## to use JTATransactionFactory, Hibernate must be able to locate the UserTransaction in JNDI
## default is java:comp/UserTransaction
## you do NOT need this setting if you specify hibernate.transaction.manager_lookup_class

#jta.UserTransaction jta/usertransaction
#jta.UserTransaction javax.transaction.UserTransaction
#jta.UserTransaction UserTransaction


## to use the second-level cache with JTA, Hibernate must be able to obtain the JTA TransactionManager

#hibernate.transaction.manager_lookup_class org.hibernate.transaction.JBossTransactionManagerLookup
#hibernate.transaction.manager_lookup_class org.hibernate.transaction.WeblogicTransactionManagerLookup
#hibernate.transaction.manager_lookup_class org.hibernate.transaction.WebSphereTransactionManagerLookup
#hibernate.transaction.manager_lookup_class org.hibernate.transaction.OrionTransactionManagerLookup
#hibernate.transaction.manager_lookup_class org.hibernate.transaction.ResinTransactionManagerLookup



##############################
### Miscellaneous Settings ###
##############################

## print all generated SQL to the console

hibernate.show_sql @SHOWSQL@


## add comments to the generated SQL

#hibernate.use_sql_comments true


## generate statistics

#hibernate.generate_statistics true


## auto schema export

#hibernate.hbm2ddl.auto create-drop
#hibernate.hbm2ddl.auto create
#hibernate.hbm2ddl.auto update


## rollback generated identifier values of deleted entities to default values

#hibernate.use_identifer_rollback true


## specify a default schema and catalog for unqualified tablenames

#hibernate.default_schema test
#hibernate.default_catalog test


## set the maximum depth of the outer join fetch tree

hibernate.max_fetch_depth 1


## enable CGLIB reflection optimizer (enabled by default)

hibernate.cglib.use_reflection_optimizer false


## use a custom stylesheet for XML generation (if not specified, hibernate-default.xslt will be used)

#hibernate.xml.output_stylesheet C:/Hibernate/net/sf/hibernate/hibernate-default.xslt



#####################
### JDBC Settings ###
#####################

## specify a JDBC isolation level

#hibernate.connection.isolation 4


## enable JDBC autocommit (not recommended!)

#hibernate.connection.autocommit true


## set the JDBC fetch size

#hibernate.jdbc.fetch_size 25


## set the maximum JDBC 2 batch size (a nonzero value enables batching)

#hibernate.jdbc.batch_size 5


## enable batch updates even for versioned data

hibernate.jdbc.batch_versioned_data true


## enable use of JDBC 2 scrollable ResultSets (specifying a Dialect will cause Hibernate to use a sensible default)

#hibernate.jdbc.use_scrollable_resultset true


## use streams when writing binary types to / from JDBC

hibernate.jdbc.use_streams_for_binary true


## use JDBC 3 PreparedStatement.getGeneratedKeys() to get the identifier of an inserted row

#hibernate.jdbc.use_get_generated_keys false



##########################
### Second-level Cache ###
##########################

## optimize chache for minimal "puts" instead of minimal "gets" (good for clustered cache)

#hibernate.cache.use_minimal_puts true


## set a prefix for cache region names

#hibernate.cache.region_prefix hibernate.test


## disable the second-level cache

hibernate.cache.use_second_level_cache @SECONDLEVELCACHE@



## enable the query cache

hibernate.cache.use_query_cache false


## choose a cache implementation

hibernate.cache.provider_class org.hibernate.cache.EhCacheProvider
#hibernate.cache.provider_class org.hibernate.cache.EmptyCacheProvider
#hibernate.cache.provider_class org.hibernate.cache.HashtableCacheProvider
#hibernate.cache.provider_class org.hibernate.cache.TreeCacheProvider
#hibernate.cache.provider_class org.hibernate.cache.OSCacheProvider
#hibernate.cache.provider_class org.hibernate.cache.SwarmCacheProvider



############
### JNDI ###
############

## specify a JNDI name for the SessionFactory

#hibernate.session_factory_name hibernate/session_factory


## Hibernate uses JNDI to bind a name to a SessionFactory and to look up the JTA UserTransaction;
## if hibernate.jndi.* are not specified, Hibernate will use the default InitialContext() which
## is the best approach in an application server

#file system
#hibernate.jndi.class com.sun.jndi.fscontext.RefFSContextFactory
#hibernate.jndi.url file:/

#WebSphere
#hibernate.jndi.class com.ibm.websphere.naming.WsnInitialContextFactory
#hibernate.jndi.url iiop://localhost:900/

