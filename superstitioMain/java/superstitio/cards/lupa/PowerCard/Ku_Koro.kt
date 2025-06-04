package superstitio.cards.lupa.PowerCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsLupaCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.lupa.LupaCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

//咕杀/くっころ
@IsLupaCard
class Ku_Koro : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(Ku_KoroPower())
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    class Ku_KoroPower : EasyBuildAbstractPowerForPowerCard(-1)
    {
        override fun makePowerCard(): SuperstitioCard
        {
            return Ku_Koro()
        }

        override fun onExhaust(card: AbstractCard)
        {
            ActionUtility.addToBot_makeTempCardInBattle(card, ActionUtility.BattleCardPlace.Discard)
        }

        override fun wasHPLost(info: DamageInfo, damageAmount: Int)
        {
            if (damageAmount > 0 && info.owner !== AbstractDungeon.player && info.type == DamageType.NORMAL)
            {
                this.flash()
                val card = CardUtility.getRandomStatusCard(true, true)
                ActionUtility.addToBot_makeTempCardInBattle(card, ActionUtility.BattleCardPlace.Hand)
            }
        }

        override fun updateDescriptionArgs()
        {
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Ku_Koro::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1

        private const val COST_UPGRADED_NEW = 0
    }
}
