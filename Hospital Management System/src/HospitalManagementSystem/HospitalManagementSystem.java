package HospitalManagementSystem;

import com.sun.source.tree.BreakTree;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "[your password]";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers loaded successfully!");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            System.out.println("Connected to the database!");
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("*********** WELCOME TO HOSPITAL SYSTEM ***********");
                System.out.println("1) Add patient");
                System.out.println("2) View patient");
                System.out.println("3) View doctors");
                System.out.println("4) Book an appointment");
                System.out.println("5) EXIT");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointments(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("********** THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!! **********");
                        return;
                    default:
                        System.out.println("Enter a valid choice: ");
                        break;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointments(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter patient id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getpatientByid(patientId) && doctor.getdoctorByid(doctorId)){
            if(checkdoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment BOOKED!!!");
                    }
                    else {
                        System.out.println("FAILED to book an appointment!!!");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor NOT available on this date!!!");
            }
        }
        else {
            System.out.println("The patient or doctor doesn't exists!!!");
        }
    }
    public static boolean checkdoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String  query ="SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
