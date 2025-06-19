import com.pm.commands.SummaryCommand;
import com.pm.models.Currency;
import com.pm.models.Order;
import com.pm.models.OrderType;
import com.pm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SummaryCommandTest {
    @Mock
    private OrderService orderService;
    
    private SummaryCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new SummaryCommand(orderService);
    }

    @Test
    void execute_WithValidOrders_ShouldDisplaySummary() throws Exception {
          
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(OrderType.BUY, Currency.EUR, Currency.USD, 1.2345, LocalDate.now()));
        when(orderService.getAllOrders()).thenReturn(orders);

         
        assertDoesNotThrow(() -> command.execute(new String[]{"summary"}));
    }

    @Test
    void execute_WithEmptyOrders_ShouldHandleGracefully() throws Exception {
          
        when(orderService.getAllOrders()).thenReturn(new ArrayList<>());

         
        assertDoesNotThrow(() -> command.execute(new String[]{"summary"}));
    }

}