package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.manager.CommandManager;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.List;

/**
 * Команда обновления элемента по идентификатору.
 * <p>
 * Обновляет существующее транспортное средство по ID с вводом новых данных.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class Update implements Command{
    private final ManagersLocator managersLocator;

    public Update(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        if(args.length!=1) throw new RuntimeException(Exceptions.INVALID_NUM_OF_ARGS);
        Client client = managersLocator.get(Client.class);
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);

        try {
            int id = Integer.parseInt(args[0]);
            List<Vehicle> vehicles = (List<Vehicle>) client.sendObject(new Request<>(CommandType.SHOW, null));
            Vehicle old = vehicles.stream().filter(vehicle -> vehicle.getId()==id).findFirst().get();


            ioManager.write(String.format("--- Updating element with id=%s ---", args[0]));
            ioManager.write(old.toString());

            Vehicle newveh =  (new VehicleFactory(ioManager)).updateVehicle(old);
            Response<?> r = client.sendObject(new Request<>(CommandType.UPDATE, newveh));

            ioManager.write("Successfully updated vehicle name: " + newveh.getId());
            return r.getResultCode();
        } catch (NumberFormatException e) {
            throw new RuntimeException(Exceptions.INVALID_ARGS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "update id {element} : update the value of the collection element whose id is equal to the given one";
    }
}
