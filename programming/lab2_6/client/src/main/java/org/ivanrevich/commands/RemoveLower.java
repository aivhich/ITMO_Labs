package org.ivanrevich.commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.factory.VehicleFactory;
import org.ivanrevich.manager.IOManager;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.responses.Result;
import org.ivanrevich.utils.ResultCode;

import java.io.IOException;
import java.util.Iterator;

/**
 * Команда удаления элементов, меньших заданного.
 * <p>
 * Запрашивает эталонное транспортное средство и удаляет все элементы,
 * которые меньше его согласно {@link Vehicle#compareTo(Vehicle)}.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Command
 * @see Vehicle
 */
public class RemoveLower implements Command{
    private final  ManagersLocator managersLocator;

    public RemoveLower(ManagersLocator managersLocator) {
        this.managersLocator = managersLocator;
    }

    @Override
    public ResultCode run(String[] args) {
        IOManager io = managersLocator.get(IOManager.class);
        Client client = managersLocator.get(Client.class);
        io.write("Enter the reference vehicle to remove all lower elements:");
        VehicleFactory factory = new VehicleFactory(io);
        Vehicle reference;

        try {
            reference = factory.createVehicleForRef();
            Response r = client.sendObject(new Request<>(CommandType.REMOVE_LOWER, reference));
            int removedCount = (int) r.getBody();
            io.write("Removed " + removedCount + " vehicles that were lower than the reference.");
            return r.getResultCode();
        } catch (IllegalArgumentException | IOException | ClassNotFoundException e) {
            io.write("Invalid input, operation aborted.");
            return ResultCode.FAIL_0;
        }
    }

    @Override
    public String toString() {
        return "remove_lower {element} : Remove all elements from the collection that are less than the given element";
    }
}
