/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Ochi
 */
public class ThreadUtils {

    /**
     * Creates a Task out of a Callable and runs it in a Deamon-Thread.
     * This method accepts Lambda-Calls as argument.
     * NOTE: You cannot pass a void-Call as has no return value. Transform
     * the method to return Void instead and return null at the end of the 
     * method.
     *
     * @param task
     * @return
     */
    public static <T> void runAsTask(Callable<T> callable) {
        
        Task<T> task = new Task<T>() {

            @Override
            protected T call() throws Exception {
              return callable.call();
            }
        };
                
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        
        thread.start();
    }

}
