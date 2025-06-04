package superstitio.cards.lupa.AttackCard

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsLupaCard
import superstitio.cards.lupa.LupaCard
import superstitio.powers.SexualHeat
import superstitioapi.utils.setDescriptionArgs
import java.util.*
@IsLupaCard
class Whipping : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
        this.isMultiDamage = true
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(HEAT_GET)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamageToAllEnemies(AttackEffect.LIGHTNING)
        SexualHeat.addToBot_addSexualHeat(
            AbstractDungeon.player,
            Arrays.stream(this.multiDamage).sum() / this.magicNumber
        )
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Whipping::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ALL_ENEMY

        private const val COST = 1
        private const val DAMAGE = 7
        private const val UPGRADE_DAMAGE = 2

        private const val MAGIC = 3
        private const val HEAT_GET = 1
    }
}
