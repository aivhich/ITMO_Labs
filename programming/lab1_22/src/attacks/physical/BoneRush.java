package attacks.physical;

import ru.ifmo.se.pokemon.*;

public class BoneRush extends PhysicalMove {
    public BoneRush() {
        super(Type.GROUND, 25, 90);
    }
    @Override
    protected void applyOppDamage(Pokemon pokemon, double v) {
        super.applyOppDamage(pokemon, v);
        double p = Math.random();
        Effect e = new Effect();
        if(p<=0.375){
            e.turns(2).stat(Stat.ATTACK, 50);
        } else if (p<=0.375*2) {
            e.turns(3).stat(Stat.ATTACK, 75);
        } else if (p<=0.125+0.375*2) {
            e.turns(4).stat(Stat.ATTACK, 100);
        }else{
            e.turns(5).stat(Stat.ATTACK, 125);
        }
        pokemon.addEffect(e);
    }
    @Override
    protected String describe() {
        return "«Bone Rush» íàíîñèò 2–5 óäàðîâ çà õîä";
    }
}
