package superstitio.powers.masoOnly;

import superstitio.DataManager;

import static superstitio.cards.general.FuckJob_Card.BodyPart;

public class LostBodyPart_Breast extends LostBodyPart{
    public static final String POWER_ID = DataManager.MakeTextID(LostBodyPart_Breast.class);

    public LostBodyPart_Breast() {
        super(POWER_ID);
    }
    @Override
    public BodyPart[] banedBodyPart() {
        return new BodyPart[]{BodyPart.breast};
    }
}
