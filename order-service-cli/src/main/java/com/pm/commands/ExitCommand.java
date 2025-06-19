package com.pm.commands;

import lombok.RequiredArgsConstructor;

/**
 * Command implementation for gracefully exiting the application.
 * This command executes a provided callback to handle the application shutdown.
 */
@RequiredArgsConstructor
public class ExitCommand implements Command {
    /** Callback to be executed when exiting the application */
    private final Runnable exitCallback;

    /**
     * Executes the exit command, displaying a goodbye message and running the exit callback.
     *
     * @param args Command arguments (not used for this command)
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Goodbye!");
        exitCallback.run();
    }
}