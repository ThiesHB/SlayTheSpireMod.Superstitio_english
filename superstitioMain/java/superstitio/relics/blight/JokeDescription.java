package superstitio.relics.blight;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.relics.BecomeBlight;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.relic.BlightWithRelic;

public class JokeDescription extends SuperstitioRelic implements BecomeBlight {
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

    @Override
    public BlightWithRelic makeNewBlightWithRelic() {
        return new BlightWithRelic_JokeDescription();
    }

    public static class BlightWithRelic_JokeDescription extends BlightWithRelic {

        public static final String ID = DataUtility.MakeTextID(BlightWithRelic_JokeDescription.class);

        public BlightWithRelic_JokeDescription() {
            super(ID);
        }
        @Override
        public AbstractRelic makeRelic() {
            return new JokeDescription();
        }
    }

//    @Override
//    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
//        return super.onAttackToChangeDamage(info, damageAmount);
//    }
//
//    @Override
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        return super.onAttackedToChangeDamage(info, damageAmount);
//    }
}