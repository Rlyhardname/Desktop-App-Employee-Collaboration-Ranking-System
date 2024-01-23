package com.sirma.academy.gui;

import com.sirma.academy.dao.*;
import com.sirma.academy.db.DataSourcePool;
import com.sirma.academy.db.SeedDBwithData;
import com.sirma.academy.exception.EntityFactoryException;
import com.sirma.academy.io.ReaderCSV;
import com.sirma.academy.model.Employee;
import com.sirma.academy.model.util.EmployeeMapper;
import com.sirma.academy.model.EmployeeProject;
import com.sirma.academy.model.util.EntityFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ButtonAdapter extends MouseAdapter {
    Application gui;

    public ButtonAdapter(Application gui) {
        this.gui = gui;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        // Save records button
        if (source == gui.getSaveRecordsBtn()) {
            if (gui.getService() != null) {
                List<EmployeeProject> employeeProjectList = gui.getService().getEmployeeProjectList();
                if (employeeProjectList != null) {
                    DAO empProjectDAO = new EmployeeProjectDAO(DataSourcePool.instanceOf());
                    empProjectDAO.saveAll(employeeProjectList);
                } else {
                    JOptionPane.showMessageDialog(gui.getFrame(), "No records have been loaded yet!");
                }

            } else {
                JOptionPane.showMessageDialog(gui.getFrame(), "No records have been loaded yet!");
            }

            // Load CVS button
        } else if (source == gui.getLoadCSVbtn()) {
            List<EmployeeProject> employeeProjectList = gui.readFile();
            if (employeeProjectList != null) {
                if (gui.getRadioSave().isSelected()) {
                    DAO empProjectDAO = new EmployeeProjectDAO(DataSourcePool.instanceOf());
                    empProjectDAO.saveAll(employeeProjectList);
                }

                if (gui.getRadioCleanEntries().isSelected()) {
                    ((ReaderCSV) gui.getReader()).cleanEntriesWithUnexistingEmployeesOrProjects();
                }

                gui.initService(employeeProjectList);
                gui.populateTable(1);
            }

            // Empty table rows/cols event
        } else if (source == gui.getTable()) {
            int row;
            int col;
            String value = null;
            try {
                row = gui.getTable().rowAtPoint(e.getPoint());
                col = gui.getTable().columnAtPoint(e.getPoint());
                value = gui.getTable().getValueAt(row, col).toString();
                if (value.startsWith("#")) {
                    gui.getArea().append(value + System.lineSeparator());
                    int index = Integer.parseInt(value.substring(1));
                    index += index - 1;
                    gui.populateTable(index);
                }

            } catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Empty row or column");
            }

            // Seed DB with emp and projects
        } else if (source == gui.getSeedDb()) {
            new SeedDBwithData();

            // SELECT CRUD
        } else if (source == gui.getBtnSelect()) {
            List<EmployeeProject> list = new ArrayList<>();
            Long id = null;
            try {
                id = Long.parseLong(gui.getSelect().getText());
                EntityDAO DAO = new EmployeeProjectDAO(gui.getDataSource());
                list = DAO.mapOfObjects(id);

            } catch (RuntimeException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(gui.getFrame(), "Field is empty or not a number!");
            }

            if (!list.isEmpty() && Objects.nonNull(list)) {
                Map<Long, Employee> map = EmployeeMapper.mapOfEmployees(list);
                StringBuilder sb = new StringBuilder();
                for (var entrySet : map.get(id).getProjects().entrySet()) {
                    List<EmployeeProject> employeeProjects = entrySet.getValue();
                    for (EmployeeProject item : employeeProjects) {
                        sb.append("emp_Id: " + item.getEmpId()
                                + " project_Id: " + item.getProjectId() + System.lineSeparator()
                                + "start_Date: " + item.getStartDate()
                                + " leave_Date: " + item.getLeaveDate() + System.lineSeparator());
                    }

                }

                gui.getArea().setText(sb.toString());
            } else {
                DAO userDAO = new UserDAO(gui.getDataSource());
                if (userDAO.existsById(id)) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Employee has no projects!");
                } else {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Employee doesn't exist!");
                }

            }

            // INSERT CRUD
        } else if (source == gui.getBtnInsert()) {
            DAO userDAO = new UserDAO(gui.getDataSource());
            try {
                Long id = Long.parseLong(gui.getInsert().getText());
                int isSuccess = userDAO.save(EntityFactory.newEmployee(id));
                if (isSuccess == -1) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Id already exists in DB!");
                }

            } catch (EntityFactoryException ex) {

                JOptionPane.showMessageDialog(gui.getFrame(), "Creation ERROR");
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Id not valid!");
            }

            // UPDATE CRUD
        } else if (source == gui.getBtnUpdate()) {
            DAO userDAO = new UserDAO(gui.getDataSource());
            try {
                Long id = Long.parseLong(gui.getUpdateFrom().getText());
                Optional<Employee> optEmp = userDAO.findById(id);
                if (!optEmp.isPresent()) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Id doesn't exists in DB!");
                } else {
                    try {
                        Long newEmpId = Long.parseLong(gui.getUpdateTo().getText());
                        Employee newEmployeeData = EntityFactory.newEmployee(newEmpId);
                        System.out.println(optEmp.get().getId() + " " + newEmployeeData);
                        userDAO.update(optEmp.get().getId(), newEmployeeData);

                        JOptionPane.showMessageDialog(gui.getFrame(), "Id: " + optEmp.get().getId()
                                + " updated to: " + newEmployeeData.getId());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(gui.getFrame(), "Not valid ID!");
                    } catch (EntityFactoryException ex1) {
                        JOptionPane.showMessageDialog(gui.getFrame(), "Internal error");
                    }

                }
            } catch (EntityFactoryException ex) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Creation ERROR");
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Id not valid!");
            }

            // DELETE CRUD
        } else if (source == gui.getBtnDelete()) {
            DAO userDAO = new UserDAO(gui.getDataSource());
            try {
                Long id = Long.parseLong(gui.getDelete().getText());
                int isSuccess = userDAO.deleteById(id);
                if (isSuccess == 1) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Successfully deleted!");
                } else {
                    JOptionPane.showMessageDialog(gui.getFrame(), "User with such an ID doesn't exist!");
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Id not valid!");
            }

        }

    }
}
