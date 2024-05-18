package superstitio.cards.maso.SkillCard;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.DelayHpLosePower_ApplyEachTurn;
import superstitio.utils.CardUtility;

public class DrinkPoison extends MasoCard {
    public static final String ID = DataManager.MakeTextID(DrinkPoison.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;
    private static final int ExtraDrawNum = 1;


    private static final int HeatReduce = 6;


    public DrinkPoison() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
//        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int totalDelayHpLose = DelayHpLosePower_ApplyEachTurn.findAll(AbstractDungeon.player).mapToInt(power -> power.amount).sum();
        DelayHpLosePower_ApplyEachTurn.addToBot_removePower(totalDelayHpLose / 2, AbstractDungeon.player, AbstractDungeon.player, true);
        addToBot_applyPower(new PoisonPower(AbstractDungeon.player, AbstractDungeon.player,
                (int) Math.sqrt((double) totalDelayHpLose / 2) + 1));
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        if (InBattleDataManager.InOrgasm) {
            this.glowColor = Color.PINK.cpy();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        if (CardUtility.isNotInBattle()) return;
        int totalDelayHpLose = DelayHpLosePower_ApplyEachTurn.findAll(AbstractDungeon.player).mapToInt(power -> power.amount).sum();
        this.magicNumber = (int) Math.sqrt((double) totalDelayHpLose / 2) + 1;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void upgradeAuto() {
        CardModifierManager.addModifier(this, new RetainMod());
    }
}

