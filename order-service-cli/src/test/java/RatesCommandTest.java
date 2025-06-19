import com.pm.commands.RatesCommand;
import com.pm.models.Currency;
import com.pm.models.CurrencyPair;
import com.pm.models.FXRate;
import com.pm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RatesCommandTest {
    @Mock
    private OrderService orderService;
    
    private RatesCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        command = new RatesCommand(orderService);
    }

    @Test
    void execute_WithExistingRates_ShouldDisplayRates() throws Exception {
          
        List<FXRate> rates = new ArrayList<>();
        rates.add(new FXRate(new CurrencyPair(Currency.EUR, Currency.USD), 1.1000, 1.1001));
        when(orderService.getExchangeRates()).thenReturn(rates);

         
        assertDoesNotThrow(() -> command.execute(new String[]{"rates"}));
    }

    @Test
    void execute_WithEmptyRates_ShouldHandleGracefully() throws Exception {
          
        when(orderService.getExchangeRates()).thenReturn(new ArrayList<>());

         
        assertDoesNotThrow(() -> command.execute(new String[]{"rates"}));
    }

    @Test
    void execute_WhenServiceThrowsException_ShouldPropagateException() throws Exception {
          
        when(orderService.getExchangeRates()).thenThrow(new RuntimeException("Service error"));

         
        assertThrows(Exception.class, () -> command.execute(new String[]{"rates"}));
    }
}