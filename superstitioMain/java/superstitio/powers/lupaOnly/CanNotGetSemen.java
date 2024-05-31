package superstitio.powers.lupaOnly;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.powers.AbstractSuperstitioPower;
import superstitioapi.powers.AllCardCostModifier_PerEnergy;

@SuperstitioImg.NoNeedImg
public class CanNotGetSemen extends AbstractSuperstitioPower implements OnAddSemenPower {
    public static final String POWER_ID = DataManager.MakeTextID(AllCardCostModifier_PerEnergy.class);

    public CanNotGetSemen() {
        super(POWER_ID, AbstractDungeon.player, -1);
    }

    @Override
    public void updateDescriptionArgs() {

    }

    @Override
    public boolean onAddSemen_shouldApply(AbstractPower power) {
        return false;
    }
}
