package pokemons;

import attacks.physical.BoneRush;
import attacks.physical.Facade;
import attacks.physical.FuryAttack;
import attacks.special.ShadowBall;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Mandibuzz extends Vullaby{
    public Mandibuzz(String name, int level){
        super(name, level);
        setStats(110, 65, 105, 55, 95, 80);
        setType(Type.DARK, Type.FLYING);
        addMove(new BoneRush());
    }
}
