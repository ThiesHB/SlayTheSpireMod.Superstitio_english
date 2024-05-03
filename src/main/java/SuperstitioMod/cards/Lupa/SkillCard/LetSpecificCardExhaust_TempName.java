package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.actions.ChoseCardFromGridSelectWindowAction;
import SuperstitioMod.actions.ChoseCardFromHandCardSelectScreen;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LetSpecificCardExhaust_TempName extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(LetSpecificCardExhaust_TempName.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGICNumber = 1;
    public LetSpecificCardExhaust_TempName() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
        this.setupBlock(BLOCK);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ChoseCardFromHandCardSelectScreen(
                AbstractDungeon.player,
                String.format(cardStrings.getEXTENDED_DESCRIPTION()[0], this.magicNumber),
                this.magicNumber,
                this::letSpecificCardExhaust));
    }

    public AbstractGameAction letSpecificCardExhaust(AbstractCard card){
        return AutoDoneAction.newAutoDone(()-> CardModifierManager.addModifier(card,new ExhaustMod()));
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBlock(UPGRADE_BLOCK);
        this.upgradeBaseCost(UPGRADE_COST);
    }
}
