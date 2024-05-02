package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

//春药
public class Philter extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Philter.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MagicNumber = 2;
    private static final int COST_UPDATE = 1;


    public Philter() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPowerToPlayer(new PlatedArmorPower(AbstractDungeon.player, AbstractDungeon.player.currentBlock / this.magicNumber));
        addToBot(new RemoveAllBlockAction(AbstractDungeon.player, AbstractDungeon.player));
    }

    @Override
    public void upgradeAuto() {
//            upgradeMagicNumber(UPGRADE_MagicNumber);
        upgradeBaseCost(COST_UPDATE);
    }
}
