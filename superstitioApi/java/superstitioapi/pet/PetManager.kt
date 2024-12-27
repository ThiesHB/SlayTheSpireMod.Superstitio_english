package superstitioapi.pet

import basemod.interfaces.PostPowerApplySubscriber
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.actions.IntentFlashAction
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.TipTracker
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.function.Consumer

class PetManager : RenderInBattle, AtEndOfPlayerTurnPreCardSubscriber, PostPowerApplySubscriber
{
    val monsterGroup: MinionGroup = MinionGroup(arrayOf())

    init
    {
        RenderInBattle.Register(RenderInBattle.RenderType.Normal, this)
    }

    fun addPet(monster: AbstractMonster)
    {
        monsterGroup.add(monster)
    }

    private fun monsterTurn(monster: AbstractMonster)
    {
        if (monster.isDeadOrEscaped && !monster.halfDead)
            return
        if (monster.intent != Intent.NONE)
        {
            AbstractDungeon.actionManager.addToBottom(ShowMoveNameAction(monster))
            AbstractDungeon.actionManager.addToBottom(IntentFlashAction(monster))
        }

        if (!TipTracker.tips["INTENT_TIP"]!! && AbstractDungeon.player.currentBlock == 0 && (monster.intent == Intent.ATTACK || monster.intent == Intent.ATTACK_DEBUFF || monster.intent == Intent.ATTACK_BUFF || monster.intent == Intent.ATTACK_DEFEND))
        {
            if (AbstractDungeon.floorNum <= 5)
            {
                ++TipTracker.blockCounter
            }
            else
            {
                TipTracker.neverShowAgain("INTENT_TIP")
            }
        }

        monster.takeTurn()
        monster.applyTurnPowers()
    }

    override fun receiveAtEndOfPlayerTurnPreCard()
    {
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier(monsterGroup::applyPreTurnLogic))
        monsterGroup.monsters.forEach(Consumer(this::monsterTurn))
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier(monsterGroup::applyEndOfTurnPowers))
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier(monsterGroup::showIntent), 2)
    }

    override fun render(sb: SpriteBatch)
    {
        monsterGroup.render(sb)
    }

    override fun update()
    {
        monsterGroup.update()
    }

    override fun updateAnimation()
    {
        monsterGroup.updateAnimations()
    }

    override fun receivePostPowerApplySubscriber(
        abstractPower: AbstractPower?,
        abstractCreature: AbstractCreature?,
        abstractCreature1: AbstractCreature?
    )
    {
        monsterGroup.monsters.forEach(Consumer(AbstractMonster::applyPowers))
    }

    companion object
    {
        fun calculateSmartDistance(m1: AbstractCreature, m2: AbstractCreature): Float
        {
            return (m1.hb_w + m2.hb_w) / 2.0f
        }

        fun spawnMonster(monsterInstance: AbstractMonster): AbstractMonster
        {
            if (InBattleDataManager.getPetManager() == null)
                return monsterInstance
            val roomMonsters: MonsterGroup =
                InBattleDataManager.getPetManager()!!.monsterGroup
            var monsterDX = Settings.WIDTH / 2.0f
            var monsterDY = AbstractDungeon.player.hb.y
            var lastMonster: AbstractMonster? = null
            if (roomMonsters.monsters.isNotEmpty())
            {
                lastMonster = roomMonsters.monsters[roomMonsters.monsters.size - 1]
                monsterDX = lastMonster.drawX
                monsterDY = lastMonster.drawY
            }
            if (lastMonster != null) monsterInstance.drawX =
                monsterDX - calculateSmartDistance(lastMonster, monsterInstance) * Settings.scale
            else monsterInstance.drawX = monsterDX - 200.0f * Settings.scale
            monsterInstance.drawY = monsterDY
            if (monsterInstance.drawX < 0.0f || monsterInstance.drawX > Gdx.graphics.width || monsterInstance.drawY < 0.0f || monsterInstance.drawY > Gdx.graphics.height)
            {
                monsterInstance.drawX = MathUtils.random(0.0f, Gdx.graphics.width.toFloat())
                monsterInstance.drawY = MathUtils.random(Gdx.graphics.height * 0.15f, Gdx.graphics.height * 0.85f)
            }
            monsterInstance.hb.move(monsterInstance.drawX, monsterInstance.drawY)
            monsterInstance.init()
            monsterInstance.applyPowers()
            monsterInstance.useUniversalPreBattleAction()
            monsterInstance.showHealthBar()
            monsterInstance.createIntent()
            monsterInstance.usePreBattleAction()
            roomMonsters.add(monsterInstance)
            return monsterInstance
        }

        fun spawnMinion(monsterClass: Class<out AbstractMonster>): AbstractMonster
        {
            val minionMonster =
                MinionMonster(CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass))
            return spawnMonster(minionMonster)
        }

        fun spawnMonster(monsterClass: Class<out AbstractMonster>): AbstractMonster
        {
            val monster =
                CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass)
            return spawnMonster(monster)
        }
    }
}
