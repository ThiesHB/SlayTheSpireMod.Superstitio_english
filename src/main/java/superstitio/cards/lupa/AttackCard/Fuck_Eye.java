package superstitio.cards.lupa.AttackCard;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.cards.lupa.TempCard.Fuck_Ear;
import superstitio.cards.modifiers.block.SexBlock;
import superstitio.utils.ActionUtility;

public class Fuck_Eye extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Fuck_Eye.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 1;
    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 1;

    public Fuck_Eye() {
        this(false);
//        AbstractCard card = new Fuck_Ear(false);
//        CardModifierManager.removeSpecificModifier(card, new RetainMod(), false);
        this.cardsToPreview = new Fuck_Ear(false);
    }

    public Fuck_Eye(boolean blank) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
        this.exhaust = true;
//        CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_gainBlock();
        ActionUtility.addToBot_makeTempCardInBattle(new Fuck_Ear(), BattleCardPlace.Hand, this.upgraded);
        addToBot_applyPower(new WeakPower(AbstractDungeon.player, 1, false));
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Inside(this.name);
    }

    @Override
    public void triggerWhenDrawn() {
        if (!CardModifierManager.hasModifier(this, RetainMod.ID))
            CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
