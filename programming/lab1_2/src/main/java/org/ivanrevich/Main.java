package org.ivanrevich;


import org.ivanrevich.pokemons.Volcanion;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Pokemon("Чужой", 1);
        Pokemon p2 = new Pokemon("Хищник", 1);
        Volcanion p3 = new Volcanion("exmaple", 3);
        b.addAlly(p1);
        b.addFoe(p2);
        b.go();
    }
}