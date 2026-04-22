package org.ivanrevich.commands;

import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.ManagersLocator;


/**
 * Команда вывода справки по доступным командам.
 * <p>
 * Выводит описание всех зарегистрированных команд в приложении.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see CommandManager
 */
public class Help implements Command{

    private final ManagersLocator managersLocator;

    public Help(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public Result run(String[] args) {
        IOManager ioManager = managersLocator.get(IOManager.class);
        CommandManager commandManager = managersLocator.get(CommandManager.class);

        ioManager.write("--- HELP ---");
        for(Command c: commandManager.getRegistedCommands()){
            ioManager.write(c.toString());
        }
//        ioManager.write("""
//                help : вывести справку по доступным командам
//                info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
//                show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
//                add {element} : добавить новый элемент в коллекцию
//                update id {element} : обновить значение элемента коллекции, id которого равен заданному
//                remove_by_id id : удалить элемент из коллекции по его id
//                clear : очистить коллекцию
//                save : сохранить коллекцию в файл
//                execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
//                exit : завершить программу (без сохранения в файл)
//                remove_head : вывести первый элемент коллекции и удалить его
//                remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный
//                history : вывести последние 13 команд (без их аргументов)
//                count_greater_than_fuel_type fuelType : вывести количество элементов, значение поля fuelType которых больше заданного
//                print_ascending : вывести элементы коллекции в порядке возрастания
//                print_unique_fuel_type : вывести уникальные значения поля fuelType всех элементов в коллекции
//                """);
        return Result.SUCCESS;
    }

    @Override
    public String toString() {
        return "help: display help on available commands";
    }
}
