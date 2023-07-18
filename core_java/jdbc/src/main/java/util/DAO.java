package util;

import lombok.AllArgsConstructor;
import java.sql.*;
import java.util.Optional;

@AllArgsConstructor
public abstract class DAO<T extends DTO> {

    protected final Connection conn;

    public abstract Optional<T> findById(long id);
    public abstract Optional<T>  update(T dto);
    public abstract Optional<T>  create(T dto);
    public abstract void delete(long id);

    protected int getLastVal(String table){
        int key = 0;
        String sql = "SELECT last_value FROM" + table;
        try(Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(sql)){
            while(res.next()){
                key = res.getInt(1);
            }
            return key;
        }catch (SQLException e ){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
