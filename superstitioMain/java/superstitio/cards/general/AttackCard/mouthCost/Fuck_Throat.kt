package superstitio.cards.general.AttackCard.mouthCost

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.hangUpCard.HangUpCardGroup

class Fuck_Throat : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card {
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MagicNum)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        HangUpCardGroup.forHangUpCardGroup { group: HangUpCardGroup ->
            if (group.hasOrb()) addToBot(GainEnergyAction(this.magicNumber))
        }.addToBotAsAbstractAction()
    }

    override fun triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy()
        HangUpCardGroup.forHangUpCardGroup { group: HangUpCardGroup ->
            if (group.hasOrb()) this.glowColor = Color.PINK.cpy()
        }.get()
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Throat::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 2
        private const val DAMAGE = 10
        private const val UPGRADE_DAMAGE = 3
        private const val MagicNum = 2
    }
}
