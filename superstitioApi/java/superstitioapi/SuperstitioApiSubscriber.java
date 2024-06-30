package superstitioapi;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.player.PlayerInitPostDungeonInitialize;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;
import superstitioapi.utils.ToolBox;

import java.util.Objects;

import static superstitioapi.InBattleDataManager.ApplyAll;
import static superstitioapi.utils.PowerUtility.foreachPower;


@SpireInitializer
public class SuperstitioApiSubscriber implements
        PostExhaustSubscriber, StartGameSubscriber, RelicGetSubscriber, PostPowerApplySubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber, OnStartBattleSubscriber, OnPlayerTurnStartSubscriber,
        OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber, PostEnergyRechargeSubscriber, PreMonsterTurnSubscriber {

//    public static boolean hasHadInMonsterTurn = false;

    public SuperstitioApiSubscriber() {
        BaseMod.subscribe(this);
        Logger.run("Done " + this + " subscribing");
    }

    public static void initialize() {
        new SuperstitioApiSubscriber();
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
        ApplyAll((sub) -> sub.receivePostExhaust(abstractCard), PostExhaustSubscriber.class);
    }

    @Override
    public void receiveStartGame() {
        ApplyAll(StartGameSubscriber::receiveStartGame, StartGameSubscriber.class);
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        ApplyAll((sub) -> sub.receiveCardUsed(abstractCard), OnCardUseSubscriber.class);
    }

    @Override
    public void receivePowersModified() {
        ApplyAll(OnPowersModifiedSubscriber::receivePowersModified, OnPowersModifiedSubscriber.class);
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        InBattleDataManager.ClearOnEndOfBattle();
        ApplyAll((sub) -> sub.receivePostBattle(abstractRoom), PostBattleSubscriber.class);
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        ApplyAll((sub) -> sub.receivePostDraw(abstractCard), PostDrawSubscriber.class);
    }

    @Override
    public void receivePostDungeonInitialize() {
        ApplyAll(PostDungeonInitializeSubscriber::receivePostDungeonInitialize, PostDungeonInitializeSubscriber.class);
        if (AbstractDungeon.player instanceof PlayerInitPostDungeonInitialize) {
            ((PlayerInitPostDungeonInitialize) AbstractDungeon.player).initPostDungeonInitialize();
        }
    }

    @Override
    public void receivePostEnergyRecharge() {
        ApplyAll(PostEnergyRechargeSubscriber::receivePostEnergyRecharge, PostEnergyRechargeSubscriber.class);
    }

    @Override
    public void receivePostPowerApplySubscriber(
            AbstractPower appliedPower, AbstractCreature target, AbstractCreature source) {
        AutoDoneInstantAction.addToTopAbstract(() -> {
            if (appliedPower instanceof OnPostApplyThisPower)
                target.powers.forEach(power -> {
                    if (power instanceof OnPostApplyThisPower)
                        if (Objects.equals(power.ID, appliedPower.ID)) {
                            ((OnPostApplyThisPower) power).tryInitializePostApplyThisPower(appliedPower, power.getClass());
                        }
                });
        });
        ApplyAll((sub) -> sub.receivePostPowerApplySubscriber(appliedPower, source, source), PostPowerApplySubscriber.class);

    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
        ApplyAll((sub) -> sub.receiveRelicGet(abstractRelic), RelicGetSubscriber.class);
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        InBattleDataManager.InitializeAtStartOfBattle();
//        hasHadInMonsterTurn = false;
        ApplyAll((sub) -> sub.receiveOnBattleStart(abstractRoom), OnStartBattleSubscriber.class);
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        InBattleDataManager.InitializeAtStartOfTurn();
//        hasHadInMonsterTurn = false;
        ApplyAll(OnPlayerTurnStartSubscriber::receiveOnPlayerTurnStart, OnPlayerTurnStartSubscriber.class);
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
//        if (!hasHadInMonsterTurn)
//            ApplyAll(AtStartOfMonsterTurnSubscriber::atStartOfMonsterTurn, AtStartOfMonsterTurnSubscriber.class);
//        hasHadInMonsterTurn = true;
        ApplyAll((sub) -> sub.receivePreMonsterTurn(abstractMonster), PreMonsterTurnSubscriber.class);
        return true;
    }

//    public interface AtStartOfMonsterTurnSubscriber extends ISubscriber {
//        void atStartOfMonsterTurn();
//    }

    public interface AtEndOfPlayerTurnPreCardSubscriber extends ISubscriber {
        void receiveAtEndOfPlayerTurnPreCard();

        @SpirePatch2(clz = GameActionManager.class, method = "callEndOfTurnActions")
        class AtEndOfTurnSubscriberPatch {
            @SpirePrefixPatch
            public static void Prefix(GameActionManager __instance) {
                ApplyAll(AtEndOfPlayerTurnPreCardSubscriber::receiveAtEndOfPlayerTurnPreCard, AtEndOfPlayerTurnPreCardSubscriber.class);
            }
        }
    }

    public interface AtManualDiscardSubscriber extends ISubscriber {
        void receiveAtManualDiscard();

        interface AtManualDiscardPower {
            void atManualDiscard();
        }

        @SpirePatch2(clz = GameActionManager.class, method = "incrementDiscard", paramtypez = {boolean.class})
        class MotherFuckerWhyTheyDoNotMakeThisDiscardSubscriberPatch {
            @SpirePrefixPatch
            public static void Prefix(boolean endOfTurn) {
                if (endOfTurn) return;
                ApplyAll(AtManualDiscardSubscriber::receiveAtManualDiscard, AtManualDiscardSubscriber.class);
                foreachPower(power ->
                        ToolBox.doIfIsInstance(power, AtManualDiscardPower::atManualDiscard, AtManualDiscardPower.class));
            }
        }
    }


//    public interface AtEndOfRoundSubscriber extends ISubscriber {
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
}
