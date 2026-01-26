import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.swing.*;


public class AttendanceTracker {
    
    // Main frame components
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField signatureField;
    private JButton submitButton;
    private JButton clearButton;
    
    /**
     * Constructor - initializes the application
     */
    public AttendanceTracker() {
        initializeGUI();
    }
    
    /**
     * Initialize and setup the GUI components
     */
    private void initializeGUI() {
        // Create the main frame
        frame = new JFrame("Attendance Tracker");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Create and add the title panel
        JPanel titlePanel = createTitlePanel();
        frame.add(titlePanel, BorderLayout.NORTH);
        
        // Create and add the form panel
        JPanel formPanel = createFormPanel();
        frame.add(formPanel, BorderLayout.CENTER);
        
        // Create and add the button panel
        JPanel buttonPanel = createButtonPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Center the frame on screen
        frame.setLocationRelativeTo(null);
        
        // Make the frame visible
        frame.setVisible(true);
    }
    
    /**
     * Create the title panel with application header
     * @return JPanel containing the title
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 105, 180)); // Hot pink color
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel titleLabel = new JLabel("ATTENDANCE TRACKING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * Create the form panel with all input fields
     * @return JPanel containing the form fields
     */
    private JPanel createFormPanel() {
        // Main panel with GridBagLayout for better control
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 240, 245)); // Lavender blush (light pink)
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Font for labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);
        Color labelColor = new Color(219, 39, 119); // Pink label color
        
        // Attendance Name field
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(nameLabel, gbc);
        
        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(nameField, gbc);
        
        // Course/Year field
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(labelFont);
        courseLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(courseLabel, gbc);
        
        courseField = new JTextField(20);
        courseField.setFont(fieldFont);
        courseField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(courseField, gbc);
        
        // Time In field (non-editable)
        JLabel timeLabel = new JLabel("Time In:");
        timeLabel.setFont(labelFont);
        timeLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(timeLabel, gbc);
        
        timeInField = new JTextField(20);
        timeInField.setFont(fieldFont);
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(255, 228, 225)); // Misty rose
        timeInField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(timeInField, gbc);
        
        // E-Signature field (non-editable)
        JLabel signatureLabel = new JLabel("E-Signature:");
        signatureLabel.setFont(labelFont);
        signatureLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panel.add(signatureLabel, gbc);
        
        signatureField = new JTextField(20);
        signatureField.setFont(new Font("Courier", Font.PLAIN, 11));
        signatureField.setEditable(false);
        signatureField.setBackground(new Color(255, 228, 225)); // Misty rose
        signatureField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(signatureField, gbc);
        
        return panel;
    }
    
    /**
     * Create the button panel with action buttons
     * @return JPanel containing the buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(255, 240, 245)); // Lavender blush (light pink)
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Submit button
        submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setBackground(new Color(255, 105, 180)); // Hot pink
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(160, 35));
        submitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        // Add action listener for submit button
        submitButton.addActionListener(e -> submitAttendance());
        panel.add(submitButton);
        
        // Clear button
        clearButton = new JButton("Clear Form");
        clearButton.setFont(new Font("Arial", Font.BOLD, 13));
        clearButton.setBackground(new Color(219, 39, 119)); // Deep pink
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(160, 35));
        clearButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        // Add action listener for clear button
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Submit the attendance form
     * Validates input and generates timestamp and e-signature
     */
    private void submitAttendance() {
        // Validate input fields
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        
        if (name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please fill in all required fields (Name and Course/Year)!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get current date and time with proper formatting
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        signatureField.setText(eSignature);
        
        // Show success message
        JOptionPane.showMessageDialog(frame,
            "Attendance submitted successfully!\n\n" +
            "Name: " + name + "\n" +
            "Course: " + course + "\n" +
            "Time: " + timeIn,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Clear all form fields
     */
    private void clearForm() {
        nameField.setText("");
        courseField.setText("");
        timeInField.setText("");
        signatureField.setText("");
        nameField.requestFocus(); // Set focus back to name field
    }
    
    /**
     * Main method - entry point of the application
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new AttendanceTracker();
        });
    }
}