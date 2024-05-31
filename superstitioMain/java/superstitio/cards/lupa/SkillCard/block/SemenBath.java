package superstitio.cards.lupa.SkillCard.block;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;

import java.util.Optional;

public class SemenBath extends LupaCard {
    public static final String ID = DataManager.MakeTextID(SemenBath.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 1;

    public SemenBath() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        CardModifierManager.addModifier(this, new RetainMod());
    }

    private static Optional<AbstractPower> getPower(String powerID) {
        AbstractPower power = AbstractDungeon.player.getPower(powerID);
        if (power == null) return Optional.empty();
        return Optional.of(power);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        DelayHpLosePower.addToBot_removePower(this.block * getTotalSemenValue(), AbstractDungeon.player, true);
        getPower(FloorSemen.POWER_ID).ifPresent(this::addToBot_removeSpecificPower);
        getPower(OutsideSemen.POWER_ID).ifPresent(this::addToBot_removeSpecificPower);
        getPower(InsideSemen.POWER_ID).ifPresent(this::addToBot_removeSpecificPower);
    }

    @Override
    public void upgradeAuto() {
    }
}

