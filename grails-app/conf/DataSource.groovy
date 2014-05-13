//dataSource {
//    pooled = true
//    driverClassName ="com.mysql.jdbc.Driver"// "org.h2.Driver"
//    username = ""
//    password = ""
//    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
//    //dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
//}
//hibernate {
//    cache.use_second_level_cache = true
//    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
//}
//// environment specific settings
//environments {
//    development {
//        dataSource {
//            
//            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:mysql://localhost:3306/novamail"//"jdbc:mysql://novacareplus.c5c4pc5ynwqv.us-east-1.rds.amazonaws.com:3306/novacareplus"//"jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
//        }
//    }
//    test {
//        dataSource {
//            dbCreate = "update"
//            url = "jdbc:mysql://localhost:3306/careplus_test"//"jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
//        }
//    }
//    production {
//        dataSource {
//            
//            dbCreate = "update"
//            url = System.getProperty("JDBC_CONNECTION_STRING")
//            pooled = true
//            properties {
//               maxActive = 800
//               minEvictableIdleTimeMillis=1800000
//               timeBetweenEvictionRunsMillis=1800000
//               numTestsPerEvictionRun=3
//               testOnBorrow=true
//               testWhileIdle=true
//               testOnReturn=true
//               validationQuery="SELECT 1"
//            }
//        }
//    }
//}
//
