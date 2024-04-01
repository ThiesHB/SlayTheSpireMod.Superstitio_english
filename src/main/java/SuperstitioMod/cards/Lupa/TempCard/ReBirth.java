package SuperstitioMod.cards.Lupa.TempCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class ReBirth extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(ReBirth.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public ArrayList<AbstractPower> sealPower = new ArrayList<>();
    public AbstractMonster sealMonster = null;

    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public ReBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, CardColor.COLORLESS, "default");
        this.exhaust = true;
        this.setupBlock(BLOCK);
    }

    public ReBirth(ArrayList<AbstractPower> sealPower, AbstractMonster sealMonster) {
        this();
        this.sealPower = sealPower;
        if (sealMonster != null) {
            this.sealMonster = sealMonster;
            this.name = this.originalName + ": " + sealMonster.name;
        }
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.gainBlock();
        sealPower.forEach(power -> {
            addToBot(new ApplyPowerAction(player, sealMonster == null ? player : sealMonster, power));
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        ReBirth newCard = (ReBirth)super.makeCopy();
        if (newCard != null)
        {
            newCard.sealMonster = this.sealMonster;
            newCard.sealPower = this.sealPower;
            return newCard;
        }
        else
            return super.makeCopy();
    }
}
