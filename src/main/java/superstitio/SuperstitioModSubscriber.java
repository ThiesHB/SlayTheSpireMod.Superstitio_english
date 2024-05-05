package superstitio;

import superstitio.powers.interfaces.OnPostApplyThisPower;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


@SpireInitializer
public class SuperstitioModSubscriber implements
        PostExhaustSubscriber, StartGameSubscriber, PostUpdateSubscriber, RelicGetSubscriber, PostPowerApplySubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber, OnStartBattleSubscriber, OnPlayerTurnStartSubscriber,
        OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber, PostEnergyRechargeSubscriber {


    public SuperstitioModSubscriber() {
        BaseMod.subscribe(this);
        Logger.run("Done " + this + " subscribing");
    }

    public static void initialize() {
        new SuperstitioModSubscriber();
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
    }

    @Override
    public void receivePostUpdate() {
    }

    @Override
    public void receiveStartGame() {
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
    }

    @Override
    public void receivePowersModified() {
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
    }

    @Override
    public void receivePostDungeonInitialize() {
    }

    @Override
    public void receivePostEnergyRecharge() {
    }

    @Override
    public void receivePostPowerApplySubscriber(
            AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof OnPostApplyThisPower)
            ((OnPostApplyThisPower) abstractPower).InitializePostApplyThisPower();
    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        InBattleDataManager.InitializeAtStartOfBattle();
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        InBattleDataManager.InitializeAtStartOfTurn();
    }
}
