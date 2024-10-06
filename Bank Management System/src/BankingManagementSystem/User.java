package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;
    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public void register(){
        sc.nextLine();
        System.out.println("Enter your full name: ");
        String full_name = sc.nextLine();
        System.out.println("Enter email id: ");
        String email = sc.nextLine();
        System.out.println("Set your password: ");
        String password = sc.nextLine();
        if(user_exists(email)){
            System.out.println("A user with same email id already exists!!!");
            return;
        }
        String register_query ="INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectRows = preparedStatement.executeUpdate();
            if(affectRows>0){
                System.out.println("Registration SUCCESSFULLY DONE!!!");
            }
            else {
                System.out.println("Registration FAILED!!!");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public  String login(){
        sc.nextLine();
        System.out.println("Enter your email: ");
        String email = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }
            else {
                System.out.println("Please enter correct email id or password!!!");
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exists(String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else {
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
