import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Student Record System - Java Swing Implementation
 * Programmer: [HAMBRE, YHARA NICOLE Q.] - [25-2377-890]
 */
public class StudentRecordSystem extends JFrame {
    
    // Data structure to store student records
    private ArrayList<StudentRecord> studentRecords;
    
    // GUI Components
    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JLabel recordCountLabel;
    private JTextField searchField;
    
    // Input fields for adding new records
    private JTextField studentIdField, firstNameField, lastNameField;
    private JTextField labWork1Field, labWork2Field, labWork3Field;
    private JTextField prelimExamField, attendanceGradeField;
    
    // Color scheme (pink theme)
    private final Color PINK_PRIMARY = new Color(255, 107, 157);
    private final Color PINK_SECONDARY = new Color(196, 69, 105);
    private final Color PINK_LIGHT = new Color(255, 168, 197);
    private final Color PINK_BG = new Color(255, 240, 245);
    private final Color PINK_BORDER = new Color(255, 179, 217);
    
    /**
     * Inner class to represent a student record
     */
    class StudentRecord {
        String studentId;
        String firstName;
        String lastName;
        String labWork1;
        String labWork2;
        String labWork3;
        String prelimExam;
        String attendanceGrade;
        
        public StudentRecord(String studentId, String firstName, String lastName,
                           String labWork1, String labWork2, String labWork3,
                           String prelimExam, String attendanceGrade) {
            this.studentId = studentId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.labWork1 = labWork1;
            this.labWork2 = labWork2;
            this.labWork3 = labWork3;
            this.prelimExam = prelimExam;
            this.attendanceGrade = attendanceGrade;
        }
        
        public Object[] toArray() {
            return new Object[]{studentId, firstName, lastName, labWork1, 
                              labWork2, labWork3, prelimExam, attendanceGrade};
        }
    }
    
    /**
     * Constructor - Initialize the application
     */
    public StudentRecordSystem() {
        studentRecords = new ArrayList<>();
        initializeUI();
    }
    
    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        setTitle("Student Record System - HAMBRE, YHARA NICOLE Q. - 25-2377-890");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(PINK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center panel with upload, input, and table sections
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(PINK_BG);
        
        centerPanel.add(createUploadSection());
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(createSearchSection());
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(createInputSection());
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(createTableSection());
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PINK_PRIMARY);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("Student Record System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel identifierLabel = new JLabel("Programmer: HAMBRE, YHARA NICOLE Q. - 25-2377-890");
        identifierLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        identifierLabel.setForeground(Color.WHITE);
        identifierLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        identifierLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        headerPanel.add(titleLabel);
        headerPanel.add(identifierLabel);
        
        return headerPanel;
    }
    
    /**
     * Create CSV upload section
     */
    private JPanel createUploadSection() {
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_BORDER, 3, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel uploadLabel = new JLabel("📂 Load CSV File:");
        uploadLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        uploadLabel.setForeground(PINK_SECONDARY);
        
        JButton loadButton = createStyledButton("Load File", PINK_LIGHT);
        loadButton.addActionListener(e -> loadCSVFile());
        
        uploadPanel.add(uploadLabel);
        uploadPanel.add(loadButton);
        
        return uploadPanel;
    }
    
    /**
     * Create search section
     */
    private JPanel createSearchSection() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_BORDER, 3, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel searchLabel = new JLabel("🔍 Search by Name:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(PINK_SECONDARY);
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 192, 217), 2, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Add real-time search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchRecords();
            }
        });
        
        JButton clearButton = createStyledButton("Clear", PINK_LIGHT);
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchRecords();
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearButton);
        
        return searchPanel;
    }
    
    /**
     * Create input form section
     */
    private JPanel createInputSection() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_BORDER, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel sectionLabel = new JLabel("✏️ Add New Record");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(PINK_SECONDARY);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        formPanel.setBackground(Color.WHITE);
        
        // Initialize input fields
        studentIdField = createInputField();
        firstNameField = createInputField();
        lastNameField = createInputField();
        labWork1Field = createInputField();
        labWork2Field = createInputField();
        labWork3Field = createInputField();
        prelimExamField = createInputField();
        attendanceGradeField = createInputField();
        
        // Add labeled fields to form
        formPanel.add(createLabeledField("Student ID:", studentIdField));
        formPanel.add(createLabeledField("First Name:", firstNameField));
        formPanel.add(createLabeledField("Last Name:", lastNameField));
        formPanel.add(createLabeledField("Lab Work 1:", labWork1Field));
        formPanel.add(createLabeledField("Lab Work 2:", labWork2Field));
        formPanel.add(createLabeledField("Lab Work 3:", labWork3Field));
        formPanel.add(createLabeledField("Prelim Exam:", prelimExamField));
        formPanel.add(createLabeledField("Attendance:", attendanceGradeField));
        
        JButton addButton = createStyledButton("Add Record", PINK_PRIMARY);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.addActionListener(e -> addRecord());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addButton);
        formPanel.add(buttonPanel);
        
        inputPanel.add(sectionLabel, BorderLayout.NORTH);
        inputPanel.add(formPanel, BorderLayout.CENTER);
        
        return inputPanel;
    }
    
    /**
     * Create table section
     */
    private JPanel createTableSection() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_BORDER, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel sectionLabel = new JLabel("Student Records");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(PINK_SECONDARY);
        
        // Create table
        String[] columnNames = {"Student ID", "First Name", "Last Name", "Lab 1", 
                               "Lab 2", "Lab 3", "Prelim", "Attendance", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only Actions column is "editable" for buttons
            }
        };
        
        recordsTable = new JTable(tableModel);
        recordsTable.setRowHeight(40);
        recordsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        recordsTable.setForeground(Color.BLACK);
        recordsTable.setSelectionBackground(new Color(255, 245, 248));
        recordsTable.setSelectionForeground(Color.BLACK);
        recordsTable.setGridColor(new Color(255, 228, 240));
        
        // Style table header
        JTableHeader header = recordsTable.getTableHeader();
        header.setBackground(PINK_PRIMARY);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // Add action buttons to table
        recordsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        recordsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane tableScrollPane = new JScrollPane(recordsTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        
        recordCountLabel = new JLabel("Total Records: 0");
        recordCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        recordCountLabel.setForeground(PINK_SECONDARY);
        recordCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        tablePanel.add(sectionLabel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tablePanel.add(recordCountLabel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    /**
     * Create footer panel
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(PINK_SECONDARY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel footerLabel = new JLabel("© 2024 Student Record System | HAMBRE, YHARA NICOLE Q. - 25-2377-890");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    /**
     * Create a styled input field
     */
    private JTextField createInputField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 192, 217), 2, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    /**
     * Create a labeled field panel
     */
    private JPanel createLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(PINK_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    /**
     * Create a styled button
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Load CSV file
     */
    private void loadCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                parseCSV(selectedFile);
                renderTable();
                JOptionPane.showMessageDialog(this, 
                    "Successfully loaded " + studentRecords.size() + " records from " + selectedFile.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error reading file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    
    /**
     * Parse CSV file
     */
    private void parseCSV(File file) throws IOException {
        studentRecords.clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] values = line.split(",");
                
                if (values.length >= 8) {
                    StudentRecord record = new StudentRecord(
                        values[0].trim(),
                        values[1].trim(),
                        values[2].trim(),
                        values[3].trim(),
                        values[4].trim(),
                        values[5].trim(),
                        values[6].trim(),
                        values[7].trim()
                    );
                    studentRecords.add(record);
                }
            }
        }
    }
    
    /**
     * Add a new record
     */
    private void addRecord() {
        String studentId = studentIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        
        // Validate required fields
        if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in Student ID, First Name, and Last Name!",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        StudentRecord newRecord = new StudentRecord(
            studentId,
            firstName,
            lastName,
            labWork1Field.getText().trim(),
            labWork2Field.getText().trim(),
            labWork3Field.getText().trim(),
            prelimExamField.getText().trim(),
            attendanceGradeField.getText().trim()
        );
        
        studentRecords.add(newRecord);
        renderTable();
        clearInputFields();
        
        JOptionPane.showMessageDialog(this,
            "Record added successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Edit a record
     */
    private void editRecord(int index) {
        StudentRecord record = studentRecords.get(index);
        
        JPanel editPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        
        JTextField editStudentId = new JTextField(record.studentId);
        JTextField editFirstName = new JTextField(record.firstName);
        JTextField editLastName = new JTextField(record.lastName);
        JTextField editLabWork1 = new JTextField(record.labWork1);
        JTextField editLabWork2 = new JTextField(record.labWork2);
        JTextField editLabWork3 = new JTextField(record.labWork3);
        JTextField editPrelimExam = new JTextField(record.prelimExam);
        JTextField editAttendanceGrade = new JTextField(record.attendanceGrade);
        
        editPanel.add(new JLabel("Student ID:"));
        editPanel.add(editStudentId);
        editPanel.add(new JLabel("First Name:"));
        editPanel.add(editFirstName);
        editPanel.add(new JLabel("Last Name:"));
        editPanel.add(editLastName);
        editPanel.add(new JLabel("Lab Work 1:"));
        editPanel.add(editLabWork1);
        editPanel.add(new JLabel("Lab Work 2:"));
        editPanel.add(editLabWork2);
        editPanel.add(new JLabel("Lab Work 3:"));
        editPanel.add(editLabWork3);
        editPanel.add(new JLabel("Prelim Exam:"));
        editPanel.add(editPrelimExam);
        editPanel.add(new JLabel("Attendance:"));
        editPanel.add(editAttendanceGrade);
        
        int result = JOptionPane.showConfirmDialog(this, editPanel,
            "Edit Student Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (editStudentId.getText().trim().isEmpty() || 
                editFirstName.getText().trim().isEmpty() || 
                editLastName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Student ID, First Name, and Last Name are required!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            StudentRecord updatedRecord = new StudentRecord(
                editStudentId.getText().trim(),
                editFirstName.getText().trim(),
                editLastName.getText().trim(),
                editLabWork1.getText().trim(),
                editLabWork2.getText().trim(),
                editLabWork3.getText().trim(),
                editPrelimExam.getText().trim(),
                editAttendanceGrade.getText().trim()
            );
            
            studentRecords.set(index, updatedRecord);
            renderTable();
            
            JOptionPane.showMessageDialog(this,
                "Record updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Delete a record
     */
    private void deleteRecord(int index) {
        StudentRecord record = studentRecords.get(index);
        
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this record?\n\n" +
            "Student ID: " + record.studentId + "\n" +
            "Name: " + record.firstName + " " + record.lastName,
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            studentRecords.remove(index);
            renderTable();
            
            JOptionPane.showMessageDialog(this,
                "Record deleted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Search and filter records by name
     */
    private void searchRecords() {
        String searchText = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        
        int matchCount = 0;
        for (StudentRecord record : studentRecords) {
            // Search in both first name and last name
            String fullName = (record.firstName + " " + record.lastName).toLowerCase();
            
            if (searchText.isEmpty() || 
                record.firstName.toLowerCase().contains(searchText) ||
                record.lastName.toLowerCase().contains(searchText) ||
                fullName.contains(searchText)) {
                
                tableModel.addRow(record.toArray());
                matchCount++;
            }
        }
        
        // Update record count with search info
        if (searchText.isEmpty()) {
            recordCountLabel.setText("Total Records: " + studentRecords.size());
        } else {
            recordCountLabel.setText("Total Records: " + studentRecords.size() + 
                                   " | Showing: " + matchCount + " match(es)");
        }
    }
    
    /**
     * Render the table with current records
     */
    private void renderTable() {
        // If there's an active search, use searchRecords instead
        if (searchField != null && !searchField.getText().trim().isEmpty()) {
            searchRecords();
            return;
        }
        
        tableModel.setRowCount(0);
        
        for (StudentRecord record : studentRecords) {
            tableModel.addRow(record.toArray());
        }
        
        recordCountLabel.setText("Total Records: " + studentRecords.size());
    }
    
    /**
     * Clear all input fields
     */
    private void clearInputFields() {
        studentIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        labWork1Field.setText("");
        labWork2Field.setText("");
        labWork3Field.setText("");
        prelimExamField.setText("");
        attendanceGradeField.setText("");
        studentIdField.requestFocus();
    }
    
    /**
     * Button renderer for table cells
     */
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
            
            editButton = new JButton("Edit");
            editButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            editButton.setBackground(new Color(76, 175, 80));
            editButton.setForeground(Color.BLACK);
            editButton.setFocusPainted(false);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            
            deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            deleteButton.setBackground(new Color(255, 23, 68));
            deleteButton.setForeground(Color.BLACK);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            
            add(editButton);
            add(deleteButton);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }
    
    /**
     * Button editor for table cells
     */
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            editButton = new JButton("Edit");
            editButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            editButton.setBackground(new Color(76, 175, 80));
            editButton.setForeground(Color.BLACK);
            editButton.setFocusPainted(false);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                editRecord(currentRow);
            });
            
            deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
            deleteButton.setBackground(new Color(255, 23, 68));
            deleteButton.setForeground(Color.BLACK);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteRecord(currentRow);
            });
            
            panel.add(editButton);
            panel.add(deleteButton);
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }
        
        public Object getCellEditorValue() {
            return "";
        }
    }

    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and display the application
        SwingUtilities.invokeLater(() -> {
            StudentRecordSystem app = new StudentRecordSystem();
            app.setVisible(true);
        });
    }
}