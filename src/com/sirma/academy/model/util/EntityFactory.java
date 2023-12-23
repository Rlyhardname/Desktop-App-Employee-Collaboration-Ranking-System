package com.sirma.academy.model.util;

import com.sirma.academy.exception.*;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.EmployeeProject;
import com.sirma.academy.model.Project;

import java.time.LocalDate;

public class EntityFactory {

    public static Employee newEmployee(Long longId) {
        if (isIdPositive(longId)) {
            // log
            return new Employee(longId);
        }

        throw new NegativeIdException("Employee id cannot be negative!");
    }

    public static Project newProject(Long projectId) {
        if (isIdPositive(projectId)) {
            // log
            return new Project(projectId);
        }

        throw new NegativeIdException("ProjectId cannot be negative!");
    }

    public static EmployeeProject newEmployeeProject(
            Long empId,
            Long projectId,
            LocalDate startDate,
            LocalDate quitDate) {
        if (empId == null) {
            // log
            throw new NullUserException("User object cannot be null!");
        }

        if (projectId == null) {
            // log
            throw new NullProjectException("Project object cannot be null!");
        }

        if (startDate == null) {
            // log
            throw new NullDateException("StartDate object cannot be null!");
        }

        if (quitDate != null) {
            // log
            if (startDate.compareTo(quitDate) >= 0) {
                throw new DateComparisonException("startDate cannot be equal or greater than quitDate!");
            }
        }
        return new EmployeeProject(empId, projectId, startDate, quitDate);
    }

    private static boolean isIdPositive(java.lang.Long number) {
        if (number <= 0) {
            return false;
        }
        return true;
    }

}



