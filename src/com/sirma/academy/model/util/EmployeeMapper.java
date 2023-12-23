package com.sirma.academy.model.util;

import com.sirma.academy.exception.EntityFactoryException;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.EmployeeProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeMapper {

    public static Map<Long, Employee> mapOfEmployees(List<EmployeeProject> employeeProjects) {
        Map<Long, Employee> employeeMap = new HashMap<>();
        for (EmployeeProject item : employeeProjects) {
            try {
                if (!employeeMap.containsKey(item.getEmpId())) {
                    Employee emp = EntityFactory.newEmployee(item.getEmpId());;
                    employeeMap.put(item.getEmpId(), emp);
                }
                Employee employee = employeeMap.get(item.getEmpId());
                employee.add(item);
            } catch (EntityFactoryException e) {
                    e.printStackTrace();
            }

        }
        return employeeMap;
    }

    public static List<Employee> listOfEmployees(Map<Long, Employee> employeeMap) {
        List<Employee> employeeList = new ArrayList<>();
        //employeeMap.forEach((K, V) -> employeeList.add(V));
        for (var item: employeeMap.entrySet()) {
            employeeList.add(item.getValue());
        }

        return employeeList;
    }


}