package superstitio.powers.masoOnly;

import superstitio.DataManager;

import static superstitio.cards.general.FuckJob_Card.BodyPart;

public class LostBodyPart_Arm extends LostBodyPart {
    public static final String POWER_ID = DataManager.MakeTextID(LostBodyPart_Arm.class);

    public LostBodyPart_Arm() {
        super(POWER_ID);
    }

    @Override
    public BodyPart[] banedBodyPart() {
        return new BodyPart[]{BodyPart.hand};
    }
}
