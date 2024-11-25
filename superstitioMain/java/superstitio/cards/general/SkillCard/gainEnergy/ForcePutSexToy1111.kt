package superstitio.cards.general.SkillCard.gainEnergy

import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.TempCard.SexToy
import superstitio.powers.*
import superstitioapi.actions.AutoDoneInstantAction

class ForcePutSexToy : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        //        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        for (i in 0 until magicNumber) {
            addToBot_applyPower(SexToy(AbstractDungeon.player, 1, SexToy.Companion.getRandomSexToyName()))
        }
        addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber))
        AutoDoneInstantAction.addToBotAbstract {
            AbstractDungeon.player.powers.stream()
                .filter { power: AbstractPower? -> power is superstitio.powers.SexToy }
                .map { power: AbstractPower -> power as superstitio.powers.SexToy }
                .forEach(superstitio.powers.SexToy::Trigger)
        }
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(ForcePutSexToy::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1
        private const val DAMAGE = 1

        private const val UPGRADE_DAMAGE = 1
    }
}
