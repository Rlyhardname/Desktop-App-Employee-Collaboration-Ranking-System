package com.sirma.academy.start;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sirma.academy.dao.DataBaseConfiguration;
import com.sirma.academy.dao.DataSourceFactory;
import com.sirma.academy.dao.DataSourcePool;
import com.sirma.academy.dao.SeedDBwithData;

/**
 * <pre>
 * @see "you can use this one time to seed the DB with empid's and projectid's for testing,
 * but you will have to configure the db settings a second time"
 * @see "Use the <a href="com/sirma/academy/start/Main#Application.start()"><code>Application.start()<code></a> in the package start.Main()"
 */
@Deprecated
public class entryPoint {
    public static void main(String[] args) {

        DataSourceFactory dataSourceFactory = new DataSourceFactory(new DataBaseConfiguration(
                "{name_placeholder}",
                "{url_placeholder}",
                "{user_placeholder}",
                "{password_placeholder}"
        ));
        MysqlDataSource dataSource = (MysqlDataSource) DataSourcePool.instanceOf(dataSourceFactory.newMysqlDataSource());
        new SeedDBwithData();

    }
}