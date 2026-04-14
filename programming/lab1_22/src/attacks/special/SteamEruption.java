package attacks.special;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class SteamEruption extends SpecialMove {
    public SteamEruption() {
        super(Type.WATER, 110, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        super.applyOppEffects(pokemon);
        if (Math.random()<=0.3){
            Effect.burn(pokemon);
        }
    }
    @Override
    protected String describe() {
        return "Извержение пара наносит урон и с вероятностью 30% может поджечь цель.";
    }
}
