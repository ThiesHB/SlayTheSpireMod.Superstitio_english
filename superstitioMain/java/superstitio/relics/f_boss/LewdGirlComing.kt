package superstitio.relics.f_boss

import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.powers.AbstractSuperstitioPower
import superstitio.relics.SuperstitioRelic
import superstitio.relics.f_boss.LewdGirlComing.RatherThanDeath
import superstitioapi.powers.interfaces.OnMonsterDeath.OnMonsterDeathPower
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.setDescriptionArgs

class LewdGirlComing  //    private static final int DrawAmount = 1;
//    private static final int EjaculationMax = 2;
    : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND)
{
    //    @Override
    //    public AbstractCard.CardColor getRelicOwner() {
    //        return DataManager.SPTT_DATA.LupaEnums.LUPA_CARD;
    //    }
    override fun atBattleStart()
    {
        val monster = CreatureUtility.getRandomMonsterSafe()
        //        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
        this.addToTop(RelicAboveCreatureAction(monster, this))
        monster.addPower(RatherThanDeath(monster, DEATH_DOOR_AMOUNT))
        //        }
        AbstractDungeon.onModifyPower()
    }

    //    @Override
    //    public void onSpawnMonster(AbstractMonster monster) {
    //        monster.addPower(new RatherThanDeath(monster, DEATH_DOOR_AMOUNT));
    //        AbstractDungeon.onModifyPower();
    //    }
    override fun onEquip()
    {
        ++AbstractDungeon.player.energy.energyMaster
    }

    override fun onUnequip()
    {
        --AbstractDungeon.player.energy.energyMaster
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(DEATH_DOOR_AMOUNT)
    }

    class RatherThanDeath(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
        OnMonsterDeathPower
    {
        override fun ifStopOwnerDeathWhenOwnerIsMonster(): Boolean
        {
            this.flash()
            addToTop_AutoRemoveOne(this)
            addToTop(HealAction(this.owner, this.owner, DEATH_DOOR_AMOUNT))
            return true
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun onDeath()
        {
            this.flash()
            addToBot(HealAction(this.owner, this.owner, DEATH_DOOR_AMOUNT))
            addToBot_AutoRemoveOne(this)
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(RatherThanDeath::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(LewdGirlComing::class.java)
        const val DEATH_DOOR_AMOUNT: Int = 1

        // 遗物类型
        private val RELIC_TIER = RelicTier.BOSS

        // 点击音效
        private val LANDING_SOUND = LandingSound.MAGICAL
    }
}