package pokemons;

import attacks.physical.Slam;
import attacks.status_changers.DoubleTeam;
import attacks.status_changers.Swagger;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Pikachu extends Pichu {
    public Pikachu(String name, int level){
        super(name, level);
        setStats(35, 55, 40, 50, 50, 90);
        setType(Type.ELECTRIC);
        addMove(new Slam());
    }
}
