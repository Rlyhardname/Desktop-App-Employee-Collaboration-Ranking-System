package com.sirma.academy.io;

import com.sirma.academy.model.EmployeeProject;

import java.util.List;

public interface CustomReader {
    List<EmployeeProject> read(String uri);
}
