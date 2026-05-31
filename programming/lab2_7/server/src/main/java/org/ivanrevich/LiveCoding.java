package org.ivanrevich;

import java.util.List;

record User(String name, int age, String city) {}



public class LiveCoding {

    public static void main(String[] args) {
        List<User> users = List.of(
                new User("Анна", 22, "Москва"),
                new User("Иван", 17, "Казань"),
                new User("Олег", 31, "Москва"),
                new User("Мария", 19, "СПб"),
                new User("Петр", 45, "Казань"),
                new User("Елена", 28, "СПб"),
                new User("Артем", 16, "Москва")
        );
        users.stream()
                .filter(user -> user.age()>=18)
                .map(item -> item.name())
                .forEach(System.out::println);
    }


}
