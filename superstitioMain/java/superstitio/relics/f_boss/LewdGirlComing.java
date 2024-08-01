package superstitio.relics.f_boss;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.powers.interfaces.OnMonsterDeath;
import superstitioapi.utils.CreatureUtility;

public class LewdGirlComing extends SuperstitioRelic {
    public static final String ID = DataManager.MakeTextID(LewdGirlComing.class);
    public static final int DEATH_DOOR_AMOUNT = 1;
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;
//    private static final int DrawAmount = 1;
//    private static final int EjaculationMax = 2;

    public LewdGirlComing() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

//    @Override
//    public AbstractCard.CardColor getRelicOwner() {
//        return DataManager.SPTT_DATA.LupaEnums.LUPA_CARD;
//    }

    @Override
    public void atBattleStart() {
        AbstractMonster monster = CreatureUtility.getRandomMonsterSafe();
//        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
        this.addToTop(new RelicAboveCreatureAction(monster, this));
        monster.addPower(new RatherThanDeath(monster, DEATH_DOOR_AMOUNT));
//        }
        AbstractDungeon.onModifyPower();
    }

//    @Override
//    public void onSpawnMonster(AbstractMonster monster) {
//        monster.addPower(new RatherThanDeath(monster, DEATH_DOOR_AMOUNT));
//        AbstractDungeon.onModifyPower();
//    }

    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(DEATH_DOOR_AMOUNT);
    }

    public static class RatherThanDeath extends AbstractSuperstitioPower implements OnMonsterDeath.OnMonsterDeathPower {
        public static final String POWER_ID = DataManager.MakeTextID(RatherThanDeath.class);

        public RatherThanDeath(final AbstractCreature owner, int amount) {
            super(POWER_ID, owner, amount);
        }

        @Override
        public boolean ifStopOwnerDeathWhenOwnerIsMonster() {
            this.flash();
            addToTop_AutoRemoveOne(this);
            addToTop(new HealAction(this.owner, this.owner, DEATH_DOOR_AMOUNT));
            return true;
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        public void onDeath() {
            this.flash();
            addToBot(new HealAction(this.owner, this.owner, DEATH_DOOR_AMOUNT));
            addToBot_AutoRemoveOne(this);
        }
    }
}