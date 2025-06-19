import com.pm.commands.NewOrderCommand;
import com.pm.models.Order;
import com.pm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class NewOrderCommandTest {
    @Mock
    private OrderService orderService;
    
    private NewOrderCommand command;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new NewOrderCommand(orderService);
    }

    @Test
    void execute_WithValidArguments_ShouldCreateOrder() throws Exception {
        String[] args = {"new", "buy", "EUR", "USD", "1.2345", "20.06.2025"};
        Order mockOrder = new Order();
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);

        assertDoesNotThrow(() -> command.execute(args));
    }

    @Test
    void execute_WithDateInPast_ShouldThrowException() {
        String[] args = {"new", "buy", "EUR", "USD", "1.2345", "20.06.2023"};
        
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> command.execute(args));
        assertTrue(exception.getMessage().contains("Validity date must be in the future"));
    }

    @Test
    void execute_WithInvalidDateFormat_ShouldThrowException() {
        String[][] invalidDateArgs = {
            {"new", "buy", "EUR", "USD", "1.2345", "2025-06-20"},  // Wrong format (ISO)
            {"new", "buy", "EUR", "USD", "1.2345", "20/06/2025"},  // Wrong separator
            {"new", "buy", "EUR", "USD", "1.2345", "6.20.2025"},   // Wrong order
            {"new", "buy", "EUR", "USD", "1.2345", "aa.bb.cccc"},  // Non-numeric
            {"new", "buy", "EUR", "USD", "1.2345", "35.13.2025"},  // Invalid day/month
            {"new", "buy", "EUR", "USD", "1.2345", "20.06.25"}     // Short year
        };

        for (String[] args : invalidDateArgs) {
            Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> command.execute(args),
                "Should throw exception for invalid date format: " + args[5]);
            assertTrue(exception.getMessage().contains("Invalid date format"));
        }
    }

    @Test
    void execute_WithFarFutureDate_ShouldCreateOrder() throws Exception {
        String[] args = {"new", "buy", "EUR", "USD", "1.2345", "31.12.2030"};
        Order mockOrder = new Order();
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);

        assertDoesNotThrow(() -> command.execute(args));
    }

    @Test
    void execute_WithInvalidNumberOfArguments_ShouldThrowException() {
        String[] args = {"new", "buy", "EUR", "USD"};
        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }

    @Test
    void execute_WithInvalidOrderType_ShouldThrowException() {
          
        String[] args = {"new", "invalid", "EUR", "USD", "1.2345", "20.06.2025"};

         
        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }

    @Test
    void execute_WithInvalidCurrency_ShouldThrowException() {
          
        String[] args = {"new", "buy", "XYZ", "USD", "1.2345", "20.06.2025"};

        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }

    @Test
    void execute_WithInvalidLimit_ShouldThrowException() {
          
        String[] args = {"new", "buy", "EUR", "USD", "invalid", "20.06.2025"};

         
        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }

    @Test
    void execute_WithInvalidDate_ShouldThrowException() {
          
        String[] args = {"new", "buy", "EUR", "USD", "1.2345", "invalid"};

         
        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }
}