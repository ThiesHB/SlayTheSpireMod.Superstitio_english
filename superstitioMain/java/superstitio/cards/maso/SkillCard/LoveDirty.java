package superstitio.cards.maso.SkillCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;

//恋污，被消耗或丢弃时触发
public class LoveDirty extends MasoCard {
    public static final String ID = DataManager.MakeTextID(LoveDirty.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    //    private static final int MAGIC = 8;
    private static final int BLOCK = 11;
    private static final int UPGRADE_BLOCK = 4;

    public LoveDirty() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
//        this.setupMagicNumber(MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new DelayRemoveDelayHpLoseBlock());
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
    }

    @Override
    public void triggerOnManualDiscard() {
        Trigger();
    }

    @Override
    public void triggerOnExhaust() {
        Trigger();
    }

    private void Trigger() {
        addToBot_gainBlock();
    }

    @Override
    public void upgradeAuto() {
    }
}
