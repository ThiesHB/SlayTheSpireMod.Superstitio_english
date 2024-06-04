package superstitio;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitio.characters.BaseCharacter;
import superstitio.characters.Lupa;
import superstitio.characters.Maso;
import superstitio.relics.blight.*;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;


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
        if (!SuperstitioModSetup.getEnableGuroCharacter() && player instanceof Maso) {
            SuperstitioModSetup.setEnableGuroCharacter(true);
        }
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
        if (player instanceof BaseCharacter) {
            addBlight(new JokeDescription.BlightWithRelic_JokeDescription());
            if (player instanceof Lupa) {
                addBlight(new Sensitive.BlightWithRelic_Sensitive());
                addBlight(new DevaBody_Lupa.BlightWithRelic_DevaBody_Lupa());
            }
            else if (player instanceof Maso) {
                if (floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
                    player.currentHealth = player.getLoadout().currentHp;
                    if (ascensionLevel >= 6) {
                        player.currentHealth = MathUtils.round((float) player.currentHealth * 0.9F);
                    }
                }
                addBlight(new MasochismMode.BlightWithRelic_MasochismMode());
                addBlight(new DevaBody_Masochism.BlightWithRelic_DevaBody_Maso());
            }
        }
    }

    public void addBlight(AbstractBlight blight) {
        if (player == null) return;
        if (player.hasBlight(blight.blightID)) return;
        blight.instantObtain(player, player.blights.size(), false);
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
