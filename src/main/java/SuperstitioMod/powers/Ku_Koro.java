package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.utils.CardUtility;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 每次受到攻击伤害时，获得1随机状态牌。所有消耗牌会回到抽牌堆
 */
public class Ku_Koro extends AbstractPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(Ku_Koro.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Ku_Koro(final AbstractCreature owner) {
        this.name = Ku_Koro.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.BUFF;

        this.amount = -1;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.makeImgFilesPath_Power("default84");
        String path48 = SuperstitioModSetup.makeImgFilesPath_Power("default32");
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

    }


    @Override
    public void updateDescription() {
        this.description = String.format(Ku_Koro.powerStrings.DESCRIPTIONS[0]);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
            final AbstractCard card = CardUtility.getRandomCurseCard(true,true);
            this.addToBot(new MakeTempCardInHandAction(card, true));
        }

        return super.onAttacked(info, damageAmount);
    }
}
