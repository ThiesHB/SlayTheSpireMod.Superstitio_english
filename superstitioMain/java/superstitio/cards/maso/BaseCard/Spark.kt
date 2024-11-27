package superstitio.cards.maso.BaseCard

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.damage.SexDamage
import superstitio.cards.maso.MasoCard
import superstitioapi.actions.AutoDoneInstantAction

class Spark : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base")
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE, SexDamage())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster)
        AutoDoneInstantAction.addToBotAbstract {
            if (sumAllDelayHpLosePower() >= this.magicNumber) addToBot_dealDamage(monster)
        }
    }

    override fun triggerOnGlowCheck()
    {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy()
        if (sumAllDelayHpLosePower() >= this.magicNumber) this.glowColor = Color.PINK.cpy()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Spark::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 7
        private const val UPGRADE_DAMAGE = 3
        private const val MAGIC = 8
        private const val UPGRADE_MAGIC = -2
    }
}
