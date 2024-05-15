package superstitio.cards.lupa.BaseCard;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.DrySemenBlock;
import superstitio.cards.lupa.LupaCard;

public class DrySemen extends LupaCard {
    public static final String ID = DataManager.MakeTextID(DrySemen.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 12;
    private static final int UPGRADE_BLOCK = 5;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 0;

    public DrySemen() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new DrySemenBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (hasEnoughSemen(this.magicNumber)) {
            useSemen(this.magicNumber);
            addToBot_gainBlock();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }
}

