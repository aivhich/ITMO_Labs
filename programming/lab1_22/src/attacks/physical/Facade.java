package attacks.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Type;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon pokemon, double v) {
        if(pokemon.getCondition() == Status.BURN || pokemon.getCondition() == Status.POISON || pokemon.getCondition() == Status.PARALYZE) {
            super.applyOppDamage(pokemon, v*2);
        }else{
            super.applyOppDamage(pokemon, v);
        }
    }

    @Override
    protected String describe() {
        return "Фасад наносит урон и бьет с удвоенной силой (140), если пользователь обожжен, отравлен или парализован.";
    }
}
