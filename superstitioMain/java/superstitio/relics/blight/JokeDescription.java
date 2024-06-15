package superstitio.relics.blight;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import superstitio.DataManager;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

public class JokeDescription extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight  {
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

//    @Override
//    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
//        return super.onAttackToChangeDamage(info, damageAmount);
//    }
//
//    @Override
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        return super.onAttackedToChangeDamage(info, damageAmount);
//    }

    @Override
    public void obtain() {
        InfoBlight.obtain(this);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        InfoBlight.instanceObtain(this, callOnEquip);
    }

    @Override
    public void instantObtain() {
        InfoBlight.instanceObtain(this, true);
    }
}