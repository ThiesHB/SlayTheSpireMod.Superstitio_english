package superstitio.powers.masoOnly;

import superstitio.DataManager;

import static superstitio.cards.general.FuckJob_Card.BodyPart;

//去势
public class LostBodyPart_Castration extends LostBodyPart {
    public static final String POWER_ID = DataManager.MakeTextID(LostBodyPart_Castration.class);

    public LostBodyPart_Castration() {
        super(POWER_ID);
    }

    @Override
    public BodyPart[] banedBodyPart() {
        return new BodyPart[]{BodyPart.genital};
    }
}
