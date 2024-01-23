package com.sirma.academy.gui;

import com.sirma.academy.service.Service;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.util.EmployeeMapper;
import com.sirma.academy.model.EmployeePairDTO;
import com.sirma.academy.model.EmployeeProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Application extends GUI {

    private Service service;
    private static Application application;

    public static Application start() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    public Application() {
        getSaveRecordsBtn().addMouseListener(new ButtonAdapter(this));
        getLoadCSVbtn().addMouseListener(new ButtonAdapter(this));
        getTable().addMouseListener(new ButtonAdapter(this));
        getSeedDb().addMouseListener(new ButtonAdapter(this));
        getBtnSelect().addMouseListener(new ButtonAdapter(this));
        getBtnInsert().addMouseListener(new ButtonAdapter(this));
        getBtnUpdate().addMouseListener(new ButtonAdapter(this));
        getBtnDelete().addMouseListener(new ButtonAdapter(this));
        getRadioDontSave().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                getTextAreaDB().setText("This feature will only load" + System.lineSeparator()
                        + "the CSV files and display" + System.lineSeparator()
                        + "them, but not use the DB");
            }

        });
        getRadioSave().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                getTextAreaDB().setText("This feature will load" + System.lineSeparator()
                        + "the SCV files, save them " + System.lineSeparator()
                        + "to the DB and display after. " + System.lineSeparator()
                        + "It will produce some buffer wait time.");
            }

        });
        getRadioButtonDontCleanEntries().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                getTextAreaClean().setText("This feature will load" + System.lineSeparator()
                        + "the SCV files and not check" + System.lineSeparator()
                        + "for anything before it" + System.lineSeparator()
                        + "displays the data.");
            }

        });
        getRadioCleanEntries().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                getTextAreaClean().setText("This feature will load " + System.lineSeparator()
                        + "the SCV files, check if " + System.lineSeparator()
                        + "employeeId && projectId " + System.lineSeparator()
                        + "from the record exists in db. " + System.lineSeparator()
                        + " If any of them don't, " + System.lineSeparator()
                        + " will throw exception" + System.lineSeparator()
                        + "and will exclude the record " + System.lineSeparator()
                        + "from the presentation ");
            }

        });

    }

    void initService(List<EmployeeProject> employeeProjectList) {
        Map<Long, Employee> employeeMap = EmployeeMapper.mapOfEmployees(
                new ArrayList<>(employeeProjectList));
        List<Employee> employeeList = EmployeeMapper.listOfEmployees(employeeMap);
        service = new Service(employeeProjectList, employeeMap, employeeList);
        service.createDataSet();
        Collections.sort(service.getDataSet());
    }

    void populateTable(int index) {
        List<EmployeePairDTO> dataSet = service.getDataSet();
        EmployeePairDTO item = dataSet.get(dataSet.size() - index);
        Employee employee1 = item.empOne();
        Employee employee2 = item.empTwo();
        Map<Long, List<EmployeeProject>> projectsEmpOne = employee1.getProjects();
        Map<Long, List<EmployeeProject>> projectsEmpTwo = employee2.getProjects();
        Map<Long, Long> mapOfProjects = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("  Employee pair with the longest work time on cumulative projects" + System.lineSeparator()
                + "  Employee with ID: " + employee1.getId() + System.lineSeparator()
                + "  Employee with ID: " + employee2.getId() + System.lineSeparator()
                + "       Projects: " + System.lineSeparator());
        for (var item1 : projectsEmpOne.entrySet()) {
            Long key = item1.getKey();
            if (!projectsEmpTwo.containsKey(key)) {
                continue;
            }

            if (!mapOfProjects.containsKey(key)) {
                mapOfProjects.put(key, 0L);
            }

            mapOfProjects.put(key, mapOfProjects.get(key) + service.getDays(projectsEmpOne.get(key), projectsEmpTwo.get(key)));
        }

        for (var visualizeProjects : mapOfProjects.entrySet()) {
            getArea().setText(sb.append("  ProjectId " + visualizeProjects.getKey() + " | Duration " + visualizeProjects.getValue() + System.lineSeparator()).toString());
        }

        getTable().setModel(new DefaultTableModel(fillRows(), fillCols()));
    }

    private Object[] fillCols() {
        return new Object[]{
                "Row_Number:",
                "Employee_One_Id",
                "Employee_Two_Id",
                "Cumulative_working_days"};
    }

    private Object[][] fillRows() {
        List<EmployeePairDTO> dataSet = service.getDataSet();
        Object[][] data = new Object[dataSet.size()][4];
        int row = 1;
        for (int i = dataSet.size() - 1; i > 0; i -= 2) {
            data[row][0] = "#" + row;
            data[row][1] = dataSet.get(i).empOne().getId();
            data[row][2] = dataSet.get(i).empTwo().getId();
            data[row][3] = dataSet.get(i).days();
            row++;
        }

        return data;
    }

    public List<EmployeeProject> readFile() {
        int returnVal = getFc().showOpenDialog(this);
        File file = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = getFc().getSelectedFile();
            //log.append("Opening: " + file.getName() + "." + newline);
        } else {
            //log.append("Open command cancelled by user." + newline);
            return null;
        }

        String URI;
        try {
            URI = file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return read(URI);
    }

    @Override
    public List<EmployeeProject> read(String uri) {
        return getReader().read(uri);
    }

    public Service getService() {
        return service;
    }
}
