package com.csse433.blackboard.common;

import java.util.function.Consumer;

/**
 * @author chetzhang
 */
public class Command {



    public Command(Runnable runnable) {
        runnable.run();
    }
}
