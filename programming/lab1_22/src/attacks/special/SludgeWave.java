package attacks.special;

import ru.ifmo.se.pokemon.*;

public class SludgeWave extends SpecialMove {
    public SludgeWave() {
        super(Type.POISON, 95, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        super.applyOppEffects(pokemon);
        if (Math.random()<=0.1 && (!pokemon.hasType(Type.POISON) || !pokemon.hasType(Type.STEEL))){
            Effect.poison(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "«Волна слизи» наносит урон и с вероятностью 10% может отравить цель.\n" +
                "Покемоны ядовитого или стального типа, не могут быть отравлены";
    }

}
