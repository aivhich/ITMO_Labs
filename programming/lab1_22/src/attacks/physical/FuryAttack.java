package attacks.physical;

import ru.ifmo.se.pokemon.*;

public class FuryAttack extends PhysicalMove {
    public FuryAttack() {
        super(Type.NORMAL, 15, 85);
    }

    @Override
    protected void applyOppDamage(Pokemon pokemon, double v) {
        super.applyOppDamage(pokemon, v);
        double p = Math.random();
        Effect e = new Effect();
        if(p<=0.375){
            e.turns(2).stat(Stat.ATTACK, 30);
        } else if (p<=0.375*2) {
            e.turns(3).stat(Stat.ATTACK, 45);
        } else if (p<=0.125+0.375*2) {
            e.turns(4).stat(Stat.ATTACK, 60);
        }else{
            e.turns(5).stat(Stat.ATTACK, 75);
        }

        pokemon.addEffect(e);
    }

    @Override
    protected String describe() {
        return "јтака €рости наносит 2Ц5 ударов за ход.";
    }
}
