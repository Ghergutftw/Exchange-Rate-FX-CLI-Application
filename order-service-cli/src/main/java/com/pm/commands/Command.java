package com.pm.commands;

/**
 * Interface representing a command in the FX OrderBook CLI application.
 * All commands must implement this interface to be executable within the application.
 */
public interface Command {
    /**
     * Executes the command with the given arguments.
     *
     * @param args The command arguments as an array of strings
     * @throws Exception If an error occurs during command execution
     */
    void execute(String[] args) throws Exception;
}