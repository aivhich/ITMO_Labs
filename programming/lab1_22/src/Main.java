import pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Mandibuzz p11 = new Mandibuzz("Mandibuzz Team 1", 10);
        Pichu p12 = new Pichu("Pichu Team 1", 3);
        Pikachu p13 = new Pikachu("Pikachu Team 1", 2);
        Raichu p14 = new Raichu("Raichu Team 1", 1);
        Volcanion p21 = new Volcanion("Volcanion Team 2", 7);
        Vullaby p22 = new Vullaby("Vullaby Team 2", 3);
        Mandibuzz p23 = new Mandibuzz("Mandibuzz Team 2", 2);
        Pichu p24 = new Pichu("Pichu Team 2", 1);
        Pikachu p25 = new Pikachu("Pikachu Team 2", 3);
        b.addAlly(p11);
        b.addAlly(p12);
        b.addAlly(p13);
        b.addAlly(p14);
        b.addFoe(p21);
        b.addFoe(p22);
        b.addFoe(p23);
        b.addFoe(p24);
        b.addFoe(p25);
        b.go();
    }
}