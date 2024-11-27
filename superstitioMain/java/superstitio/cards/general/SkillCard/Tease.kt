package superstitio.cards.general.SkillCard

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.BarricadePower
import com.megacrit.cardcrawl.powers.FrailPower
import com.megacrit.cardcrawl.powers.WeakPower
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.actions.AutoDoneInstantAction

class Tease : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.exhaust = true
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val a = (monster!!.maxHealth * MAGIC / 100f).toInt()
        AutoDoneInstantAction.addToBotAbstract { monster.decreaseMaxHealth(a) }
        addToBot_applyPower(FrailPower(monster, 1, false))
        addToBot_applyPower(WeakPower(monster, 1, false))
        addToBot(GainBlockAction(monster, (a * 0.75f).toInt())) //乐，实际上脆弱还是不起效
        addToBot_applyPower(BarricadePower(monster))
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Tease::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 0
        private const val MAGIC = 15
        private const val UPGRADE_MAGIC = 5
    }
}
