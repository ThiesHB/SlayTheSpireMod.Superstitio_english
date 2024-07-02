package superstitio.cards.lupa.SkillCard.block;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.AbstractSuperstitioPower;

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

    @SuperstitioImg.NoNeedImg
    public static class SexPlateArmorPower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(SexPlateArmorPower.class);

        public SexPlateArmorPower(final AbstractCreature owner, int amount) {
            super(POWER_ID, owner, amount);
            this.loadRegion("platedarmor");
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        public void playApplyPowerSfx() {
            CardCrawlGame.sound.play("POWER_PLATED", 0.05F);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
                this.flash();
                addToBot_reducePowerToOwner(SexPlateArmorPower.POWER_ID, 1);
            }
        }

        @Override
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            this.flash();
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
        }
    }
}
