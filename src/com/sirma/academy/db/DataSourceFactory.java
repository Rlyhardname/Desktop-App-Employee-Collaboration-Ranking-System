package com.sirma.academy.db;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public record DataSourceFactory(DataBaseConfiguration dataBaseConfiguration) {

    public MysqlDataSource newMysqlDataSource() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName(dataBaseConfiguration.getName());
        dataSource.setURL(dataBaseConfiguration.getUrl());
        dataSource.setUser(dataBaseConfiguration.getUser());
        dataSource.setPassword(dataBaseConfiguration.getPassword());
        return dataSource;
    }

}
