package attacks.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class Slam extends PhysicalMove {
    public Slam() {
        super(Type.NORMAL, 80, 75);
    }

    @Override
    protected String describe() {
        return "Удар наносит урон без дополнительного эффекта.";
    }
}
