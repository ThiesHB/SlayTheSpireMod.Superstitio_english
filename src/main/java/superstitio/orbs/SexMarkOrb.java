package superstitio.orbs;

public abstract class SexMarkOrb extends AbstractLupaOrb {

    public String sexMarkName;

    public SexMarkOrb(String id, int amount, String sexMarkName) {
        super(id, amount, amount, false);
        this.sexMarkName = sexMarkName;
        this.name = String.format(this.orbStringsSet.getNAME(), sexMarkName);
        this.updateDescription();
    }

    public SexMarkOrb setSexMarkName(String sexMarkName) {
        this.sexMarkName = sexMarkName;
        this.name = String.format(this.orbStringsSet.getNAME(), sexMarkName);
        this.updateDescription();
        return this;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.evokeAmount);
    }

    @Override
    public void onEvoke() {
    }

//    @Override
//    public void updateDescription() {
//        this.description = String.format(orbStringsSet.getDESCRIPTION()[0]);
//    }

    @Override
    public void onEndOfTurn() {
    }

    public abstract int attack();

    public abstract int block();
}
