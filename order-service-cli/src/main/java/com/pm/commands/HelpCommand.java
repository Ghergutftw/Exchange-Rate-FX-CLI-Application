package com.pm.commands;

/**
 * Command implementation for displaying help information about available commands.
 * This command shows usage instructions and examples for all supported commands.
 */
public class HelpCommand implements Command {
    /**
     * Executes the help command, displaying usage information for all available commands.
     *
     * @param args Command arguments (not used for this command)
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Available commands:");
        System.out.println("new [buy|sell] <investment ccy> <counter ccy> <limit> <validity>");
        System.out.println("Example: new buy EUR SEK 1.14 31.12.2025");
        System.out.println("cancel <ID>");
        System.out.println("Example: cancel 5");
        System.out.println("rates - Show current exchange rates");
        System.out.println("orders - Show all orders sorted by currency pair and distance to market rate");
        System.out.println("summary - Show order summary grouped by currency and type");
        System.out.println("help - Show this help message");
        System.out.println("exit - Exit the application");
    }
}