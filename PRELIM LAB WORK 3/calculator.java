import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class GradeCalculator extends JFrame {
    private JTextField lab1Field, lab2Field, lab3Field, attendanceField;
    private JLabel labAvgLabel, attScoreLabel, classScoreLabel;
    private JLabel exam75Label, exam100Label;
    private JLabel standingBadge, descriptionLabel;
    private JLabel classStandingCircle, classScoreDisplay;
    
    private static final Color PINK_PRIMARY = new Color(217, 28, 122);
    private static final Color PINK_LIGHT = new Color(255, 133, 179);
    private static final Color PINK_LIGHTER = new Color(255, 224, 236);
    private static final Color PINK_LIGHTEST = new Color(255, 245, 249);
    
    public GradeCalculator() {
        setTitle("Class Standing Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 195, 213), 
                                                      0, getHeight(), new Color(255, 94, 157));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        
        // Left Section
        JPanel leftSection = createLeftSection();
        contentPanel.add(leftSection);
        
        // Right Section (Formulas)
        JPanel rightSection = createFormulasSection();
        contentPanel.add(rightSection);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Reset Button
        JButton resetBtn = createStyledButton("RESET");
        resetBtn.addActionListener(e -> reset());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(resetBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        calculate();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Class Standing Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(PINK_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        standingBadge = new JLabel("High Class Standing");
        standingBadge.setFont(new Font("Segoe UI", Font.BOLD, 16));
        standingBadge.setForeground(Color.WHITE);
        standingBadge.setBackground(PINK_LIGHT);
        standingBadge.setOpaque(true);
        standingBadge.setBorder(new EmptyBorder(8, 24, 8, 24));
        standingBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descriptionLabel = new JLabel("<html><center>Your class standing is <b>High</b>! You have a strong performance.</center></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(102, 102, 102));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(standingBadge);
        panel.add(Box.createVerticalStrut(15));
        panel.add(descriptionLabel);
        
        return panel;
    }
    
    private JPanel createLeftSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Score Circle
        JPanel circlePanel = createScoreCircle();
        panel.add(circlePanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Input Section
        JPanel inputsPanel = createInputsSection();
        panel.add(inputsPanel);
        
        return panel;
    }
    
    private JPanel createScoreCircle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        classStandingCircle = new JLabel("100") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int diameter = 180;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                
                // Background circle
                g2d.setColor(PINK_LIGHTER);
                g2d.setStroke(new BasicStroke(10));
                g2d.drawOval(x, y, diameter, diameter);
                
                // Progress circle
                g2d.setColor(PINK_LIGHT);
                int angle = (int)(360 * (Double.parseDouble(getText()) / 100.0));
                g2d.drawArc(x, y, diameter, diameter, 90, -angle);
                
                super.paintComponent(g);
            }
        };
        classStandingCircle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        classStandingCircle.setForeground(PINK_PRIMARY);
        classStandingCircle.setPreferredSize(new Dimension(200, 200));
        classStandingCircle.setHorizontalAlignment(SwingConstants.CENTER);
        classStandingCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        classScoreDisplay = new JLabel("Class Standing: 100.00");
        classScoreDisplay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        classScoreDisplay.setForeground(PINK_PRIMARY);
        classScoreDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(classStandingCircle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(classScoreDisplay);
        
        return panel;
    }
    
    private JPanel createInputsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Lab Work Inputs
        JPanel labPanel = createInputGroup();
        labPanel.add(createInputRow("Lab Work 1:", lab1Field = createNumberField(100)));
        labPanel.add(createInputRow("Lab Work 2:", lab2Field = createNumberField(100)));
        labPanel.add(createInputRow("Lab Work 3:", lab3Field = createNumberField(100)));
        panel.add(labPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Attendance Input
        JPanel attPanel = createInputGroup();
        attPanel.add(createInputRow("Number of Absences (max 4):", attendanceField = createNumberField(4)));
        panel.add(attPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Stats Grid
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 10, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(createStatCard("Lab Average", labAvgLabel = new JLabel("100.00")));
        statsGrid.add(createStatCard("Attendance", attScoreLabel = new JLabel("100")));
        statsGrid.add(createStatCard("Class Standing", classScoreLabel = new JLabel("100.00")));
        panel.add(statsGrid);
        panel.add(Box.createVerticalStrut(15));
        
        // Exam Scores
        panel.add(createExamCard("Required Prelim Exam for Passing (75)", exam75Label = new JLabel("75.00")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createExamCard("Required Prelim Exam for Excellent (100)", exam100Label = new JLabel("100.00")));
        
        return panel;
    }
    
    private JPanel createInputGroup() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PINK_LIGHTEST);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PINK_LIGHTER, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }
    
    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(PINK_PRIMARY);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(field, BorderLayout.EAST);
        
        return panel;
    }
    
    private JTextField createNumberField(int max) {
        JTextField field = new JTextField("0", 5);
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setForeground(PINK_PRIMARY);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PINK_LIGHT, 2, true),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validateAndCalculate(); }
            public void removeUpdate(DocumentEvent e) { validateAndCalculate(); }
            public void insertUpdate(DocumentEvent e) { validateAndCalculate(); }
            
            private void validateAndCalculate() {
                try {
                    double val = Double.parseDouble(field.getText());
                    if (val > max) field.setText(String.valueOf(max));
                    if (val < 0) field.setText("0");
                } catch (NumberFormatException ex) {
                    if (!field.getText().isEmpty()) field.setText("0");
                }
                calculate();
            }
        });
        
        return field;
    }
    
    private JPanel createStatCard(String label, JLabel valueLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 179, 209), 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelComp.setForeground(new Color(136, 136, 136));
        labelComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(PINK_PRIMARY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(labelComp);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);
        
        return panel;
    }
    
    private JPanel createExamCard(String label, JLabel valueLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PINK_LIGHTEST);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 179, 209), 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelComp.setForeground(new Color(136, 136, 136));
        labelComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(PINK_PRIMARY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(labelComp);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);
        
        return panel;
    }
    
    private JPanel createFormulasSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PINK_LIGHTEST);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 179, 209), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("Calculation Formulas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PINK_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(createFormulaCard("1", "Lab Work Average", "Lab Work Average = (Lab 1 + Lab 2 + Lab 3) / 3"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormulaCard("2", "Class Standing", "Class Standing = (Attendance × 0.40) + (Lab Work Average × 0.60)"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormulaCard("3", "Prelim Grade", "Prelim Grade = (Prelim Exam × 0.70) + (Class Standing × 0.30)"));
        
        return panel;
    }
    
    private JPanel createFormulaCard(String number, String name, String formula) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, PINK_LIGHT),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel nameLabel = new JLabel(number + ". " + name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(PINK_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel formulaLabel = new JLabel(formula);
        formulaLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        formulaLabel.setForeground(new Color(85, 85, 85));
        formulaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(formulaLabel);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PINK_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void calculate() {
        try {
            double lab1 = Double.parseDouble(lab1Field.getText());
            double lab2 = Double.parseDouble(lab2Field.getText());
            double lab3 = Double.parseDouble(lab3Field.getText());
            double absences = Double.parseDouble(attendanceField.getText());
            
            double attendance = Math.max(0, 100 - (absences * 25));
            double labAverage = (lab1 + lab2 + lab3) / 3;
            double classStanding = (attendance * 0.4) + (labAverage * 0.6);
            
            double exam75 = Math.min(100, Math.max(0, (75 - (classStanding * 0.3)) / 0.7));
            double exam100 = Math.min(100, Math.max(0, (100 - (classStanding * 0.3)) / 0.7));
            
            labAvgLabel.setText(String.format("%.2f", labAverage));
            attScoreLabel.setText(String.format("%.0f", attendance));
            classScoreLabel.setText(String.format("%.2f", classStanding));
            exam75Label.setText(String.format("%.2f", exam75));
            exam100Label.setText(String.format("%.2f", exam100));
            
            classStandingCircle.setText(String.format("%.0f", classStanding));
            classScoreDisplay.setText(String.format("Class Standing: %.2f", classStanding));
            classStandingCircle.repaint();
            
            String standing = "Fair";
            String description = "Keep working hard to improve your standing.";
            
            if (classStanding >= 90) {
                standing = "High";
                description = "<html><center>Your class standing is <b>High</b>! You have a strong performance.</center></html>";
            } else if (classStanding >= 75) {
                standing = "Good";
                description = "<html><center>Your class standing is <b>Good</b>! You're doing well.</center></html>";
            }
            
            standingBadge.setText(standing + " Class Standing");
            descriptionLabel.setText(description);
            
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }
    
    private void reset() {
        lab1Field.setText("0");
        lab2Field.setText("0");
        lab3Field.setText("0");
        attendanceField.setText("0");
        calculate();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GradeCalculator().setVisible(true);
        });
    }
}s