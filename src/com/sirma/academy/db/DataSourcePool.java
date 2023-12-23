package com.sirma.academy.db;

import javax.sql.DataSource;

public class DataSourcePool {
    private static DataSource dataSource;

    public static DataSource instanceOf(DataSource dSource) {
        if (dataSource == null) {
            dataSource = dSource;
        }
        return dataSource;
    }
    public static DataSource instanceOf() {
        return dataSource;
    }


}
