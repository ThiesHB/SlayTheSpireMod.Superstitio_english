package superstitio.relics.a_starter.Lupa;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.AbstractLupaRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@AutoAdd.Seen
public class Sensitive extends AbstractLupaRelic {
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
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        int amount = 0;
        if (card.costForTurn >= 1)
            amount += card.costForTurn;
        if (card.costForTurn == -1)
            amount += card.energyOnUse;
        if (amount == 0) return;
        this.addToTop(new ApplyPowerAction(player, player, new SexualHeat(player, amount * SexualHeatRate)));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexualHeatRate);
    }
}