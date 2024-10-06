package BankingManagementSystem;

import javax.accessibility.AccessibleTable;
import java.nio.channels.ScatteringByteChannel;
import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;
    public Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public long open_account(String email){
        if(!account_exists(email)){
            String open_acc_query = "INSERT INTO accounts (acc_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.println("Enter your full name: ");
            String fullName = sc.nextLine();
            System.out.println("Enter initial amount to deposit: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter your security pin: ");
            String security_pin = sc.nextLine();
            try {
                long acc_number = generateAccountnumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_acc_query);
                preparedStatement.setLong(1, acc_number);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int affectRows = preparedStatement.executeUpdate();
                if(affectRows>0){
                    return acc_number;
                }
                else {
                    throw new RuntimeException("Account creation FAILED!!!");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already EXISTS!!!");

    }
    public long getAccount_number(String email){
        String query = "SELECT acc_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("acc_number");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Such Account Number DOESN'T EXISTS!!!");

    }
    private long generateAccountnumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT acc_number FROM accounts ORDER BY acc_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_acc_number = resultSet.getLong("acc_number");
                return last_acc_number+1;
            }
            else {
                return 10000100;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 1000100;
    }

    public boolean account_exists(String email){
        String query = "SELECT acc_number FROM accounts WHERE email = ?";
        try {
            {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return true;
                }
                else{
                    return false;
                }
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
