import com.pm.commands.ExitCommand;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ExitCommandTest {
    @Test
    void execute_ShouldCallExitCallback() {
          
        Runnable exitCallback = mock(Runnable.class);
        ExitCommand command = new ExitCommand(exitCallback);
        String[] args = {"exit"};

        command.execute(args);

        // Assert
        verify(exitCallback, times(1)).run();
    }
}