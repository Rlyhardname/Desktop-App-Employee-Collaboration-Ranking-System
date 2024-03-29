package com.sirma.academy.gui;

import com.sirma.academy.db.DataSourceFactory;
import com.sirma.academy.db.DataSourcePool;
import com.sirma.academy.db.SeedDataBase;
import com.sirma.academy.io.CustomReader;
import com.sirma.academy.io.ReaderCSV;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.sirma.academy.db.DataBaseConfiguration;
import com.sirma.academy.model.EmployeeProject;

import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GUI extends JFrame implements CustomReader {

    private JFileChooser fc;
    private DataSourceFactory dataSourceFactory;
    private MysqlDataSource dataSource;
    private CustomReader reader;

    private JButton loadCSVbtn;
    private JTable table;
    private JTextArea area;

    private JButton saveRecordsBtn;

    private JRadioButton radioDontSave;
    private JRadioButton radioSave;
    private ButtonGroup dbButtonGroup;

    private JRadioButton radioCleanEntries;
    private JRadioButton radioButtonDontCleanEntries;
    private ButtonGroup cleanupButtonGroup;
    private JFrame frame;

    private JTextArea textAreaDB;
    private JTextArea textAreaClean;
    private JTextField select;
    private JTextField insert;
    private JTextField updateFrom;
    private JTextField updateTo;
    private JTextField delete;

    private JButton btnSelect;
    private JButton btnInsert;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton seedDb;
    private JButton truncateEmployeeProjects;
    private JButton truncateEmployee;
    private JButton truncateProjects;

    public GUI() {
        String dataBaseName = "mock";
        dataSourceFactory = new DataSourceFactory(new DataBaseConfiguration(
                dataBaseName,
                "jdbc:mysql://localhost/" + dataBaseName,
                "root",
                "dCBZXTf49PcL3L97lWXP"
        ));
        dataSource = (MysqlDataSource) DataSourcePool.instanceOf(dataSourceFactory.newMysqlDataSource());
        try {
            // IF any issues make changes in SeedDataBase constructor
            new SeedDataBase(dataSource, dataSourceFactory.dataBaseConfiguration(), dataBaseName);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this.getFrame(), "DB seeding error, look inside GUI constructor");
        }

        reader = new ReaderCSV();
        init();
    }

    private void init() {
        frame = this;
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path filePath = Paths.get(root.toString(), "resources", "MOCK_DATA(3).csv");
        fc = new JFileChooser(filePath.toString());
        setTitle("A Sample JFrame Window");

        // LEFT PANE

        // Load file Buttons
        loadCSVbtn = new JButton("Load CSV");
        loadCSVbtn.setBounds(100, 100, 115, 55);
        saveRecordsBtn = new JButton("Save imports to DB");

        // Save to db radio
        dbButtonGroup = new ButtonGroup();
        radioSave = new JRadioButton("Auto save to DB when importing");
        radioDontSave = new JRadioButton("Don't save to DB when importing");
        dbButtonGroup.add(radioSave);
        dbButtonGroup.add(radioDontSave);
        JPanel saveRadioDB = new JPanel();
        saveRadioDB.add(radioSave);
        saveRadioDB.add(radioDontSave);
        radioDontSave.setSelected(true);
        textAreaDB = new JTextArea();

        // Clean up records radio
        cleanupButtonGroup = new ButtonGroup();
        radioCleanEntries = new JRadioButton("Remove bad records");
        radioButtonDontCleanEntries = new JRadioButton("Don't remove bad records");
        radioButtonDontCleanEntries.setSelected(true);
        JPanel cleanRadio = new JPanel();
        cleanRadio.add(radioCleanEntries);
        cleanRadio.add(radioButtonDontCleanEntries);
        cleanupButtonGroup.add(radioCleanEntries);
        cleanupButtonGroup.add(radioButtonDontCleanEntries);
        textAreaClean = new JTextArea();

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 2));
        leftPanel.add(loadCSVbtn);
        leftPanel.add(saveRecordsBtn);
        leftPanel.add(saveRadioDB);
        leftPanel.add(cleanRadio);
        leftPanel.add(textAreaDB);
        leftPanel.add(textAreaClean);
        add(leftPanel);

        // MID PANEL
        JPanel mid = new JPanel();
        mid.setLayout(new GridLayout(2, 1));
        area = new JTextArea(10, 20);
        table = new JTable();
        mid.add(area);
        JPanel crud = new JPanel();
        crud.setLayout(new GridLayout(5, 1));

        // CRUD label init and customization
        btnSelect = new JButton("SELECT employee by ID");
        btnInsert = new JButton("INSERT employee by ID");
        btnUpdate = new JButton("UPDATE employee FROM - TO by ID");
        btnDelete = new JButton("DELETE employee by ID");
        seedDb = new JButton("Seed DB with Mock emp and projects 1 to 99");
        truncateEmployeeProjects = new JButton("Delete all entries from records table");
        truncateEmployee = new JButton("Delete all employees");
        truncateProjects = new JButton("Delete all projects");

        // CRUD text field customization
        select = new JTextField();
        select.setPreferredSize(new Dimension(100, 20));
        updateFrom = new JTextField();
        updateFrom.setPreferredSize(new Dimension(100, 20));
        updateTo = new JTextField();
        updateTo.setPreferredSize(new Dimension(100, 20));
        delete = new JTextField();
        delete.setPreferredSize(new Dimension(100, 20));
        insert = new JTextField();
        insert.setPreferredSize(new Dimension(100, 20));

        // select panel
        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(btnSelect);
        panel1.add(select);
        crud.add(panel1);

        // insert panel
        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(btnInsert);
        panel2.add(insert);
        crud.add(panel2);

        // update panel
        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.add(btnUpdate);
        panel3.add(updateFrom);
        panel3.add(updateTo);
        crud.add(panel3);

        // delete panel
        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(btnDelete);
        panel4.add(delete);
        crud.add(panel4);

        // seed button add
        crud.add(seedDb);
        mid.add(crud);
        this.getContentPane().add(mid);

        // truncate EmployeeProjects
        crud.add(truncateEmployeeProjects);

        // truncate employees
        crud.add(truncateEmployee);

        // truncate projects
        crud.add(truncateProjects);

        // RIGHT PANEL
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane);

        //FRAME final touches
        setLayout(new GridLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public List<EmployeeProject> read(String uri) {
        return null;
    }

    public JFileChooser getFc() {
        return fc;
    }

    public void setFc(JFileChooser fc) {
        this.fc = fc;
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MysqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CustomReader getReader() {
        return reader;
    }

    public void setReader(CustomReader reader) {
        this.reader = reader;
    }

    public JButton getLoadCSVbtn() {
        return loadCSVbtn;
    }

    public void setLoadCSVbtn(JButton loadCSVbtn) {
        this.loadCSVbtn = loadCSVbtn;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JTextArea getArea() {
        return area;
    }

    public void setArea(JTextArea area) {
        this.area = area;
    }

    public JButton getSaveRecordsBtn() {
        return saveRecordsBtn;
    }

    public JRadioButton getRadioDontSave() {
        return radioDontSave;
    }

    public JRadioButton getRadioSave() {
        return radioSave;
    }

    public ButtonGroup getDbButtonGroup() {
        return dbButtonGroup;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JRadioButton getRadioCleanEntries() {
        return radioCleanEntries;
    }

    public JRadioButton getRadioButtonDontCleanEntries() {
        return radioButtonDontCleanEntries;
    }

    public ButtonGroup getCleanupButtonGroup() {
        return cleanupButtonGroup;
    }

    public JTextArea getTextAreaDB() {
        return textAreaDB;
    }

    public JTextArea getTextAreaClean() {
        return textAreaClean;
    }

    public void setSaveRecordsBtn(JButton saveRecordsBtn) {
        this.saveRecordsBtn = saveRecordsBtn;
    }

    public void setRadioDontSave(JRadioButton radioDontSave) {
        this.radioDontSave = radioDontSave;
    }

    public void setRadioSave(JRadioButton radioSave) {
        this.radioSave = radioSave;
    }

    public void setDbButtonGroup(ButtonGroup dbButtonGroup) {
        this.dbButtonGroup = dbButtonGroup;
    }

    public void setRadioCleanEntries(JRadioButton radioCleanEntries) {
        this.radioCleanEntries = radioCleanEntries;
    }

    public void setRadioButtonDontCleanEntries(JRadioButton radioButtonDontCleanEntries) {
        this.radioButtonDontCleanEntries = radioButtonDontCleanEntries;
    }

    public void setCleanupButtonGroup(ButtonGroup cleanupButtonGroup) {
        this.cleanupButtonGroup = cleanupButtonGroup;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setTextAreaDB(JTextArea textAreaDB) {
        this.textAreaDB = textAreaDB;
    }

    public void setTextAreaClean(JTextArea textAreaClean) {
        this.textAreaClean = textAreaClean;
    }

    public JTextField getSelect() {
        return select;
    }

    public void setSelect(JTextField select) {
        this.select = select;
    }

    public JTextField getInsert() {
        return insert;
    }

    public void setInsert(JTextField insert) {
        this.insert = insert;
    }

    public JTextField getUpdateFrom() {
        return updateFrom;
    }

    public void setUpdateFrom(JTextField updateFrom) {
        this.updateFrom = updateFrom;
    }

    public JTextField getDelete() {
        return delete;
    }

    public void setDelete(JTextField delete) {
        this.delete = delete;
    }

    public JButton getBtnSelect() {
        return btnSelect;
    }

    public void setBtnSelect(JButton btnSelect) {
        this.btnSelect = btnSelect;
    }

    public JButton getBtnInsert() {
        return btnInsert;
    }

    public void setBtnInsert(JButton btnInsert) {
        this.btnInsert = btnInsert;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public void setBtnUpdate(JButton btnUpdate) {
        this.btnUpdate = btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JButton getSeedDb() {
        return seedDb;
    }

    public void setSeedDb(JButton seedDb) {
        this.seedDb = seedDb;
    }

    public JTextField getUpdateTo() {
        return updateTo;
    }

    public void setUpdateTo(JTextField updateTo) {
        this.updateTo = updateTo;
    }

    public JButton getTruncateEmployeeProjects() {
        return truncateEmployeeProjects;
    }

    public void setTruncateEmployeeProjects(JButton truncateEmployeeProjects) {
        this.truncateEmployeeProjects = truncateEmployeeProjects;
    }

    public JButton getTruncateEmployee() {
        return truncateEmployee;
    }

    public void setTruncateEmployee(JButton truncateEmployee) {
        this.truncateEmployee = truncateEmployee;
    }

    public JButton getTruncateProjects() {
        return truncateProjects;
    }

    public void setTruncateProjects(JButton truncateProjects) {
        this.truncateProjects = truncateProjects;
    }
}


