package Customer_Management_API.response;

import Customer_Management_API.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerResponse {
    private Customer customer;
    private String tier;

    // Getters and Setters
}