package superstitio;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;


@SpireInitializer
public class SuperstitioModSubscriber implements
        PostExhaustSubscriber, StartGameSubscriber, RelicGetSubscriber, PostPowerApplySubscriber,
        PostCreateStartingDeckSubscriber, PostCreateStartingRelicsSubscriber,
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
    }

    @Override
    public void receiveStartGame() {

//        if (player instanceof BaseCharacter) {
//            if (!player.hasRelic(JokeDescription.ID))
//                RelicLibrary.getRelic(JokeDescription.ID).makeCopy().instantObtain(player, player.relics.size(), false);
//        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
    }

    @Override
    public void receivePowersModified() {
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        InBattleDataManager.ClearOnEndOfBattle();
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
            AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
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

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        return true;
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup cardGroup) {

    }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> arrayList) {

    }
}
