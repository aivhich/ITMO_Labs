package attacks.status_changers;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
        pokemon.setMod(Stat.EVASION, 1);
    }

    @Override
    protected String describe() {
        return "ЂDouble Teamї повышает показатель Ђ”клончивостиї пользовател€ на одну ступень, из-за чего по нему сложнее попасть.";
    }
}
