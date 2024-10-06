package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner sc;
    public AccountManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void transfer_money(long senderAccount_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter receiver's account number: ");
        long receiverAccount_number = sc.nextLong();
        System.out.println("Money to be transferred: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter your security PIN: ");
        String security_pin = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if (senderAccount_number!=0 && receiverAccount_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,senderAccount_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query ="UPDATE accounts SET balance = balance - ? WHERE acc_number = ?";
                        String credit_query ="UPDATE accounts SET balance = balance + ? WHERE acc_number = ?";
                        PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                        creditpreparedStatement.setDouble(1,amount);
                        creditpreparedStatement.setLong(2, receiverAccount_number);
                        debitpreparedStatement.setDouble(1, amount);
                        debitpreparedStatement.setLong(2, senderAccount_number);
                        int affectedRows1 = debitpreparedStatement.executeUpdate();
                        int affectedRows2 = creditpreparedStatement.executeUpdate();
                        if(affectedRows1>0 && affectedRows2>0){
                            System.out.println("Transaction SUCCESSFUL!!!");
                            System.out.println("Rs. "+amount+" has been transferred from Acc no. "+senderAccount_number+" to Acc no. "+receiverAccount_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("Money transfer FAILED!!!");
                            connection.rollback();
                            connection.setAutoCommit(false);
                        }
                    }
                    else {
                        System.out.println("INSUFFICIENT balance available!!!");
                    }
                }
                else {
                    System.out.println("Please enter correct security pin!!!");
                }
            }
            else {
                System.out.println("Please enter correct account number!!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void credit_money(long acc_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter amount you want to credit: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter your security PIN: ");
        String security_pin = sc.nextLine();

        try {
            connection.setAutoCommit(false);
            if(acc_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,acc_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_number = ? ";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, acc_number);
                    int affectRows = preparedStatement1.executeUpdate();
                    if(affectRows>0){
                        System.out.println("Rs. "+amount+" is CREDITED successfully to account number "+acc_number);
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else{
                        System.out.println("Transaction FAILED!!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("INCORRECT pin!!!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long acc_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter amount you want to debit: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter your security PIN: ");
        String security_pin = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if(acc_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,acc_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_number = ? ";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, acc_number);
                        int affectRows = preparedStatement1.executeUpdate();
                        if(affectRows>0){
                            System.out.println("Rs. "+amount+" is DEBITED successfully from account number "+acc_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction FAILED!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("INSUFFICIENT balance available!!!");
                    }
                }
                else {
                    System.out.println("INCORRECT pin!!!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long acc_number){
        sc.nextLine();
        System.out.println("Enter your security PIN: ");
        String security_pin = sc.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_number = ? AND security_pin = ?");
            preparedStatement.setLong(1,acc_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Your balance is: "+balance);
            }
            else {
                System.out.println("Invalid SECURITY PIN!!!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
