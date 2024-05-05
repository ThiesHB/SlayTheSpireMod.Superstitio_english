package superstitio.orbs;

import com.megacrit.cardcrawl.orbs.Lightning;

public abstract class SexMarkOrb extends Lightning {
//    public SexMarkOrb(String ID, String NAME, int basePassiveAmount, int baseEvokeAmount, String passiveDescription, String evokeDescription,
//    String imgPath) {
//        super(ID, NAME, basePassiveAmount, baseEvokeAmount, passiveDescription, evokeDescription, imgPath);
//    }

    public String sexMarkName;

    public SexMarkOrb(String sexMarkName) {
        super();
        this.sexMarkName = sexMarkName;
    }

    @Override
    public void onEvoke() {
    }

    @Override
    public void onEndOfTurn() {
    }

    public abstract int attack();

    public abstract int block();
}
