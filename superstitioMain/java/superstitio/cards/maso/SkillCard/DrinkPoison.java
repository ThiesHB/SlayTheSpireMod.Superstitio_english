package superstitio.cards.maso.SkillCard;

import basemod.cardmods.ExhaustMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.powers.SexualHeat;
import superstitioapi.utils.ActionUtility;

import static superstitio.delayHpLose.DelayHpLosePower.findAll;

public class DrinkPoison extends MasoCard {
    public static final String ID = DataManager.MakeTextID(DrinkPoison.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 0;


    public DrinkPoison() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int totalDelayHpLose = findAll(AbstractDungeon.player, DelayHpLosePower.class)
                .mapToInt(power -> power.amount).sum();
        DelayHpLosePower.addToBot_removePower(totalDelayHpLose / 2, AbstractDungeon.player, true);
        if (totalDelayHpLose / 4 <= 0) return;
        addToBot_applyPower(new PoisonPower(AbstractDungeon.player, AbstractDungeon.player,
                totalDelayHpLose / 4));
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        if (SexualHeat.Orgasm.isPlayerInOrgasm()) {
            this.glowColor = Color.PINK.cpy();
        }
    }

    @Override
    public void initializeDescription() {
        if (!ActionUtility.isNotInBattle()) {
            int totalDelayHpLose = findAll(AbstractDungeon.player, DelayHpLosePower.class)
                    .mapToInt(power -> power.amount).sum();
            this.magicNumber = totalDelayHpLose / 2;
            this.baseMagicNumber = totalDelayHpLose / 2;
        }
        super.initializeDescription();
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

