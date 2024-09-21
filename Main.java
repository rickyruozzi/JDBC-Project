import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    private static void add_book(Scanner sc, Connection conn){
        System.out.println("Insert the title of the book: ");
                String title = sc.nextLine();
                System.out.println("Insert the author of the book: ");
                String author = sc.nextLine();
                System.out.println("Insert the year of the book: ");
                String sty=sc.nextLine();
                int year=0;
                try {
                    year = Integer.parseInt(sty);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid pubblication year.");
                }
                System.out.println("Insert book's genre: ");
                String genre = sc.nextLine();
                System.out.println("Insert the book's page number: ");
                String nps = sc.nextLine();
                int num_pag=0;
                try {
                    num_pag = Integer.parseInt(nps);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid page number.");
                }
                System.out.println("Insert the book's price: ");
                String bps = sc.nextLine();
                float price=0;
                try {
                    price = Float.parseFloat(bps);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid book's price.");
                }
                System.out.println("Add an ISBN: ");
                String ISBN = sc.nextLine();
                String query = "INSERT INTO books (ISBN, title, author, pub_year, genre, page_num, price) VALUES (?, ?, ?, ?, ?, ?, ?)"; 
                try {
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, ISBN);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.setInt(4, year);
                    pstmt.setString(5, genre);
                    pstmt.setInt(6, num_pag);
                    pstmt.setFloat(7, price);

                    pstmt.executeUpdate();
                    System.out.println("Book inserted!");
                } catch (SQLException e) {
                    System.out.println("Error while adding the book: " + e.getMessage());
                }
    }

    private static void remove_book(Scanner sc, Connection conn){
        System.out.println("Insert the ISBN of the book to remove: ");
        String ISBN = sc.nextLine();
        String query = "DELETE FROM books WHERE ISBN = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ISBN);
            pstmt.executeUpdate();
            System.out.println("Book removed!");
            } catch (SQLException e) {
                System.out.println("Error while removing the book: " + e.getMessage());
            }
    }

private static void modify_element(Scanner sc, Connection conn) {
    System.out.println("Insert the name of the field to modify: ");
    String campo = sc.nextLine();
    System.out.println("Insert the new value:");
    String valore = sc.nextLine();
    System.out.println("Insert the index of the element to modify:");
    String Sindex = sc.nextLine();
    int index = 0;
    try {
        index = Integer.parseInt(Sindex);
    } catch (NumberFormatException e) {
        System.out.println("Convertion error");
    }

    // Whitelist of allowed column names
    String[] allowedColumns = {"title", "author", "pub_year", "genre", "page_num", "price"};

    if (Arrays.asList(allowedColumns).contains(campo)) {
        String query = "UPDATE books SET " + campo + "=? where id=? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            if (campo.equals("page_num") || campo.equals("pub_year")) {
                int val = 0;
                try {
                    val = Integer.parseInt(valore);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting value to integer");
                }
                pstmt.setInt(1, val);
            } else if (campo.equals("price")) {
                float val = 0;
                try {
                    val = Float.parseFloat(valore);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting value to float");
                }
                pstmt.setFloat(1, val);
            } else {
                pstmt.setString(1, valore);
            }
            pstmt.setInt(2, index);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    } else {
        System.out.println("Invalid column name");
    }
}

    private static void view_books(Scanner sc, Connection conn){
        String query = "SELECT * FROM books";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery(); //salviamo il valore di ritorno della query nell'oggetto ResultSet
            if(resultSet.isBeforeFirst()){
            System.out.println("Books in the database:");
            while (resultSet.next()) {      //resultSet.next() restituisce true se altri elementi sono disponibili
                System.out.println("\nISBN: " + resultSet.getString("ISBN")); //getString() permette di ottenere la stringa che corrisponde nel record al campo ISBN
                System.out.println("Title: " + resultSet.getString("title"));
                System.out.println("Author: " + resultSet.getString("author"));
                System.out.println("Publication Year: " + resultSet.getInt("pub_year"));    //per gli interi usiamo getInt()
                System.out.println("Genre: " + resultSet.getString("genre"));
                System.out.println("Page Number: " + resultSet.getInt("page_num"));
                System.out.println("Price: " + resultSet.getFloat("price"));    //per i Float usiamo getFloat()
                System.out.println("\n");
            }}
            else{
                System.out.println("\nThere are no books in the DB");
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Replace with your database driver
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading JDBC driver: " + e.getMessage());
            return;
        }
        String url = "jdbc:mysql://localhost:3306/library"; // Replace with your database URL
        String username = "root"; // Replace with your database username
        String password = "Ruozzi1234"; // Replace with your database password

        // Create a JDBC connection
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");
            // You can now use the connection to execute queries, etc.
            Scanner sc = new Scanner(System.in);
            boolean flag=true;
            while(flag){
                System.out.println("\nInsert \n1 - To add a book in the DB\n2 - To delete a book in the DB\n3 - To modify a book in the DB\n4 - To view books in the DB\n5 - To quit the program");
                String choice = sc.nextLine();
                switch(choice){
                    case "1":
                        add_book(sc, conn);
                        break;

                    case "2":
                        remove_book(sc, conn);
                        break;

                    case "3":
                        modify_element(sc,conn);
                        break;

                    case "4":
                        view_books(sc, conn);
                        break;

                    case "5":
                        flag=false;
                        System.out.println("Bye Bye!");
                        break;

                    default:
                        System.out.println("Invalid choice");
                        break;
                    }
                }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
}