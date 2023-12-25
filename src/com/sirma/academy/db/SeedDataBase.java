package com.sirma.academy.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SeedDataBase {
    public SeedDataBase(DataSource dataSource, DataBaseConfiguration dataBaseConfiguration, String dataBaseName) {
        String createDB = "CREATE DATABASE IF NOT EXISTS " + dataBaseConfiguration.getName() ;
        boolean exists = true;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createDB);
        } catch (SQLException e) {
            System.err.println("DB url not existing yet...");
            exists = false;
        }

        if(!exists){
            ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://localhost/");
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
            statement.execute(createDB);
            exists=true;
            ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://localhost/"+dataBaseName);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        String createTableEmployee = "CREATE TABLE IF NOT EXISTS " + dataBaseConfiguration.getName()  + ".employee (" +
                "id BIGINT, " +
                "PRIMARY KEY (id))";
        String createProject = "CREATE TABLE IF NOT EXISTS " + dataBaseConfiguration.getName()  + ".project (" +
                "id BIGINT, " +
                "PRIMARY KEY (id))";
        String employee_Project = "CREATE TABLE IF NOT EXISTS " + dataBaseConfiguration.getName()  + ".employee_Project (" +
                "id INT, " +
                "emp_id BIGINT NOT NULL, " +
                "project_id BIGINT NOT NULL, " +
                "start_Date DATE NOT NULL, " +
                "leave_Date DATE, " +
                "PRIMARY KEY(id), " +
                "CONSTRAINT constraint_emp_id_update FOREIGN KEY (emp_id) REFERENCES employee(id) ON UPDATE CASCADE, " +
                "CONSTRAINT constraint_emp_id_delete FOREIGN KEY (emp_id) REFERENCES employee(id) ON DELETE CASCADE, " +
                "CONSTRAINT constraint_project_id_update FOREIGN KEY (project_id) REFERENCES project(id) ON UPDATE CASCADE, " +
                "CONSTRAINT constraint_project_id_delete FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE)";




        if(exists){
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.addBatch(createTableEmployee);
                statement.addBatch(createProject);
                statement.addBatch(employee_Project);
                statement.executeBatch();


            } catch (SQLException e) {
                System.err.println("Look at SeedConfiguration");
                throw new RuntimeException(e);
            }
        }


    }
}
