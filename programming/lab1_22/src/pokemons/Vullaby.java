package pokemons;

import attacks.physical.Facade;
import attacks.physical.FuryAttack;
import attacks.special.ShadowBall;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Vullaby extends Pokemon {
    Facade facade;
    public Vullaby(String name, int level){
        super(name, level);
        setStats(70, 55, 75, 45, 65, 60);
        setType(Type.DARK, Type.FLYING);
        setMove(new Facade(), new FuryAttack(), new ShadowBall());
    }
}
