package superstitio.cards.lupa.SkillCard.cardManipulation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction;

public class NakedToSchool extends LupaCard {
    public static final String ID = DataManager.MakeTextID(NakedToSchool.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

    public NakedToSchool() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        new ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.discardPile, card ->
        {
            float targetX = MathUtils.random((float) Settings.WIDTH * 0.1F, (float) Settings.WIDTH * 0.9F);
            float targetY = MathUtils.random((float) Settings.HEIGHT * 0.8F, (float) Settings.HEIGHT * 0.2F);
            addToBot(new UpgradeSpecificCardAction(card));
            AutoDoneInstantAction.addToBotAbstract(() -> {
                AbstractDungeon.player.discardPile.removeCard(card);
                AbstractDungeon.effectList.add(new UpgradeShineEffect(targetX, targetY));
                ShowCardAndAddToDrawPileEffect toDrawPileEffect = new ShowCardAndAddToDrawPileEffect(card, targetX, targetY, true);
                AbstractCard fakeCard = ReflectionHacks.getPrivate(toDrawPileEffect, ShowCardAndAddToDrawPileEffect.class, "card");
                fakeCard.current_x = Gdx.graphics.getWidth();
                AbstractDungeon.effectList.add(toDrawPileEffect);
            });
        })
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], this.magicNumber))
                .setAnyNumber(true)
                .setChoseAmount(this.magicNumber)
                .addToBot();
    }

    @Override
    public void upgradeAuto() {
    }
}
