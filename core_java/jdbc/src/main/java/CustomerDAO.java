import util.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerDAO extends DAO<Customer> {
    private static final String DB_NAME= "customer";
    private static final String INSERT_ALL = "INSERT INTO " + DB_NAME + " (customer_id, first_name, last_name," +
            "email, phone, address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL = "SELECT customer_id, first_name, last_name, " +
            "email, phone, address, city, state, zipcode FROM " + DB_NAME;

    private static final String UPDATE_ALL = "UPDATE " + DB_NAME + " SET first_name = ?, last_name = ?, " +
            "email = ?, phone = ?, address = ?, city = ?, state = ?, zipcode = ? WHERE customer_id = ?";

    private static final String DELETE = "DELETE FROM " +  DB_NAME + " WHERE customer_id = ?";

    public CustomerDAO(Connection conn) {
        super(conn);
    }

    @Override
    public Optional<Customer> findById(long id) {
        String stat = GET_ALL + " WHERE " + " customer_id=?";
        Optional<Customer> empty = Optional.empty();
        try(PreparedStatement statement = super.conn.prepareStatement(stat)) {
            statement.setLong(1, id);
            ResultSet res = statement.executeQuery();
            res.next();
            return Optional.of(new Customer(
                    res.getLong("customer_id"),
                    res.getString("first_name"),
                    res.getString("last_name"),
                    res.getString("email"),
                    res.getString("phone"),
                    res.getString("address"),
                    res.getString("city"),
                    res.getString("state"),
                    res.getString("zipcode")));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return empty;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Optional<Customer> update(Customer dto) {
        try(PreparedStatement statement = super.conn.prepareStatement(UPDATE_ALL)) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getAddress());
            statement.setString(6, dto.getCity());
            statement.setString(7, dto.getState());
            statement.setString(8, dto.getZipCode());
            statement.setLong(9, dto.getId());
            statement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return findById(dto.getId());
    }

    @Override
    public Optional<Customer> create(Customer dto) {
        try(PreparedStatement statement = super.conn.prepareStatement(INSERT_ALL)){
            statement.setLong(1, dto.getId());
            statement.setString(2, dto.getFirstName());
            statement.setString(3, dto.getLastName());
            statement.setString(4, dto.getEmail());
            statement.setString(5, dto.getPhone());
            statement.setString(6, dto.getAddress());
            statement.setString(7, dto.getCity());
            statement.setString(8, dto.getState());
            statement.setString(9, dto.getZipCode());
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return findById(dto.getId());
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement statement = super.conn.prepareStatement(DELETE)){
            statement.setLong(1, id);
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
