package superstitio.powers.masoOnly;

import superstitio.DataManager;

import static superstitio.cards.general.FuckJob_Card.BodyPart;

public class LostBodyPart_Leg extends LostBodyPart {
    public static final String POWER_ID = DataManager.MakeTextID(LostBodyPart_Leg.class);

    public LostBodyPart_Leg() {
        super(POWER_ID);
    }

    @Override
    public BodyPart[] banedBodyPart() {
        return new BodyPart[]{BodyPart.leg};
    }
}
