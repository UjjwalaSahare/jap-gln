package Customer_Management_API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import Customer_Management_API.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
	List<Customer> findByName(String name);
    List<Customer> findByEmail(String email);

}
