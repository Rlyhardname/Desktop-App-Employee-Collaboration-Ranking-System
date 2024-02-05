package com.sirma.academy.dao;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.EmployeeProject;
import com.sirma.academy.model.util.EntityFactory;
import com.sirma.academy.model.Project;
import com.sirma.academy.exception.EntityFactoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EmployeeProjectDAO implements DAO<EmployeeProject, Integer>, EntityDAO<EmployeeProject> {

    private DataSource dataSource;

    public EmployeeProjectDAO(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<EmployeeProject> findBy(EmployeeProject object) {
        return Optional.of(null);
    }

    @Override
    public Optional<EmployeeProject> findById(Integer id) {
        String sql = "SELECT * FROM Employee_Project " + "WHERE id=?";
        ResultSet rs = null;
        java.lang.Long empId;
        java.lang.Long projectId;
        LocalDate startDate;
        LocalDate leaveDate;
        try (Connection connection = dataSource.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(sql)) {
            prepStatement.setLong(1, id);
            rs = prepStatement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            empId = rs.getLong(2);
            projectId = rs.getLong(3);
            startDate = rs.getDate(4).toLocalDate();
            leaveDate = rs.getDate(5).toLocalDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        DAO UserDao = new UserDAO(dataSource);
        Optional<Employee> employee = UserDao.findById(empId);
        if (!employee.isPresent()) {
            // Log employee not in organization anymore???
        }
        DAO projectDAO = new ProjectDAO(dataSource);
        Optional<Project> project = projectDAO.findById(projectId);
        if (!project.isPresent()) {
            // Log project is over???
        }

        EmployeeProject employeeProject = null;
        try {
            employeeProject = EntityFactory.newEmployeeProject(employee.get().getId(), project.get().id(), startDate, leaveDate);
        } catch (EntityFactoryException e) {
            return Optional.empty();
        }

        if (employeeProject.getId() != id.intValue()) {
            try {
                assert (employeeProject.getId() >= -2147483648 && employeeProject.getId() <= 2147483647) : "This should never fail!";
            } catch (AssertionError e) {
                // Log failure and fix data types...
                e.printStackTrace();
                System.out.println("Time to change hash to Long");
                return Optional.empty();
            }
            // Log duplicate hash value...
        }

        return Optional.of(employeeProject);
    }

    @Override
    public List<EmployeeProject> findAll() {
        return null;
    }

    @Override
    public int save(EmployeeProject employeeProject) {
        String sql = "INSERT INTO employee_Project (id,emp_Id,project_Id,start_Date,leave_Date) " + "VALUES (?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeProject.getId());
            preparedStatement.setLong(2, employeeProject.getEmpId());
            preparedStatement.setLong(3, employeeProject.getProjectId());
            preparedStatement.setDate(4, Date.valueOf(employeeProject.getStartDate()));
            LocalDate date = employeeProject.getLeaveDate();
            if (Objects.nonNull(date)) {
                preparedStatement.setDate(5, Date.valueOf(date));
            } else {
                preparedStatement.setNull(5, Types.NULL);
            }


            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                System.out.println("Something went wrong!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            // not sure if this will get thrown
            System.err.println(getClass().getName() + " line 115: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public int saveAll(List<EmployeeProject> employeeProjects) {
        if (employeeProjects.size() == 0) {
            return -1;
        }
        String sql = "INSERT INTO employee_Project (id,emp_Id,project_Id,start_Date,leave_Date)" + "VALUES (?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (EmployeeProject employeeProject : employeeProjects) {
                int hash = employeeProject.getId();
                Long empId = employeeProject.getEmpId();
                Long projectId = employeeProject.getProjectId();
                LocalDate startDate = employeeProject.getStartDate();
                LocalDate leaveDate = employeeProject.getLeaveDate();

                if (empId != null && projectId != null && startDate != null) {
                    preparedStatement.setInt(1, hash);
                    preparedStatement.setLong(2, empId);
                    preparedStatement.setLong(3, projectId);
                    preparedStatement.setDate(4, Date.valueOf(startDate));
                    preparedStatement.setDate(5, Date.valueOf(leaveDate));
                    preparedStatement.addBatch();
                }

            }

            int[] batchSize = preparedStatement.executeBatch();
            if (batchSize.length != employeeProjects.size()) {
                System.out.println(getClass().getName() + " line 142: Some records weren't saved!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            System.err.println(getClass().getName() + " line 144: " + e.getMessage());
            System.out.println("Id already exists in DB!");
        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println(getClass().getName()+ " line 147: "+e.getMessage());
            //throw new RuntimeException(e);
        }

        return 1;

//        if(objects.size()==0){
//            return -1;
//        }
//        String sql = "SELECT * FROM EmployeeProject " +
//                "WHERE empId=? AND projectId=? AND startDate=? AND quitDate=?";
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return 1;
    }

    @Override
    public void update(Long id, EmployeeProject object) {

    }

    @Override
    public void updateAll(List<EmployeeProject> object1, String object2) {

    }

    @Override
    public int deleteById(Integer id) {
        return 1;
    }

    @Override
    public void deleteAll(List<EmployeeProject> objects) {

    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public void truncate() {
        String sql = "TRUNCATE TABLE Employee_Project";
        try(Connection con = dataSource.getConnection();
        PreparedStatement prep = con.prepareStatement(sql)){
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<EmployeeProject> mapOfObjects(Long id) {
        String sql = "SELECT * FROM employee_Project " + "WHERE emp_Id=?";
        ResultSet rs = null;
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                try {
                    Long empId = rs.getLong(2);
                    Long projectId = rs.getLong(3);
                    LocalDate startDate = rs.getDate(4).toLocalDate();
                    LocalDate leaveDate = rs.getDate(5).toLocalDate();
                    employeeProjects.add(EntityFactory.newEmployeeProject(empId, projectId, startDate, leaveDate));
                } catch (EntityFactoryException e) {
                    e.printStackTrace();
                    // log corruptedDB data?
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        return employeeProjects;
    }

}
