package com.sirma.academy.io;

import com.sirma.academy.dao.DAO;
import com.sirma.academy.dao.DataSourcePool;
import com.sirma.academy.dao.ProjectDAO;
import com.sirma.academy.dao.UserDAO;
import com.sirma.academy.exception.EntityFactoryException;
import com.sirma.academy.exception.UnsupportedLocalDateFormatException;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.EmployeeProject;
import com.sirma.academy.model.EntityFactory;
import com.sirma.academy.model.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReaderCSV implements CustomReader {
    List<EmployeeProject> employeeAndProjectList;

    public ReaderCSV() {
        employeeAndProjectList = new LinkedList<>();
    }

    @Override
    public List<EmployeeProject> read(String uri) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(uri))) {
            String line = bufferedReader.readLine();
            DateTimeFormatter formatter = setupFormatter(line);
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                int successfulEntry = handleLine(line, formatter);
                if (successfulEntry == -1) {
                    System.err.println(getClass().getName()+ " line 34:unsuccessful entry");
                    //log unsuccessful entry
                }

            }
        } catch (UnsupportedLocalDateFormatException e) {
            // log
            System.err.println(getClass().getName()+ " line 40: "+e.getMessage());
            // send User message e.getMessage() th at date format of CSV is corrupted.
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return employeeAndProjectList;
    }

    private DateTimeFormatter setupFormatter(String line) {
        String[] firstLine = line.split(",");
        try {
            DateTimeFormatter formatter = Parser.formatterUsed(firstLine[2]);
            handleLine(line, formatter);
            return formatter;
        } catch (UnsupportedLocalDateFormatException e) {
            throw e;
        }
    }

    private int handleLine(String line, DateTimeFormatter formatter) {
        String[] fields = line.split("\s*,\s*");
        java.lang.Long empId = Parser.parseLong(fields[0]);
        java.lang.Long projectId = Parser.parseLong(fields[1]);
        LocalDate startProjectDate = Parser.parseDate(fields[2], formatter);
        LocalDate quitProjectDate = Parser.parseDate(fields[3], formatter);
        try {
            Employee employee = EntityFactory.newEmployee(empId);
            Project project = EntityFactory.newProject(projectId);
            EmployeeProject employeeProject = EntityFactory.newEmployeeProject(employee.getId(), project.id(), startProjectDate, quitProjectDate);
            employeeAndProjectList.add(employeeProject);
        } catch (EntityFactoryException e) {
            // log
            System.err.println(getClass().getName()+ " line 76: "+e.getMessage());
            return -1;
        }

        return 1;
    }

    public void cleanEntriesWithUnexistingEmployeesOrProjects() {
        Iterator<EmployeeProject> iter = employeeAndProjectList.iterator();
        DAO userDAO = new UserDAO(DataSourcePool.instanceOf());
        DAO projectDAO = new ProjectDAO(DataSourcePool.instanceOf());
        while (iter.hasNext()) {
            EmployeeProject employeeProject = iter.next();
            boolean userExists = userDAO.existsById(employeeProject.getEmpId());
            if (!userExists) {
                iter.remove();
                continue;
            }

            boolean projectExists = projectDAO.existsById(employeeProject.getProjectId());
            if (!projectExists) {
                iter.remove();
            }

        }

    }

}
