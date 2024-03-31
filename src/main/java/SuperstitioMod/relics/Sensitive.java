package SuperstitioMod.relics;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.SexualHeat;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Sensitive extends CustomRelic {
    public static final String ID = SuperstitioModSetup.MakeTextID(Sensitive.class.getSimpleName() + "Relic");
    private static final String IMG_PATH = SuperstitioModSetup.getImgFilesPath() + "relics/default_relic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexualHeatRate = 1;

    public Sensitive() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        int amount = 0;
        if (card.costForTurn >= 1)
            amount = card.costForTurn;
        if (card.costForTurn == -1)
            amount = AbstractDungeon.player.energy.energy;
        if (amount != 0)
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SexualHeat(AbstractDungeon.player, amount)));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], SexualHeatRate);
    }
}