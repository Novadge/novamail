dataSource {
    pooled = true
    driverClassName ="com.mysql.jdbc.Driver"// "org.h2.Driver"
    username = "novadge"
    password = "\$Money38"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    dbCreate = 'update'
   // url = 'jdbc:h2:mem:testDb'
    url = "jdbc:mysql://localhost:3306/novamail"
}

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
