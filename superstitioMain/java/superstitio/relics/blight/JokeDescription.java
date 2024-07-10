package superstitio.relics.blight;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import superstitio.DataManager;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

@AutoAdd.Seen
public class JokeDescription extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight {
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
    public void obtain() {
        AInfoBlightOnOrgasm.obtain(this);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        AInfoBlightOnOrgasm.instanceObtain(this, callOnEquip);
    }

    @Override
    public void instantObtain() {
        AInfoBlightOnOrgasm.instanceObtain(this, true);
    }
}