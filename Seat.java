import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Seat {
    static ArrayList<String> seatList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        declareSeats();
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Roll No: ");
        String rollNo = scanner.nextLine();
        
        System.out.print("Enter Class: ");
        String classDetail = scanner.nextLine();
        
        if (name.isEmpty() || rollNo.isEmpty() || classDetail.isEmpty()) {
            System.out.println("Please fill all fields to get a seat.");
            return;
        }

        allocateSeat(name, rollNo, classDetail);
        displayTableData();
    }

    public static void declareSeats() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 10; j++) {
                seatList.add("R" + i + "S" + j);
            }
        }
        try {
            String query = "SELECT SEAT FROM seat";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                seatList.remove(rs.getString(1));
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void allocateSeat(String name, String rollNo, String classDetail) {
        try {
            if (seatList.isEmpty()) {
                System.out.println("THERE ARE NO SEATS AVAILABLE");
                return;
            }

            Random rn = new Random();
            String seatNo = seatList.remove(rn.nextInt(seatList.size()));

            String sql = "INSERT INTO seat (NAME, ROLL_NO, CLASS, SEAT) VALUES (?, ?, ?, ?)";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "root");
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, rollNo);
            statement.setString(3, classDetail);
            statement.setString(4, seatNo);
            statement.executeUpdate();
            connection.close();
            
            System.out.println("RECORD ADDED SUCCESSFULLY. Assigned Seat: " + seatNo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void displayTableData() {
        try {
            String query = "SELECT * FROM seat";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            System.out.println("\nStored Data:");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("NAME") + ", Roll No: " + rs.getString("ROLL_NO") + 
                        ", Class: " + rs.getString("CLASS") + ", Seat: " + rs.getString("SEAT"));
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
