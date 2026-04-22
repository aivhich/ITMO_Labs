package org.ivanrevich.utils;

/**
 * Объект команды с именем и аргументами.
 * <p>
 * Используется для хранения распарсенных команд.
 * </p>
 *
 * @param name имя команды
 * @param args массив аргументов
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public record CommandObj(String name, String[] args) { }
