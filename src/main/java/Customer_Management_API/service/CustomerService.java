package Customer_Management_API.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import Customer_Management_API.entity.Customer;
import Customer_Management_API.repository.CustomerRepository;
import Customer_Management_API.response.CustomerResponse;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;

    public ResponseEntity<Customer> createCustomer(Customer customer) {
        customer.setId(UUID.randomUUID());
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    public ResponseEntity<Customer> getCustomerById(UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Customer>> getCustomerByNameOrEmail(
            String name,
            String email
    ) {
        if (name != null) {
            return ResponseEntity.ok(customerRepository.findByName(name));
        } else if (email != null) {
            return ResponseEntity.ok(customerRepository.findByEmail(email));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Customer> updateCustomer(UUID id, Customer customerDetails) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setName(customerDetails.getName());
            customer.setEmail(customerDetails.getEmail());
            customer.setAnnualSpend(customerDetails.getAnnualSpend());
            customer.setLastPurchaseDate(customerDetails.getLastPurchaseDate());
            return ResponseEntity.ok(customerRepository.save(customer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static String calculateTier(BigDecimal annualSpend, LocalDate lastPurchaseDate) {
        if (annualSpend.compareTo(new BigDecimal("10000")) >= 0 && lastPurchaseDate.isAfter(LocalDate.now().minusMonths(6))) {
            return "Platinum";
        } else if (annualSpend.compareTo(new BigDecimal("1000")) >= 0 && lastPurchaseDate.isAfter(LocalDate.now().minusMonths(12))) {
            return "Gold";
        } else if (annualSpend.compareTo(new BigDecimal("1000")) < 0) {
            return "Silver";
        }
        return "None";
    }

    public ResponseEntity<CustomerResponse> getCustomerWithTier(UUID id) {

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            String tier = calculateTier(customer.getAnnualSpend(), customer.getLastPurchaseDate());
            CustomerResponse customerResponse = new CustomerResponse(customer, tier);
            return ResponseEntity.ok(customerResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}