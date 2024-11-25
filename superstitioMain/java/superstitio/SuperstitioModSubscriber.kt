package superstitio

import basemod.BaseMod
import basemod.interfaces.*
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom

@SpireInitializer
class SuperstitioModSubscriber : PostExhaustSubscriber, StartGameSubscriber, RelicGetSubscriber,
    PostPowerApplySubscriber, PostCreateStartingDeckSubscriber, PostCreateStartingRelicsSubscriber,
    PostBattleSubscriber, PostDungeonInitializeSubscriber, OnStartBattleSubscriber, OnPlayerTurnStartSubscriber,
    OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber, PostEnergyRechargeSubscriber,
    PreMonsterTurnSubscriber {
    init {
        BaseMod.subscribe(this)
        Logger.run("Done $this subscribing")
    }

    override fun receivePostExhaust(abstractCard: AbstractCard?) {
    }

    override fun receiveStartGame() {
        //        if (player instanceof BaseCharacter) {
//            if (!player.hasRelic(JokeDescription.ID))
//                RelicLibrary.getRelic(JokeDescription.ID).makeCopy().instantObtain(player, player.relics.size(), false);
//        }
    }

    override fun receiveCardUsed(abstractCard: AbstractCard?) {
    }

    override fun receivePowersModified() {
    }

    override fun receivePostBattle(abstractRoom: AbstractRoom?) {
        InBattleDataManager.ClearOnEndOfBattle()
    }

    override fun receivePostDraw(abstractCard: AbstractCard?) {
    }

    override fun receivePostDungeonInitialize() {
    }

    override fun receivePostEnergyRecharge() {
    }

    override fun receivePostPowerApplySubscriber(
        abstractPower: AbstractPower?, target: AbstractCreature?, source: AbstractCreature?
    ) {
    }

    override fun receiveRelicGet(abstractRelic: AbstractRelic?) {
    }

    override fun receiveOnBattleStart(abstractRoom: AbstractRoom?) {
        InBattleDataManager.InitializeAtStartOfBattle()
    }

    override fun receiveOnPlayerTurnStart() {
        InBattleDataManager.InitializeAtStartOfTurn()
    }

    override fun receivePreMonsterTurn(abstractMonster: AbstractMonster?): Boolean {
        return true
    }

    override fun receivePostCreateStartingDeck(playerClass: PlayerClass?, cardGroup: CardGroup?) {
    }

    override fun receivePostCreateStartingRelics(playerClass: PlayerClass?, arrayList: ArrayList<String>?) {
    }

    companion object {
        var hasHadInMonsterTurn: Boolean = false

        @JvmStatic
        fun initialize() {
            val mod = SuperstitioModSubscriber()
        }
    }
}
