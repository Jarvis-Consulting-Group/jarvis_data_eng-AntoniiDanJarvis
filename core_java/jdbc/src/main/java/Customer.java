import lombok.Value;
import util.DTO;

@Value
public class Customer implements DTO {
    long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String city;
    String state;
    String zipCode;
}
