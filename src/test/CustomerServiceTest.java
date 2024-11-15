package Customer_Management_API;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import Customer_Management_API.entity.Customer;
import Customer_Management_API.repository.CustomerRepository;
import Customer_Management_API.response.CustomerResponse;
import Customer_Management_API.service.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer();
        customer.setName("Merry Trump");
        customer.setEmail("Merry.Trump@example.com");
        customer.setAnnualSpend(new BigDecimal("1500.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(6));

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> response = customerService.createCustomer(customer);
        assertNotNull(response.getBody());
        assertEquals("Merry Trump", response.getBody().getName());
    }

    @Test
    void getCustomerById() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Merry Trump");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        ResponseEntity<Customer> response = customerService.getCustomerById(id);
        assertEquals("Merry Trump", response.getBody().getName());
    }

    @Test
    void getCustomerByNameOrEmail() {
        Customer customer1 = new Customer();
        customer1.setName("Merry Trump");
        Customer customer2 = new Customer();
        customer2.setEmail("john.doe@example.com");

        when(customerRepository.findByName("Merry Trump")).thenReturn(Arrays.asList(customer1));
        when(customerRepository.findByEmail("Merry.Trump@example.com")).thenReturn(Arrays.asList(customer2));

        ResponseEntity<List<Customer>> responseByName = customerService.getCustomerByNameOrEmail("Merry Trump", null);
        ResponseEntity<List<Customer>> responseByEmail = customerService.getCustomerByNameOrEmail(null, "Merry.Trump@example.com");

        assertEquals(1, responseByName.getBody().size());
        assertEquals("Merry Trump", responseByName.getBody().get(0).getName());
        assertEquals(1, responseByEmail.getBody().size());
        assertEquals("Merry.Trump@example.com", responseByEmail.getBody().get(0).getEmail());
    }

    @Test
    void updateCustomer() {
        UUID id = UUID.randomUUID();
        Customer existingCustomer = new Customer();
        existingCustomer.setId(id);
        existingCustomer.setName("Merry Trump");

        Customer customerDetails = new Customer();
        customerDetails.setName("Mariya Trump");
        customerDetails.setEmail("Mariya.Trump@example.com");
        customerDetails.setAnnualSpend(new BigDecimal("2000.00"));
        customerDetails.setLastPurchaseDate(LocalDate.now());

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        ResponseEntity<Customer> response = customerService.updateCustomer(id, customerDetails);

        assertNotNull(response.getBody());
        assertEquals("Mariya Trump", response.getBody().getName());
        assertEquals("Mariya.Trump@example.com", response.getBody().getEmail());
    }

    @Test
    void deleteCustomer() {
        UUID id = UUID.randomUUID();

        doNothing().when(customerRepository).deleteById(id);

        ResponseEntity<Void> response = customerService.deleteCustomer(id);

        verify(customerRepository, times(1)).deleteById(id);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void calculateTier() {
        assertEquals("Platinum", CustomerService.calculateTier(new BigDecimal("15000.00"), LocalDate.now().minusMonths(1)));
        assertEquals("Gold", CustomerService.calculateTier(new BigDecimal("5000.00"), LocalDate.now().minusMonths(10)));
        assertEquals("Silver", CustomerService.calculateTier(new BigDecimal("500.00"), LocalDate.now().minusMonths(5)));
        assertEquals("None", CustomerService.calculateTier(new BigDecimal("5000.00"), LocalDate.now().minusMonths(13)));
    }

    @Test
    void getCustomerWithTier() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Merry Trump");
        customer.setAnnualSpend(new BigDecimal("1500.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(6));

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        ResponseEntity<CustomerResponse> response = customerService.getCustomerWithTier(id);

        assertNotNull(response.getBody());
        assertEquals("Gold", response.getBody().getTier());
    }
}
