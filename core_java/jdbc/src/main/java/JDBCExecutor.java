import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCExecutor {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/hplussport";
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");
        DatabaseConnectionManager conManager = new DatabaseConnectionManager(url, properties);
        try (Connection conn = conManager.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            Customer customer = new Customer(6789, "John", "Adams",
                    "jadams.wh.gov", "(555) 555-9845", "1234 Main St",
                    "Arlington", "VA", "01234");
            customerDAO.create(customer);
            customerDAO.findById(customer.getId()).ifPresent(System.out::println);

            customer = new Customer(6789, "John", "Adams",
                    "jadams", "(555) 555-9845", "1234 Main St",
                    "Arlington", "VA", "01234");
            customerDAO.update(customer);
            customerDAO.findById(customer.getId()).ifPresent(System.out::println);

            customerDAO.delete(customer.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
