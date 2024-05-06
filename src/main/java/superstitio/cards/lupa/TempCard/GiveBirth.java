package superstitio.cards.lupa.TempCard;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard_TempCard;
import superstitio.cards.modifiers.block.PregnantBlock;

import java.util.ArrayList;

public class GiveBirth extends AbstractLupaCard_TempCard {
    public static final String ID = DataManager.MakeTextID(GiveBirth.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    public ArrayList<AbstractPower> sealPower = new ArrayList<>();
    public AbstractCreature sealMonster = null;

    public GiveBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.exhaust = true;
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK);
    }

    public GiveBirth(ArrayList<AbstractPower> sealPower, AbstractCreature sealMonster) {
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
        for (BlockInstance blockInstance : BlockModifierManager.blockInstances(AbstractDungeon.player)) {
            if (blockInstance.getBlockTypes().stream().anyMatch(blockModifier -> blockModifier instanceof PregnantBlock)) {
                AutoDoneInstantAction.addToBotAbstract(() -> BlockModifierManager.removeSpecificBlockType(blockInstance));
            }
        }
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public AbstractCard makeCopy() {
        GiveBirth newCard = (GiveBirth) super.makeCopy();
        if (newCard != null) {
            newCard.sealMonster = this.sealMonster;
            newCard.sealPower = this.sealPower;
            return newCard;
        } else
            return super.makeCopy();
    }
}
