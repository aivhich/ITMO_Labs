package org.ivanrevich.managers;

import org.ivanrevich.exceptions.ErrorHandler;
import org.ivanrevich.ManagersLocator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientErrorHandler extends ErrorHandler {

    public ClientErrorHandler(ManagersLocator managersLocator,
                              IOManager currentIo,
                              AtomicBoolean workModeFlag) {
        super(currentIo::write,
                () -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    managersLocator.register(IOManager.class, stack.current());
                },
                () -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    managersLocator.register(IOManager.class, stack.current());
                    currentIo.write("Script execution error.");
                },
                () -> workModeFlag.set(false),

                null,
                ()->{
                    currentIo.write("You're not authorized for run commands - login or signup. See 'help'");
                }
        );
    }
}