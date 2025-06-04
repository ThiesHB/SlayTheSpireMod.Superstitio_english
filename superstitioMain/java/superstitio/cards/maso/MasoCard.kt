package superstitio.cards.maso

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect
import superstitio.cards.NormalCard
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower
import superstitio.delayHpLose.RemoveDelayHpLoseBlock

/**
 * @see NormalCard
 */
abstract class MasoCard(
    id: String,
    cardType: CardType,
    cost: Int,
    cardRarity: CardRarity,
    cardTarget: CardTarget,
    imgSubFolder: String = CardTypeToString(cardType),
) : NormalCard(id, cardType, cost, cardRarity, cardTarget, imgSubFolder)
{

    override fun addToBot_gainCustomBlock(amount: Int, blockModifier: AbstractBlockModifier)
    {
        if (blockModifier is DelayRemoveDelayHpLoseBlock)
        {
            addToBot_applyPower(DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount))
            AbstractDungeon.effectList.add(
                FlashAtkImgEffect(
                    AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AttackEffect.SHIELD
                )
            )
            return
        }
        if (blockModifier is RemoveDelayHpLoseBlock)
        {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true)
            AbstractDungeon.effectList.add(
                FlashAtkImgEffect(
                    AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AttackEffect.SHIELD
                )
            )
            return
        }
        super.addToBot_gainCustomBlock(amount, blockModifier)
    }
}
