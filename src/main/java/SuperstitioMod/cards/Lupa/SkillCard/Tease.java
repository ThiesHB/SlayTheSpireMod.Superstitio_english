package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.FrailPower;

public class Tease extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Tease.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int MagicNumber = 15;
    private static final int UPGRADE_MagicNumber = 5;

    public Tease() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNumber);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int a = (int) (monster.maxHealth * MagicNumber / 100f);
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                monster.decreaseMaxHealth(a);
            }
        });
        addToBot_applyPowerToEnemy(new FrailPower(monster, 1, false), monster);
        this.addToBot(new GainBlockAction(monster, a));
        addToBot_applyPowerToEnemy(new BarricadePower(monster), monster);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
