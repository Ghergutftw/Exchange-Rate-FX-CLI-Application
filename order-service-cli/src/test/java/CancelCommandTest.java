import com.pm.commands.CancelCommand;
import com.pm.models.Currency;
import com.pm.models.Order;
import com.pm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CancelCommandTest {
    @Mock
    private OrderService orderService;
    
    private CancelCommand command;
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new CancelCommand(orderService);
        random = new Random();
    }

    @Test
    void execute_WithValidOrderId_ShouldCancelOrder() throws Exception {
        List<Order> orders = List.of(
            createOrder("1"),
            createOrder("2"),
            createOrder("3")
        );

        when(orderService.getAllOrders()).thenReturn(orders);

        Order randomOrder = orders.get(random.nextInt(orders.size()));
        String randomOrderId = randomOrder.getId();
        
        String[] args = {"cancel", randomOrderId};
        when(orderService.cancelOrder(randomOrderId)).thenReturn(true);

        assertDoesNotThrow(() -> command.execute(args));
    }

    @Test
    void execute_WithInvalidArgs_ShouldThrowException() {
        String[] args = {"cancel"};
        assertThrows(IllegalArgumentException.class, () -> command.execute(args));
    }

    @Test
    void execute_WhenOrderNotFound_ShouldHandleGracefully() throws Exception {
        String[] args = {"cancel", "nonexistent"};
        when(orderService.cancelOrder("nonexistent")).thenReturn(false);
        assertDoesNotThrow(() -> command.execute(args));
    }

    @Test
    void execute_WhenServiceThrowsException_ShouldWrapException() throws Exception {
        List<Order> orders = List.of(createOrder("1"));
        when(orderService.getAllOrders()).thenReturn(orders);
        String randomOrderId = orders.get(0).getId();
        
        String[] args = {"cancel", randomOrderId};
        when(orderService.cancelOrder(anyString())).thenThrow(new RuntimeException("Service error"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> command.execute(args));
        assertTrue(exception.getMessage().contains("Failed to cancel order"));
    }

    private Order createOrder(String id) {
        Order order = new Order();
        order.setId(id);
        order.setBuy(true);
        order.setInvestmentCcy(Currency.EUR.name());
        order.setCounterCcy(Currency.USD.name());
        order.setLimit(1.2);
        order.setValidUntil(LocalDate.now().plusDays(7));
        return order;
    }
}