package com.github.domktorymysli.grenton.command;

import java.net.InetAddress;
import java.util.Random;

public final class CluFunctionCommand extends CluCommandBase implements CluCommand {

    private static final Random randomGenerator = new Random();

    public CluFunctionCommand(InetAddress ip, String functionName, String[] args) {

        this.sessionId = this.generateRandomSessionId();

        String arguments = "nil";

        if (args.length > 0) {
            arguments = String.join(",", args);
        }

        this.command = "req:" + ip.getHostAddress() + ":" + this.sessionId + ":" + functionName + "(" + arguments + ")";
    }
    
    public CluFunctionCommand(InetAddress ip, String function) {
        this.sessionId = this.generateRandomSessionId();

        this.command = "req:" + ip.getHostAddress() + ":" + this.sessionId + ":" + function;
    }

    private String generateRandomSessionId() {
        final StringBuilder randomSessionId = new StringBuilder(Integer.toHexString(randomGenerator.nextInt(65534)));

        while(randomSessionId.length() < 6) randomSessionId.append('0');

        return randomSessionId.toString();
    }
}
