package SuperstitioMod.cards.Lupa.TempCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_TempCard;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class ReBirth extends AbstractLupaCard_TempCard {
    public static final String ID = DataManager.MakeTextID(ReBirth.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    public ArrayList<AbstractPower> sealPower = new ArrayList<>();
    public AbstractMonster sealMonster = null;

    public ReBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
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
        //this.gainBlock();
        this.addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, this.block));
        for (AbstractPower power : sealPower) {
            addToBot(new ApplyPowerAction(player, sealMonster == null ? player : sealMonster, power));
        }
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBlock(UPGRADE_PLUS_BLOCK);
    }

    @Override
    public AbstractCard makeCopy() {
        ReBirth newCard = (ReBirth) super.makeCopy();
        if (newCard != null) {
            newCard.sealMonster = this.sealMonster;
            newCard.sealPower = this.sealPower;
            return newCard;
        }
        else
            return super.makeCopy();
    }
}
