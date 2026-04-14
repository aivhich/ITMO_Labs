package pokemons;

import attacks.physical.Bulldoze;
import attacks.physical.FlameCharge;
import attacks.special.SludgeWave;
import attacks.special.SteamEruption;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class Volcanion extends Pokemon {
    public Volcanion(String name, int level){
        super(name, level);
        setStats(80, 110, 120, 130, 90, 70);
        setType(Type.FIRE, Type.WATER);
        setMove(new SludgeWave(), new Bulldoze(), new FlameCharge(), new SteamEruption());
    }
}
