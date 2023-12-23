package com.sirma.academy.db;

import com.sirma.academy.dao.DAO;
import com.sirma.academy.dao.ProjectDAO;
import com.sirma.academy.dao.UserDAO;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.util.EntityFactory;
import com.sirma.academy.model.Project;
import com.sirma.academy.exception.EntityFactoryException;

import java.util.ArrayList;
import java.util.List;

public class SeedDBwithData {
    public SeedDBwithData(){
        // Seed employees
        DAO userDAO = new UserDAO(DataSourcePool.instanceOf());
        List<Employee> aEmployees = new ArrayList<>();
        for (long i = -2; i < 100; i++) {
            try{
                aEmployees.add(EntityFactory.newEmployee(i));
            }catch (EntityFactoryException e){
                System.err.println(getClass().getName()+ "  line 20: "+e.getMessage());
                // Log or send to client e.getMessage();
            }

        }
        userDAO.saveAll(aEmployees);

        // Seed projects
        DAO projectDAO = new ProjectDAO(DataSourcePool.instanceOf());
        List<Project> projects = new ArrayList<>();
        for (long i = -2; i < 100; i++) {
            try{
                projects.add(EntityFactory.newProject(i));
            }catch (EntityFactoryException e){
                System.err.println(getClass().getName()+ " line 34: "+e.getMessage());
                // Log or send to client e.getMessage();
            }

        }
        projectDAO.saveAll(projects);


    }
}
