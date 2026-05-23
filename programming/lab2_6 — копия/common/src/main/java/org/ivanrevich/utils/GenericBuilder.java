package org.ivanrevich.utils;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Универсальный билдер для создания объектов.
 * <p>
 * Реализует паттерн Builder с использованием функциональных интерфейсов.
 * Позволяет цепочечное установление полей объекта.
 * </p>
 *
 * @param <T> тип создаваемого объекта
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public class GenericBuilder<T> {
    private Supplier<T> supplier;


    private GenericBuilder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public GenericBuilder() {
    }

    public static <T> GenericBuilder<T> of(Supplier<T> supplier) {
        return new GenericBuilder<>(supplier);
    }

    public <P> GenericBuilder<T> with(BiConsumer<T, P> consumer, P value) {
        return new GenericBuilder<>(() -> {
            T object = supplier.get();
            consumer.accept(object, value);
            return object;
        });
    }

    public T build() {
        return supplier.get();
    }
}
