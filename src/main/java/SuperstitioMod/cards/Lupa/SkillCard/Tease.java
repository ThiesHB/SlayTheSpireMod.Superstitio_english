package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int amount = (int) (monster.maxHealth * MagicNumber / 100f);
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                monster.decreaseMaxHealth(amount);
            }
        });
        this.addToBot(new ApplyPowerAction(monster, player, new FrailPower(monster, 1, false)));
        this.addToBot(new GainBlockAction(monster, amount));
        this.addToBot(new ApplyPowerAction(monster, player, new BarricadePower(monster)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
