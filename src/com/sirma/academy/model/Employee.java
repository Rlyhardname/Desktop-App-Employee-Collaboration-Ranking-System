package com.sirma.academy.model;

import java.util.*;

public class Employee {
    private final Long id;
    private final Map<Long, List<EmployeeProject>> projects;

    public Employee(Long id) {
        this.id = id;
        projects = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public Map<Long, List<EmployeeProject>> getProjects() {
        return projects;
    }

    public void add(EmployeeProject project) {
        try {
            if (!projects.containsKey(project.getProjectId())) {
                projects.put(project.getProjectId(), new ArrayList<>());
            }

        } catch (RuntimeException e) {
            System.out.println("mistake here?");
        }

        List<EmployeeProject> t = projects.get(project.getProjectId());
        t.add(project);
        projects.put(project.getProjectId(),t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee aEmployee = (Employee) o;
        return Objects.equals(id, aEmployee.id) && Objects.equals(projects, aEmployee.projects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projects);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", projects=" + projects +
                '}';
    }
}
