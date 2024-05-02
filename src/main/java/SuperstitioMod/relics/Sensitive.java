package SuperstitioMod.relics;

import SuperstitioMod.DataManager;
import SuperstitioMod.powers.SexualHeat;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Sensitive extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(Sensitive.class.getSimpleName() + "Relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexualHeatRate = 2;

    public Sensitive() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        int amount = 0;
        if (card.costForTurn >= 1)
            amount += card.costForTurn;
        if (card.costForTurn == -1)
            amount += AbstractDungeon.player.energy.energy;
        //if (amount != 0)
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SexualHeat(AbstractDungeon.player, amount * SexualHeatRate)));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexualHeatRate);
    }
}