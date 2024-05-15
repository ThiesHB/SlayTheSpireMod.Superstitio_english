package superstitio.relics.a_starter;

import basemod.AutoAdd;
import superstitio.DataManager;
import superstitio.relics.AbstractLupaRelic;

@AutoAdd.Seen
public class JokeDescription extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(JokeDescription.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public JokeDescription() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void updateDescriptionArgs() {
    }

}