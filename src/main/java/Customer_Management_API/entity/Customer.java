package Customer_Management_API.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Customer {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private UUID id;

	    @NotNull(message = "Name is required")
	    private String name;

	    @NotNull(message = "Email is required")
	    @Email(message = "Email should be valid")
	    private String email;

	    private BigDecimal annualSpend;
	    private LocalDate lastPurchaseDate;


}
