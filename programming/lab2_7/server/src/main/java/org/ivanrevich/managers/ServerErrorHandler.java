package org.ivanrevich.managers;

import org.ivanrevich.exceptions.ErrorHandler;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.ivanrevich.ManagersLocator;

public class ServerErrorHandler extends ErrorHandler {

    public ServerErrorHandler(ManagersLocator managersLocator,
                              IOManager currentIo,
                              boolean isRawMode) {
        super(
                currentIo::write,
                () -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    managersLocator.register(IOManager.class, stack.current());
                },

                () -> {
                    IOManagerStack stack = managersLocator.get(IOManagerStack.class);
                    stack.pop();
                    currentIo.write("");  // пустая строка для форматирования
                    managersLocator.register(IOManager.class, stack.current());
                    currentIo.write("Script execution error.");
                },
                () -> {
                    if (isRawMode) {
                        currentIo.write("Command cancelled");
                    }
                },
                () -> {
                    if (isRawMode) {
                        currentIo.write("Command cancelled (soft)");
                    }
                },
                null
        );
    }
}