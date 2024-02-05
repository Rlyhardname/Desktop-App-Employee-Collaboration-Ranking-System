package com.sirma.academy.dao;

import com.sirma.academy.model.Project;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;


public class ProjectDAO implements DAO<Project, Long> {

    private DataSource dataSource;

    public ProjectDAO(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Project> findBy(Project object) {
        return Optional.empty();
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        return null;
    }

    @Override
    public int save(Project object) {
        return 1;
    }

    @Override
    public int saveAll(List<Project> projects) {
        if (projects.size() == 0) {
            return -1;
        }
        String sql = "INSERT INTO Project (id)" +
                "VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Project project : projects) {
                Long id = project.id();
                if (id != null) {
                    preparedStatement.setLong(1, id);
                    preparedStatement.addBatch();
                }

            }

            int[] batchSize = preparedStatement.executeBatch();
            if (batchSize.length != projects.size()) {
                System.out.println(getClass().getName() + " line 142: Some records weren't saved!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            System.err.println(getClass().getName() + " line 73: " + e.getMessage());
            System.out.println("Id already exists in DB!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public void update(Long id, Project object) {

    }

    @Override
    public void updateAll(List<Project> object1, String object2) {

    }

    @Override
    public int deleteById(Long id) {
        return 1;
    }

    @Override
    public void deleteAll(List<Project> objects) {

    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT * FROM Project " +
                "WHERE id=?";
        ResultSet rs = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println(getClass().getName() + " line 91: " + e.getMessage());
            //throw when id doesn't exist.
            //  throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    public void truncate() {
        String disableChecks = "SET FOREIGN_KEY_CHECKS=0";
        String enableChecks = "SET FOREIGN_KEY_CHECKS=1";
        String sql = "TRUNCATE TABLE project";
        try (Connection con = dataSource.getConnection();
             Statement prep = con.createStatement()) {
            prep.addBatch(disableChecks);
            prep.addBatch(sql);
            prep.addBatch(enableChecks);
            prep.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
