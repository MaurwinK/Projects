import javax.swing.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Hotelreservationsystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "[your password]";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1) Reserve a room");
                System.out.println("2) View reservation");
                System.out.println("3) Get Room Number");
                System.out.println("4) Update reservation");
                System.out.println("5) Delete reservation");
                System.out.println("6) Exit");
                System.out.println("Please choose an option!");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        reserveRoom(connection, sc);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, sc);
                        break;
                    case 4:
                        updateReservation(connection, sc);
                        break;
                    case 5:
                        deleteReservation(connection, sc);
                        break;
                    case 6:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice try again!!");
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    private static void reserveRoom(Connection connection, Scanner sc){
        try{
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();

            String sql = "INSERT INTO reservation (guest_name, room_number, contact_number) VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            try(Statement statement = connection.createStatement()){
                int affectedrows = statement.executeUpdate(sql);

                if (affectedrows>0){
                    System.out.println("Reservation successful!");
                }
                else {
                    System.out.println("Reservation failed");
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static void viewReservation(Connection connection) throws SQLException{
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservation; ";

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

            System.out.println("Current reservations: ");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s  |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }

    private static void getRoomNumber (Connection connection, Scanner sc) {
        try {
            System.out.println("Enter reservation id: ");
            int reservationId = sc.nextInt();
            System.out.println("Enter guest name: ");
            String guestName = sc.next();

            String sql = "SELECT room_number FROM reservation WHERE reservation_id =" + reservationId + " AND guest_name ='" + guestName + "';";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for reservation ID " + reservationId + " and guest name " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID or guest name!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner sc){
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter guest name to update: ");
            String newGuestName = sc.nextLine();
            System.out.println("Enter room number to update: ");
            int newRoomNumber = sc.nextInt();
            System.out.println("Enter contact number to update: ");
            String newContactNumber = sc.next();

            String sql =  "UPDATE reservation SET guest_name = '"+newGuestName+"', room_number ='"+ newRoomNumber +"', contact_number ='"+newContactNumber+ "' WHERE reservation_id = "+reservationId;

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows>0){
                    System.out.println("UPDATE successful!!");
                }
                else {
                    System.out.println("UPDATE failed");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter the reservation to delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given reservation ID!!");
                return;
            }

            String sql = "DELETE FROM reservation WHERE reservation_id =" + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectRows = statement.executeUpdate(sql);

                if (affectRows > 0) {
                    System.out.println("Reservation successfully deleted!!");
                } else {
                    System.out.println("Reservation deletion failed!!");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection connection, int reservationId){
        try{
            String sql = "SELECT reservation_id FROM reservation WHERE reservation_id =" +reservationId;
            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                // If there's a result, the reservation exists
                return resultSet.next();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
    private static void exit() throws InterruptedException{
        System.out.print("Exiting System!");
        int i = 5;
        while (i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("Extremely thankful for using my Hotel Reservation System!!!");
    }
}
