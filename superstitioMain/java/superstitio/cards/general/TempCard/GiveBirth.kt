package superstitio.cards.general.TempCard

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.PregnantBlock
import superstitio.cards.general.AbstractTempCard
import superstitioapi.actions.AutoDoneInstantAction

class GiveBirth() : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    var sealPower: MutableList<AbstractPower> = ArrayList()
    var sealMonster: AbstractCreature? = null

    init
    {
        this.exhaust = true
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK)
    }

    constructor(sealPower: MutableList<AbstractPower>, sealMonster: AbstractCreature?) : this()
    {
        this.sealPower = sealPower
        if (sealMonster != null)
        {
            this.sealMonster = sealMonster
            this.name = this.originalName + ": " + sealMonster.name
        }
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        //this.gainBlock();
        this.addToBot(AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, this.block))
        for (blockInstance in BlockModifierManager.blockInstances(AbstractDungeon.player))
        {
            if (blockInstance.blockTypes.stream()
                    .anyMatch { blockModifier: AbstractBlockModifier? -> blockModifier is PregnantBlock }
            )
            {
                AutoDoneInstantAction.addToBotAbstract { BlockModifierManager.removeSpecificBlockType(blockInstance) }
            }
        }
    }

    override fun upgradeAuto()
    {
    }

    override fun makeCopy(): AbstractCard
    {
        val newCard = super.makeCopy() as GiveBirth?
        if (newCard != null)
        {
            newCard.sealMonster = this.sealMonster
            newCard.sealPower = this.sealPower
            return newCard
        }
        else return super.makeCopy()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(GiveBirth::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.SPECIAL

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val BLOCK = 5
        private const val UPGRADE_PLUS_BLOCK = 3
    }
}
