package com.sirma.academy.start;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sirma.academy.db.*;

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
                "finalProjectMock",
                "jdbc:mysql://localhost/finalProjectMock",
                "{DB_USER}",
                "{DB_PASSWORD}"
        ));

        MysqlDataSource dataSource = (MysqlDataSource) DataSourcePool.instanceOf(dataSourceFactory.newMysqlDataSource());
        try{
           // new SeedDataBase(dataSource,dataSourceFactory.dataBaseConfiguration());
            new SeedDBwithData();
        }catch (RuntimeException e) {

        }



    }
}