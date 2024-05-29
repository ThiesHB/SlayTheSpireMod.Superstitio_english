package superstitio.cards.lupa.SkillCard.block;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.lupa.LupaCard;
import superstitio.delayHpLose.PreventHpLimit_Turns;

public class NudeLive extends LupaCard {
    public static final String ID = DataManager.MakeTextID(NudeLive.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 16;
    private static final int UPGRADE_BLOCK = 6;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 0;

    public NudeLive() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new PreventHpLimit_Turns(AbstractDungeon.player, this.magicNumber));
        addToBot_gainBlock();
    }

    @Override
    public void upgradeAuto() {
    }
}

