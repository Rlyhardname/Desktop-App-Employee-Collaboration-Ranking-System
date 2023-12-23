package com.sirma.academy.service;

import com.sirma.academy.model.Employee;
import com.sirma.academy.model.EmployeePairDTO;
import com.sirma.academy.model.EmployeeProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class Service {
    private List<EmployeeProject> employeeProjectList;
    private Map<Long, Employee> employeeMap;
    private List<Employee> employeeList;
    private List<EmployeePairDTO> dataSet;

    public Service(List<EmployeeProject> employeeProjectList,
                   Map<Long, Employee> employeeMap,
                   List<Employee> employeeList) {
        this.employeeProjectList = employeeProjectList;
        this.employeeMap = employeeMap;
        this.employeeList = employeeList;
        dataSet = new ArrayList<>();
    }

    public void createDataSet() {
        EmployeePairDTO employeePairDTO = new EmployeePairDTO(null, null, 0L);
        int startAt = 0;
        while (employeeList.size() > startAt) {
            employeePairDTO = fillDistributionDataSet(startAt, employeePairDTO);
            startAt += 1;
        }

    }

    private EmployeePairDTO fillDistributionDataSet(int start, EmployeePairDTO DTOarg) {
        EmployeePairDTO employeePairDTO = DTOarg;
        if (start > 0) {
            swapEmployeeIndex(0, start);
        }

        for (int i = 0; i < employeeList.size(); i++) {
            if(i==start){
                continue;
            }
            Employee curEmp = employeeList.get(i);
            Employee left = employeeList.get(start);
            long leftIndexChronoTime = getChronoTime(left, curEmp);
            if (leftIndexChronoTime > 0L) {
                dataSet.add(new EmployeePairDTO(left, curEmp, leftIndexChronoTime));
            }

            if (leftIndexChronoTime > employeePairDTO.days()) {
                Long newBest = leftIndexChronoTime;
                employeePairDTO = new EmployeePairDTO(left, curEmp, newBest);
            }

        }

        return employeePairDTO;
    }

    private void swapEmployeeIndex(int left, int right) {
        Employee leftEmp = employeeList.get(left);
        Employee rightEmp = employeeList.get(right);
        employeeList.set(left, rightEmp);
        employeeList.set(right, leftEmp);
    }

    public long getChronoTime(Employee emp1, Employee emp2) {
        long days = 0;
        for (var KV : emp2.getProjects().entrySet()) {
            long key = KV.getKey();
            if (!emp1.getProjects().containsKey(key)) {
                continue;
            }

            List<EmployeeProject> empProjectOfTypeKey1 = emp1.getProjects().get(key);
            List<EmployeeProject> empProjectOfTypeKey2 = emp2.getProjects().get(key);
            days += getDays(empProjectOfTypeKey1, empProjectOfTypeKey2);
        }

        return days;
    }

    public long getDays(List<EmployeeProject> empProjectOfTypeKey1, List<EmployeeProject> empProjectOfTypeKey2) {
        long days = 0;
        for (EmployeeProject project1 : empProjectOfTypeKey1) {
            for (EmployeeProject project2 : empProjectOfTypeKey2) {
                if (isPartialMatch(project1, project2)) {
                    days += calcDays(project1, project2);
                }

            }

        }

        return days;
    }

    public long calcDays(EmployeeProject project1, EmployeeProject project2) {
        LocalDate empOneStart = project1.getStartDate();
        LocalDate empOneLeave = project1.getLeaveDate();
        LocalDate empTwoStart = project2.getStartDate();
        LocalDate empTwoLeave = project2.getLeaveDate();
        String startedFirst = empOneStart.isBefore(empTwoStart) ? "empOneStart" : "empTwoStart";
        String quitFirst = empOneLeave.isBefore(empTwoLeave) ? "empOneLeave" : "empTwoLeave";
        if (startedFirst.equals("empOneStart")) {
            if (quitFirst.equals("empOneLeave")) {
                return DAYS.between(empTwoStart, empOneLeave);
            }
            if (quitFirst.equals("empTwoLeave")) {
                return DAYS.between(empTwoStart, empTwoLeave);
            }
        }

        if (startedFirst.equals("empTwoStart")) {
            if (quitFirst.equals("empOneLeave")) {
                return DAYS.between(empOneStart, empOneLeave);
            }
            if (quitFirst.equals("empTwoLeave")) {
                return DAYS.between(empOneStart, empTwoLeave);
            }
        }

        System.err.println("Mistakes were made...");
        assert 1 == 0 : "should never reach this code";
        return Long.MAX_VALUE;
    }

    boolean isPartialMatch(EmployeeProject project1, EmployeeProject project2) {
        if (project1.getStartDate().isAfter(project2.getStartDate()) &&
                project1.getStartDate().isBefore(project2.getLeaveDate())) {
            return true;
        }

        if (project1.getLeaveDate().isAfter(project2.getStartDate()) &&
                project1.getLeaveDate().isBefore(project2.getLeaveDate())) {
            return true;
        }

        if (project1.getStartDate().isBefore(project2.getStartDate()) &&
                project1.getLeaveDate().isAfter(project2.getLeaveDate())) {
            return true;
        }

        return false;

    }

    public List<EmployeeProject> getEmployeeProjectList() {
        return employeeProjectList;
    }

    public Map<Long, Employee> getEmployeeMap() {
        return employeeMap;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<EmployeePairDTO> getDataSet() {
        return dataSet;
    }
}