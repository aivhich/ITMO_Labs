package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.managers.CommandManager;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

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
        if(args.length!=1) return ResultCode.INVALID_NUM_OF_ARGS;
        Client client = managersLocator.get(Client.class);
        CommandManager commandManager = managersLocator.get(CommandManager.class);
        IOManager ioManager = managersLocator.get(IOManager.class);

        try {
            int id = Integer.parseInt(args[0]);
            PriorityQueue<Vehicle> vehicles = (PriorityQueue<Vehicle>) client.sendObject(new Request<>(CommandType.SHOW, null)).getBody();
            Optional<Vehicle> old = vehicles.stream().filter(vehicle -> vehicle.getId()==id).findFirst();
            if(old.isEmpty()){
                return ResultCode.ID_ISN_EXIST;
            }

            ioManager.write(String.format("--- Updating element with id=%s ---", args[0]));
            ioManager.write(old.get().toString());

            Vehicle newveh =  (new VehicleFactory(ioManager)).updateVehicle(old.get());
            Response<?> r = client.sendObject(new Request<>(CommandType.UPDATE, newveh));

            ioManager.write("Request to updated vehicle name has been sent: " + newveh.getId());
            return r.getResultCode();
        } catch (NumberFormatException e) {
            return ResultCode.INVALID_ARGS;
        } catch (ClassNotFoundException e) {
            return ResultCode.INVALID_REQUEST;
        } catch (IOException e) {
            return ResultCode.INTERNAL_CLI_ERROR;
        }
    }

    @Override
    public String toString() {
        return "update id {element} : update the value of the collection element whose id is equal to the given one";
    }
}
