import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class StudentManagementApp extends JFrame {
    private JTextField nameField, rollField, gradeField, emailField;
    private JTextArea displayArea;
    private ArrayList<Student> students;
    private final String fileName = "students.dat";

    public StudentManagementApp() {
        students = loadStudentsFromFile();
        createGUI();
    }

    static class Student implements Serializable {
        private String name, rollNumber, grade, email;

        public Student(String name, String rollNumber, String grade, String email) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.grade = grade;
            this.email = email;
        }

        public String getRollNumber() { return rollNumber; }

        @Override
        public String toString() {
            return rollNumber + " - " + name + " (" + grade + "), " + email;
        }
    }

    private void createGUI() {
        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        nameField = new JTextField();
        rollField = new JTextField();
        gradeField = new JTextField();
        emailField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Search");
        inputPanel.add(addButton);
        inputPanel.add(searchButton);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove");
        JButton displayButton = new JButton("Display All");
        buttonPanel.add(removeButton);
        buttonPanel.add(displayButton);

        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        searchButton.addActionListener(e -> searchStudent());
        removeButton.addActionListener(e -> removeStudent());
        displayButton.addActionListener(e -> displayAllStudents());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String grade = gradeField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || roll.isEmpty() || grade.isEmpty() || email.isEmpty()) {
            showMessage("All fields are required.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMessage("Invalid email format.");
            return;
        }

        students.add(new Student(name, roll, grade, email));
        saveStudentsToFile();
        showMessage("Student added successfully.");
        clearFields();
    }

    private void searchStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            showMessage("Enter roll number to search.");
            return;
        }

        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                displayArea.setText("Found:\n" + s.toString());
                return;
            }
        }

        displayArea.setText("Student not found.");
    }

    private void removeStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            showMessage("Enter roll number to remove.");
            return;
        }

        boolean removed = students.removeIf(s -> s.getRollNumber().equalsIgnoreCase(roll));
        saveStudentsToFile();

        if (removed) {
            showMessage("Student removed.");
        } else {
            showMessage("Student not found.");
        }
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            displayArea.setText("No students available.");
            return;
        }

        StringBuilder sb = new StringBuilder("All Students:\n");
        for (Student s : students) {
            sb.append(s.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void saveStudentsToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Student> loadStudentsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<Student>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementApp::new);
    }
}