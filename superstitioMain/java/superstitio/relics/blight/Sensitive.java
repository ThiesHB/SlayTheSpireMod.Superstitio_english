package superstitio.relics.blight;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.relicToBlight.InfoBlight;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class Sensitive extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight  {
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
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (card.isInAutoplay) return;
        int amount = 0;
        if (card.costForTurn >= 1)
            amount += card.costForTurn;
        if (card.costForTurn == -1)
            amount += card.energyOnUse;
        if (amount == 0) return;
        SexualHeat.addAction_addSexualHeat(player, amount * SexualHeatRate, AutoDoneInstantAction::addToTopAbstract);
    }

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