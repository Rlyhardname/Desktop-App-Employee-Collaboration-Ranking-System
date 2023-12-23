package com.sirma.academy.dao;

import com.sirma.academy.model.Employee;
import com.sirma.academy.model.util.EntityFactory;
import com.sirma.academy.exception.EntityFactoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<Employee, Long> {
    private DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Employee> findBy(Employee object) {
        return Optional.empty();
    }

    @Override
    public Optional<Employee> findById(Long idArg) {
        String sql = "SELECT * FROM Employee " +
                "WHERE id=?";
        ResultSet rs = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {
            prepStatement.setLong(1, idArg);
            rs = prepStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                try {
                    return Optional.of(EntityFactory.newEmployee(id));
                } catch (EntityFactoryException e) {
                    System.err.println(getClass().getName() + " line 48: " + e.getMessage());
                    // log e.getMessage()
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM Employee";
        List<Employee> aEmployees = new ArrayList<>();
        ResultSet rs = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                long id = rs.getLong(1);
                try {
                    aEmployees.add(EntityFactory.newEmployee(id));
                } catch (EntityFactoryException e) {
                    System.err.println(getClass().getName() + " line 82: " + e.getMessage());
                    // log e.getMessage()
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println(getClass().getName() + " line 93: " + e.getMessage());
                throw new RuntimeException(e);
            }

        }

        return aEmployees;
    }

    @Override
    public int save(Employee aEmployee) {
        String sql = "INSERT INTO Employee (id) " +
                "VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aEmployee.getId());
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                System.out.println("Something went wrong!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // not sure if this will get thrown
            System.err.println(getClass().getName() + " line 115: " + e.getMessage());
            System.out.println("Id already exists in DB!");
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public int saveAll(List<Employee> aEmployees) {
        if (aEmployees.size() == 0) {
            return -1;
        }
        String sql = "INSERT INTO Employee (id)" +
                "VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Employee aEmployee : aEmployees) {
                java.lang.Long id = aEmployee.getId();
                if (id != null) {
                    preparedStatement.setLong(1, id);
                    preparedStatement.addBatch();
                }

            }

            int[] batchSize = preparedStatement.executeBatch();
            if (batchSize.length != aEmployees.size()) {
                System.out.println(getClass().getName() + " line 142: Some records weren't saved!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println(getClass().getName() + " line 144: " + e.getMessage());
            System.out.println("Id already exists in DB!");
        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println(getClass().getName()+ " line 147: "+e.getMessage());
            //throw new RuntimeException(e);
        }

        return 1;
    }

    // Add 1-2 more fields like name and country or phone number to have single update and updateAll crud..
    @Override
    public void update(Long id, Employee aEmployee) {
        String sql = "UPDATE Employee " +
                "SET id=? " +
                "WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aEmployee.getId());
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAll(List<Employee> aEmployees, String Country) {

    }

    @Override
    public int deleteById(java.lang.Long id) {
        String sql = "DELETE FROM Employee " +
                "WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                System.out.println(getClass().getName() + " line 175: User with such id doesn't exist!");
                return -1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public void deleteAll(List<Employee> aEmployees) {
        String sql = "DELETE FROM Employee" +
                "WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Employee aEmployee : aEmployees) {
                preparedStatement.setLong(1, aEmployee.getId());
                preparedStatement.addBatch();
            }
            int[] batchSize = preparedStatement.executeBatch();
            if (batchSize.length != aEmployees.size()) {
                System.out.println(getClass().getName() + " Some users weren't removed or didn't exist at all!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(java.lang.Long id) {
        String sql = "SELECT * FROM Employee " +
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
            // throw when user id doesn't exist
            System.err.println(getClass().getName() + " line 216: " + e.getMessage());
            //throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
