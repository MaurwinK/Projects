package HospitalManagementSystem;

import com.mysql.cj.util.DnsSrv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatient(){
        System.out.println("Enter the patient's full name: ");
        String name = scanner.next();
        System.out.println("Enter the patient's age: ");
        int age =scanner.nextInt();
        System.out.println("Enter the patient's gender: ");
        String gender = scanner.next();

        try{
            String query ="INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectRows = preparedStatement.executeUpdate();
            if(affectRows>0){
                System.out.println("Patient added SUCCESSFULLY!!!");
            }
            else {
                System.out.println("FAILED to add the patient");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-12s | %-20s | %-10s | %-12s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getpatientByid(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) {

    }
}
