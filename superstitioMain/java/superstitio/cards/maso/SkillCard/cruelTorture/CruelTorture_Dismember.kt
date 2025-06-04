package superstitio.cards.maso.SkillCard.cruelTorture

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.CruelTortureTag
import superstitio.cards.IsMasoCard
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.cards.addRetainMod
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.Card_TriggerHangCardManually
import superstitioapi.hangUpCard.HangUpCardGroup
@IsMasoCard
//凌迟
class CruelTorture_Dismember : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), Card_TriggerHangCardManually
{
    init
    {
        this.setupMagicNumber(MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        CardModifierManager.addModifier(this, CruelTortureTag())
        this.addRetainMod()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        HangUpCardGroup.forEachHangUpCard {
            addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber))
            addToBot_gainBlock()
        }.addToBotAsAbstractAction()
    }

    override fun upgradeAuto()
    {
    }

    override fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean
    {
        orb.targetType = CardOrb.HangOnTarget.Self
        orb.actionType = CardOrb.HangEffectType.Good
        return true
    }

    override fun forceChangeOrbCounterShown(orb: CardOrb): Int
    {
        return 0
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CruelTorture_Dismember::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 1
        private const val BLOCK = 3
        private const val UPGRADE_BLOCK = 1
    }
}

