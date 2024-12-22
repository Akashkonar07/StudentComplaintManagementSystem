import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Database connection class
class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:ur host/DB name"; // Ensure the database name is correct
    private static final String USER = "postgres"; // Ensure the username is correct
    private static final String PASSWORD = "your data base password"; // Replace with the correct password


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Main application class
class StudentComplaintManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    // Login Frame
    static class LoginFrame extends JFrame {
        private JTextField emailField;
        private JPasswordField passwordField;

        public LoginFrame() {
            setTitle("Login");
            setSize(300, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(4, 2));

            emailField = new JTextField();
            passwordField = new JPasswordField();
            JButton studentLoginButton = new JButton("Student Login");
            JButton adminLoginButton = new JButton("Admin Login");

            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(studentLoginButton);
            add(adminLoginButton);

            studentLoginButton.addActionListener(e -> authenticateUser("student"));
            adminLoginButton.addActionListener(e -> authenticateUser("admin"));

            pack();
            setVisible(true);
        }

        private void authenticateUser(String userType) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            int userId = -1;
            if (userType.equals("student")) {
                userId = authenticate("student", email, password);
                if (userId != -1) {
                    new StudentFrame(userId, email).setVisible(true);
                    dispose(); // Close login frame
                }
            } else {
                userId = authenticate("admin", email, password);
                if (userId != -1) {
                    new AdminFrame(userId).setVisible(true);
                    dispose(); // Close login frame
                }
            }

            if (userId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private int authenticate(String userType, String email, String password) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT id FROM " + userType + " WHERE email = ? AND password = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, email);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1; // Invalid credentials
        }
    }

    // Student Frame - Dashboard after Login
    static class StudentFrame extends JFrame {
        private final int studentId;
        private final String studentEmail;

        public StudentFrame(int studentId, String email) {
            this.studentId = studentId;
            this.studentEmail = email;

            setTitle("Student Dashboard");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(4, 1));

            add(new JLabel("Welcome, " + email, SwingConstants.CENTER));

            JButton fileComplaintButton = new JButton("File Complaint");
            JButton viewComplaintsButton = new JButton("View My Complaints");
            JButton logoutButton = new JButton("Logout");

            fileComplaintButton.addActionListener(e -> new FileComplaintFrame(studentId).setVisible(true));
            viewComplaintsButton.addActionListener(e -> new ViewComplaintsFrame(studentId).setVisible(true));
            logoutButton.addActionListener(e -> {
                dispose(); // Close the current window
                new LoginFrame().setVisible(true); // Open the login frame again
            });

            add(fileComplaintButton);
            add(viewComplaintsButton);
            add(logoutButton);
            pack(); // Adjusts the frame size to fit the components
        }
    }

    // File Complaint Frame
    static class FileComplaintFrame extends JFrame {
        private JComboBox<String> categoryComboBox;
        private JTextArea complaintTextArea;

        public FileComplaintFrame(int studentId) {
            setTitle("File Complaint");
            setSize(400, 300);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(4, 1));

            categoryComboBox = new JComboBox<>(new String[]{
                    "Class Concern (Handled by Class In-Charge)",
                    "Academic Issue (Handled by HOD)",
                    "Council Issue (Handled by Council In-Charge)",
                    "Ragging Issue (Handled by Ragging Cell)",
                    "Infrastructure Issue (Handled by Administration)",
                    "Policy Violation (Handled by Principal)"
            });
            complaintTextArea = new JTextArea();

            add(new JLabel("Category:"));
            add(categoryComboBox);
            add(new JLabel("Complaint:"));
            add(new JScrollPane(complaintTextArea));

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(new SubmitAction(studentId));
            add(submitButton);
            pack(); // Adjusts the frame size to fit the components
        }

        private class SubmitAction implements ActionListener {
            private final int studentId;

            public SubmitAction(int studentId) {
                this.studentId = studentId;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String category = (String) categoryComboBox.getSelectedItem();
                String complaint = complaintTextArea.getText().trim();

                if (complaint.isEmpty()) {
                    JOptionPane.showMessageDialog(FileComplaintFrame.this, "Complaint cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Prevent submission of empty complaint
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO complaint (student_id, category, complaint_text, assigned_role) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, studentId);
                    pstmt.setString(2, category);
                    pstmt.setString(3, complaint);
                    pstmt.setString(4, determineRole(category)); // Set assigned role
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(FileComplaintFrame.this, "Complaint submitted successfully!");
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FileComplaintFrame.this, "Error submitting complaint.");
                }
            }

            private String determineRole(String category) {
                switch (category) {
                    case "Class Concern (Handled by Class In-Charge)":
                        return "Class In-Charge";
                    case "Academic Issue (Handled by HOD)":
                        return "HOD";
                    case "Council Issue (Handled by Council In-Charge)":
                        return "Council In-Charge";
                    case "Ragging Issue (Handled by Ragging Cell)":
                        return "Ragging Cell";
                    case "Infrastructure Issue (Handled by Administration)":
                        return "Administration";
                    case "Policy Violation (Handled by Principal)":
                        return "Principal";
                    default:
                        return "Unknown"; // Default case
                }
            }
        }
    }

    // Admin Frame
    static class AdminFrame extends JFrame {
        private int adminId;
        private String adminRole; // To store the role of the admin
        private JTable complaintsTable;
        private DefaultTableModel complaintsModel;

        public AdminFrame(int adminId) {
            this.adminId = adminId;
            this.adminRole = getAdminRole(adminId);

            setTitle("Admin Dashboard");
            setSize(600, 400);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            String[] columnNames = {"ID", "Student ID", "Category", "Complaint Text", "Assigned Role", "Status", "Date"};
            complaintsModel = new DefaultTableModel(columnNames, 0);
            complaintsTable = new JTable(complaintsModel);
            JScrollPane scrollPane = new JScrollPane(complaintsTable);
            add(scrollPane, BorderLayout.CENTER);

            JButton loadComplaintsButton = new JButton("Load My Complaints");
            loadComplaintsButton.addActionListener(e -> loadComplaints());
            JButton updateStatusButton = new JButton("Update Status");
            updateStatusButton.addActionListener(e -> updateComplaintStatus());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(loadComplaintsButton);
            buttonPanel.add(updateStatusButton);
            add(buttonPanel, BorderLayout.SOUTH);
            pack(); // Adjusts the frame size to fit the components
        }

        private String getAdminRole(int adminId) {
            String role = "";
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT role.role_name FROM admin JOIN role ON admin.role_id = role.id WHERE admin.id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, adminId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    role = rs.getString("role_name");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return role;
        }

        private void loadComplaints() {
            complaintsModel.setRowCount(0); // Clear previous data
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM complaint WHERE assigned_role = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, adminRole); // Use the admin's role to filter complaints
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getInt("student_id"),
                            rs.getString("category"),
                            rs.getString("complaint_text"),
                            rs.getString("assigned_role"),
                            rs.getString("status"),
                            rs.getDate("date")
                    };
                    complaintsModel.addRow(row);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading complaints.");
            }
        }

        private void updateComplaintStatus() {
            int selectedRow = complaintsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int complaintId = (int) complaintsModel.getValueAt(selectedRow, 0);
                String newStatus = JOptionPane.showInputDialog(this, "Enter new status:");
                if (newStatus != null && !newStatus.trim().isEmpty()) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "UPDATE complaint SET status = ? WHERE id = ?";
                        PreparedStatement pst = conn.prepareStatement(query);
                        pst.setString(1, newStatus);
                        pst.setInt(2, complaintId);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Complaint status updated successfully!");
                        loadComplaints(); // Refresh the table
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error updating status.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a complaint to update.");
            }
        }
    }

    // View Complaints Frame
    static class ViewComplaintsFrame extends JFrame {
        private int studentId;
        private JTable complaintsTable;
        private DefaultTableModel complaintsModel;

        public ViewComplaintsFrame(int studentId) {
            this.studentId = studentId;

            setTitle("My Complaints");
            setSize(600, 400);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            String[] columnNames = {"ID", "Category", "Complaint Text", "Assigned Role", "Status", "Date"};
            complaintsModel = new DefaultTableModel(columnNames, 0);
            complaintsTable = new JTable(complaintsModel);
            JScrollPane scrollPane = new JScrollPane(complaintsTable);
            add(scrollPane, BorderLayout.CENTER);

            JButton loadComplaintsButton = new JButton("Load My Complaints");
            loadComplaintsButton.addActionListener(e -> loadComplaints());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(loadComplaintsButton);
            add(buttonPanel, BorderLayout.SOUTH);
            pack(); // Adjusts the frame size to fit the components
        }

        private void loadComplaints() {
            complaintsModel.setRowCount(0); // Clear previous data
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM complaint WHERE student_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, studentId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("category"),
                            rs.getString("complaint_text"),
                            rs.getString("assigned_role"),
                            rs.getString("status"),
                            rs.getDate("date")
                    };
                    complaintsModel.addRow(row);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading complaints.");
            }
        }
    }
}