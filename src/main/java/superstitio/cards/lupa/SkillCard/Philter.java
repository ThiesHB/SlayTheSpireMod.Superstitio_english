package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.SexPlateArmorPower;

//春药
public class Philter extends LupaCard {
    public static final String ID = DataManager.MakeTextID(Philter.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = -1;


    public Philter() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new SexPlateArmorPower(AbstractDungeon.player, AbstractDungeon.player.currentBlock / this.magicNumber));
        addToBot(new RemoveAllBlockAction(AbstractDungeon.player, AbstractDungeon.player));
    }
}
