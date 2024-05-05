package superstitio.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SexMarkOrb_Inside extends SexMarkOrb {
    private final static int AOEDamageRate = 2;


    public SexMarkOrb_Inside() {
        this("");
    }
    public SexMarkOrb_Inside(String sexMarkName) {
        super(sexMarkName);
        this.baseEvokeAmount = AOEDamageRate;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = AOEDamageRate;
        this.passiveAmount = this.basePassiveAmount;
    }

    @Override
    public int attack() {
        return this.evokeAmount;
    }

    @Override
    public int block() {
        return 0;
    }

    @Override
    public AbstractOrb makeCopy() {
        return new SexMarkOrb_Inside();
    }

}
