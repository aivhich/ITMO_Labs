package pokemons;

import attacks.physical.Facade;
import attacks.physical.Slam;
import attacks.status_changers.DoubleTeam;
import attacks.status_changers.Swagger;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Raichu extends Pikachu {
    public Raichu(String name, int level){
        super(name, level);
        setStats(60, 90, 55, 90, 80, 110);
        setType(Type.ELECTRIC);
        addMove(new Facade());
    }
}
