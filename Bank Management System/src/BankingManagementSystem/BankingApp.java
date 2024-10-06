package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/Bank";
    private static final String username = "root";
    private static final String password = "[your password]";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers loaded successfully!");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //Establish the connection
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            System.out.println("Connected to the database!");
            //Perform database operations here
            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            Accounts accounts = new Accounts(connection, sc);
            AccountManager accountManager = new AccountManager(connection, sc);

            String email;
            long acc_number;

            while (true){
                System.out.println("********** WELCOME TO BANKING SYSTEM **********");
                System.out.println();
                System.out.println("1) Register new user");
                System.out.println("2) Login");
                System.out.println("3) EXIT");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User logged IN!");
                            if(!accounts.account_exists(email)){
                                System.out.println();
                                System.out.println("1) Open a new bank account");
                                System.out.println("2) EXIT");
                                if (sc.nextInt()==1){
                                    acc_number = accounts.open_account(email);
                                    System.out.println("Account created SUCCESSFULLY!!!");
                                    System.out.println("Your account number is: "+acc_number);
                                }
                                else {
                                    break;
                                }
                            }
                            acc_number = accounts.getAccount_number(email);
                            int choice1 = 0;
                            while (choice1!=5){
                                System.out.println();
                                System.out.println("1) Debit Money");
                                System.out.println("2) Credit Money");
                                System.out.println("3) Transfer Money");
                                System.out.println("4) Check your balance: ");
                                System.out.println("5) Log Out");
                                System.out.println("Enter your choice: ");
                                choice1= sc.nextInt();
                                switch (choice1){
                                    case 1:
                                        accountManager.debit_money(acc_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(acc_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(acc_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(acc_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Please enter a valid choice!!!");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("INCORRECT email id or password!!!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting the SYSTEM!!!");
                        return;

                    default:
                        System.out.println("Please enter VALID choice");
                        break;
                }
            }
        }
        catch (SQLException e){
            System.err.println("Connection failed: "+ e.getMessage());
        }
    }
}
