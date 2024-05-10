package superstitio;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitio.powers.interfaces.OnPostApplyThisPower;

import static superstitio.InBattleDataManager.ApplyAll;


@SpireInitializer
public class SuperstitioModSubscriber implements
        PostExhaustSubscriber, StartGameSubscriber, PostUpdateSubscriber, RelicGetSubscriber, PostPowerApplySubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber, OnStartBattleSubscriber, OnPlayerTurnStartSubscriber,
        OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber, PostEnergyRechargeSubscriber, PreMonsterTurnSubscriber {

    public static boolean hasHadInMonsterTurn = false;

    public SuperstitioModSubscriber() {
        BaseMod.subscribe(this);
        Logger.run("Done " + this + " subscribing");
    }

    public static void initialize() {
        new SuperstitioModSubscriber();
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
        ApplyAll((sub) -> sub.receivePostExhaust(abstractCard), PostExhaustSubscriber.class);
    }

    @Override
    public void receivePostUpdate() {
        ApplyAll(PostUpdateSubscriber::receivePostUpdate, PostUpdateSubscriber.class);
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
    }

    @Override
    public void receivePostEnergyRecharge() {
        ApplyAll(PostEnergyRechargeSubscriber::receivePostEnergyRecharge, PostEnergyRechargeSubscriber.class);
    }

    @Override
    public void receivePostPowerApplySubscriber(
            AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof OnPostApplyThisPower)
            ((OnPostApplyThisPower) abstractPower).InitializePostApplyThisPower();
        ApplyAll((sub) -> sub.receivePostPowerApplySubscriber(abstractPower, abstractCreature, abstractCreature1), PostPowerApplySubscriber.class);

    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
        ApplyAll((sub) -> sub.receiveRelicGet(abstractRelic), RelicGetSubscriber.class);
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        InBattleDataManager.InitializeAtStartOfBattle();
        hasHadInMonsterTurn = false;
        ApplyAll((sub) -> sub.receiveOnBattleStart(abstractRoom), OnStartBattleSubscriber.class);
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        InBattleDataManager.InitializeAtStartOfTurn();
        hasHadInMonsterTurn = false;
        ApplyAll(OnPlayerTurnStartSubscriber::receiveOnPlayerTurnStart, OnPlayerTurnStartSubscriber.class);
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        if (!hasHadInMonsterTurn)
            ApplyAll(AtStartOfMonsterTurnSubscriber::atStartOfMonsterTurn, AtStartOfMonsterTurnSubscriber.class);
        hasHadInMonsterTurn = true;
        ApplyAll((sub) -> sub.receivePreMonsterTurn(abstractMonster), PreMonsterTurnSubscriber.class);
        return true;
    }

    public interface AtStartOfMonsterTurnSubscriber extends ISubscriber {
        void atStartOfMonsterTurn();
    }
}
