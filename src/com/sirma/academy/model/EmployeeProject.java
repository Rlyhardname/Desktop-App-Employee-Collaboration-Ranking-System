package com.sirma.academy.model;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeeProject {
    private final int id;
    private final Long empId;
    private final Long projectId;
    private final LocalDate startDate;
    private final LocalDate leaveDate;

    public EmployeeProject(Long empId, Long projectId, LocalDate startDate, LocalDate leaveDate) {
        this.empId = empId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.leaveDate = leaveDate;
        id = hashCode();
    }

    public int getId() {
        return id;
    }

    public Long getEmpId() {
        return empId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeProject that = (EmployeeProject) o;
        return Objects.equals(empId, that.empId) && Objects.equals(projectId, that.projectId) && Objects.equals(startDate, that.startDate) && Objects.equals(leaveDate, that.leaveDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, projectId, startDate, leaveDate);
    }

    @Override
    public String toString() {
        return "EmployeeProject{" +
                "employee=" + empId +
                ", project=" + projectId +
                ", startTime=" + startDate +
                ", leaveDate=" + leaveDate +
                '}';
    }

}
