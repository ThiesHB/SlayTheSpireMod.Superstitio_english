package superstitio.cards.general.BaseCard

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager.SPTT_DATA.GeneralEnums
import superstitio.cards.SuperstitioCard

abstract class Invite(id: String) :
    SuperstitioCard(id, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, GeneralEnums.GENERAL_CARD, "base")
{
    init
    {
        tags.add(CardTags.STARTER_DEFEND)
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, makeNewBlockType())
    }

    protected abstract fun makeNewBlockType(): AbstractBlockModifier

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        //        addToBot_reducePowerToPlayer(SexualDamage_ByEnemy.POWER_ID, this.block);
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 5
        private const val UPGRADE_PLUS_BLOCK = 3
    }
}
