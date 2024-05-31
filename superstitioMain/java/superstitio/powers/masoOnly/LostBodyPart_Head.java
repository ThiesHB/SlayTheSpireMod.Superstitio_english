package superstitio.powers.masoOnly;

import superstitio.DataManager;

import static superstitio.cards.general.FuckJob_Card.BodyPart;

public class LostBodyPart_Head extends LostBodyPart {
    public static final String POWER_ID = DataManager.MakeTextID(LostBodyPart_Head.class);

    public LostBodyPart_Head() {
        super(POWER_ID);
    }


    @Override
    public BodyPart[] banedBodyPart() {
        return new BodyPart[]{BodyPart.head, BodyPart.mouth};
    }
}
