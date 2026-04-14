package org.ivanrevich;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class DopMain {
    public static void main(String[] args) {
        /*
        - Дан `Map<String, Integer>` (имя → баллы). Выведите пары, отсортированные по баллам убыванию (при равенстве — по ключу).
        */
        Map<String, Integer> map = Map.of(
                "Иван Иванов", 100,
                "Иван Петров", 252,
                "Петр Петров", 456,
                "Евгений Попов", 130,
                "Николай Николаев", 120,
                "Александр Александров", 100,
                "Никита Никитин", 1345,
                "Анна Романова", 1435,
                "Роман Зайцев", 100,
                "Алиса Алисова", 200
                );


        map.entrySet().stream()
                .sorted(
                        Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .forEach(entry -> System.out.println(entry.getKey() + "," + entry.getValue()));

    }
}
