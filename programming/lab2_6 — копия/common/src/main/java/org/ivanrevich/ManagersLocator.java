package org.ivanrevich;

import java.util.HashMap;
import java.util.Map;

/**
 * Локатор сервисов (Service Locator паттерн).
 * <p>
 * Централизованное хранилище зависимостей для получения
 * менеджеров и других сервисов в приложении.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public class ManagersLocator {
    private final Map<Class<?>, Object> services = new HashMap<>();

    public <T> void register(Class<T> type, T instance) {
        services.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) services.get(type);
    }

}
