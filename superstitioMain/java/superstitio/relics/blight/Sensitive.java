package superstitio.relics.blight;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.BecomeBlight;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.relic.BlightWithRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class Sensitive extends SuperstitioRelic implements BecomeBlight {
    public static final String ID = DataManager.MakeTextID(Sensitive.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexualHeatRate = 2;
    private final int hpLose = 0;

    public Sensitive() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }



    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexualHeatRate);
    }

    @Override
    public BlightWithRelic makeNewBlightWithRelic() {
        return new BlightWithRelic_Sensitive();
    }

    public static class BlightWithRelic_Sensitive extends BlightWithRelic {

        public static final String ID = DataUtility.MakeTextID(BlightWithRelic_Sensitive.class);

        public BlightWithRelic_Sensitive() {
            super(ID);
        }

        @Override
        public AbstractRelic makeRelic() {
            return new Sensitive();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster monster) {
            int amount = 0;
            if (card.costForTurn >= 1)
                amount += card.costForTurn;
            if (card.costForTurn == -1)
                amount += card.energyOnUse;
            if (amount == 0) return;
            SexualHeat.addAction_addSexualHeat(player, amount * SexualHeatRate, AutoDoneInstantAction::addToTopAbstract);
        }
    }
}