package superstitioapi

import basemod.BaseMod
import basemod.interfaces.*
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.player.PlayerInitPostDungeonInitialize
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.ToolBox

@SpireInitializer
class SuperstitioApiSubscriber : PostExhaustSubscriber, StartGameSubscriber, RelicGetSubscriber,
    PostPowerApplySubscriber, PostBattleSubscriber, PostDungeonInitializeSubscriber, OnStartBattleSubscriber,
    OnPlayerTurnStartSubscriber, OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber,
    PostEnergyRechargeSubscriber, PreMonsterTurnSubscriber
{
    //    public static boolean hasHadInMonsterTurn = false;
    init
    {
        BaseMod.subscribe(this)
        Logger.run("Done $this subscribing")
    }

    override fun receivePostExhaust(abstractCard: AbstractCard?)
    {
        InBattleDataManager.ApplyAll(
            { sub: PostExhaustSubscriber -> sub.receivePostExhaust(abstractCard) },
            PostExhaustSubscriber::class.java
        )
    }

    override fun receiveStartGame()
    {
        InBattleDataManager.ApplyAll(
            StartGameSubscriber::receiveStartGame,
            StartGameSubscriber::class.java
        )
    }

    override fun receiveCardUsed(abstractCard: AbstractCard?)
    {
        InBattleDataManager.ApplyAll(
            { sub: OnCardUseSubscriber -> sub.receiveCardUsed(abstractCard) },
            OnCardUseSubscriber::class.java
        )
    }

    override fun receivePowersModified()
    {
        InBattleDataManager.ApplyAll(
            OnPowersModifiedSubscriber::receivePowersModified,
            OnPowersModifiedSubscriber::class.java
        )
    }

    override fun receivePostBattle(abstractRoom: AbstractRoom?)
    {
        InBattleDataManager.ClearOnEndOfBattle()
        InBattleDataManager.ApplyAll(
            { sub: PostBattleSubscriber -> sub.receivePostBattle(abstractRoom) },
            PostBattleSubscriber::class.java
        )
    }

    override fun receivePostDraw(abstractCard: AbstractCard?)
    {
        InBattleDataManager.ApplyAll(
            { it.receivePostDraw(abstractCard) },
            PostDrawSubscriber::class.java
        )
    }

    override fun receivePostDungeonInitialize()
    {
        InBattleDataManager.ApplyAll(
            PostDungeonInitializeSubscriber::receivePostDungeonInitialize,
            PostDungeonInitializeSubscriber::class.java
        )
        if (AbstractDungeon.player is PlayerInitPostDungeonInitialize)
        {
            (AbstractDungeon.player as PlayerInitPostDungeonInitialize).initPostDungeonInitialize()
        }
    }

    override fun receivePostEnergyRecharge()
    {
        InBattleDataManager.ApplyAll(
            PostEnergyRechargeSubscriber::receivePostEnergyRecharge,
            PostEnergyRechargeSubscriber::class.java
        )
    }

    override fun receivePostPowerApplySubscriber(
        appliedPower: AbstractPower?, target: AbstractCreature?, source: AbstractCreature?
    )
    {
        if (target == null) return
        AutoDoneInstantAction.addToTopAbstract {
            if (appliedPower is OnPostApplyThisPower<*>)
                for (power: AbstractPower in target.powers)
                {
                    if (power is OnPostApplyThisPower<out AbstractPower>
                        && power.ID == appliedPower.ID
                    )
                        power.tryInitializePostApplyThisPower(appliedPower)
                }
        }
        InBattleDataManager.ApplyAll({ sub: PostPowerApplySubscriber ->
            sub.receivePostPowerApplySubscriber(
                appliedPower,
                source,
                source
            )
        }, PostPowerApplySubscriber::class.java)
    }

    override fun receiveRelicGet(abstractRelic: AbstractRelic?)
    {
        InBattleDataManager.ApplyAll(
            { sub: RelicGetSubscriber -> sub.receiveRelicGet(abstractRelic) },
            RelicGetSubscriber::class.java
        )
    }

    override fun receiveOnBattleStart(abstractRoom: AbstractRoom?)
    {
        InBattleDataManager.InitializeAtStartOfBattle()
        //        hasHadInMonsterTurn = false;
        InBattleDataManager.ApplyAll(
            { sub: OnStartBattleSubscriber -> sub.receiveOnBattleStart(abstractRoom) },
            OnStartBattleSubscriber::class.java
        )
    }

    override fun receiveOnPlayerTurnStart()
    {
        InBattleDataManager.InitializeAtStartOfTurn()
        //        hasHadInMonsterTurn = false;
        InBattleDataManager.ApplyAll(
            OnPlayerTurnStartSubscriber::receiveOnPlayerTurnStart,
            OnPlayerTurnStartSubscriber::class.java
        )
    }

//    override fun receiveOnPlayerDamaged(amount: Int, info: DamageInfo?): Int
//    {
//        var newAmount = amount
//        HangUpCardGroup.forEachHangUpCard(
//            CardOrb_OnAttackedToChangeDamage::class.java
//        ) { orb: CardOrb_OnAttackedToChangeDamage ->
//            newAmount = orb.receiveOnPlayerDamaged(newAmount, info)
//        }
//        return newAmount
//    }

    override fun receivePreMonsterTurn(abstractMonster: AbstractMonster?): Boolean
    {
//        if (!hasHadInMonsterTurn)
//            ApplyAll(AtStartOfMonsterTurnSubscriber::atStartOfMonsterTurn, AtStartOfMonsterTurnSubscriber.class);
//        hasHadInMonsterTurn = true;
        InBattleDataManager.ApplyAll(
            { sub: PreMonsterTurnSubscriber -> sub.receivePreMonsterTurn(abstractMonster) },
            PreMonsterTurnSubscriber::class.java
        )
        return true
    }

    //    public interface AtStartOfMonsterTurnSubscriber extends ISubscriber {
    //        void atStartOfMonsterTurn();
    //    }
    interface AtEndOfPlayerTurnPreCardSubscriber : ISubscriber
    {
        fun receiveAtEndOfPlayerTurnPreCard()

        companion object
        {
            @SpirePatch2(clz = GameActionManager::class, method = "callEndOfTurnActions")
            object AtEndOfTurnSubscriberPatch
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(__instance: GameActionManager?)
                {
                    InBattleDataManager.ApplyAll(
                        AtEndOfPlayerTurnPreCardSubscriber::receiveAtEndOfPlayerTurnPreCard,
                        AtEndOfPlayerTurnPreCardSubscriber::class.java
                    )
                }
            }
        }
    }

//    interface AtLastHpSubscriber : ISubscriber
//    {
//        fun receiveAtLastHp()
//
//        @SpirePatch(clz = AbstractPlayer::class, method = "damage", paramtypez = [DamageInfo?::class])
//        object OnPlayerDamagedHook
//        {
//            @SpireInsertPatch(localvars = ["damageAmount"], locator = LocatorPre::class)
//            @JvmStatic
//            fun InsertPre(__instance: AbstractPlayer?, info: DamageInfo?, @ByRef damageAmount: IntArray)
//            {
//                var damage = BaseMod.publishOnPlayerDamaged(damageAmount[0], info)
//                if (damage < 0)
//                {
//                    damage = 0
//                }
//
//                damageAmount[0] = damage
//            }
//
//            private class LocatorPre private constructor() : SpireInsertLocator()
//            {
//                @Throws(Exception::class)
//                override fun Locate(ctBehavior: CtBehavior): IntArray
//                {
//                    val matcher: Matcher = MethodCallMatcher(AbstractRelic::class.java, "onLoseHpLast")
//                    return LineFinder.findInOrder(ctBehavior, matcher)
//                }
//            }
//        }
//    }

    interface AtManualDiscardSubscriber : ISubscriber
    {
        fun receiveAtManualDiscard()

        interface AtManualDiscardPower
        {
            fun atManualDiscard()
        }

        companion object
        {
            @SpirePatch2(clz = GameActionManager::class, method = "incrementDiscard", paramtypez = [Boolean::class])
            object MotherFuckerWhyTheyDoNotMakeThisDiscardSubscriberPatch
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(endOfTurn: Boolean)
                {
                    if (endOfTurn) return
                    InBattleDataManager.ApplyAll(
                        AtManualDiscardSubscriber::receiveAtManualDiscard,
                        AtManualDiscardSubscriber::class.java
                    )
                    PowerUtility.foreachPower { power: AbstractPower ->
                        ToolBox.doIfIsInstance(
                            power,
                            AtManualDiscardPower::class.java,
                            AtManualDiscardPower::atManualDiscard
                        )
                    }
                }
            }
        }
    } //    public interface AtEndOfRoundSubscriber extends ISubscriber {
    //        void receiveAtEndOfRound();
    //
    //        @SpirePatch2(clz = A.class, method = "applyEndOfTurnTriggers")
    //        class AtEndOfRoundSubscriberPatch {
    //            @SpirePrefixPatch
    //            public static void Prefix(AbstractCreature __instance) {
    //                if (__instance instanceof AbstractPlayer)
    //                    ApplyAll(AtEndOfRoundSubscriber::receiveAtEndOfRound, AtEndOfRoundSubscriber.class);
    //            }
    //        }
    //    }

    companion object
    {
        @JvmStatic
        fun initialize()
        {
            val mod = SuperstitioApiSubscriber()
        }
    }


}
