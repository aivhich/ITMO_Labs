package org.ivanrevich.utils;


/**
 * Конфигуратор терминала.
 * <p>
 * Управляет raw-режимом терминала для улучшенного ввода.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public class TerminalConfigurator {
    public static boolean enableRawMode(){
        try{
            Runtime.getRuntime()
                    .exec(new String[]{"sh","-c","stty raw -echo < /dev/tty"})
                    .waitFor();
            return true;
        } catch (Exception e){
            System.out.println("Cannot enable raw mode");
            return false;
        }
    }

    public static void disableRawMode(){
        try{
            Runtime.getRuntime()
                    .exec(new String[]{"sh","-c","stty sane < /dev/tty"})
                    .waitFor();
        } catch (Exception e){
            System.out.println("Cannot restore terminal.");
        }

    }
}
