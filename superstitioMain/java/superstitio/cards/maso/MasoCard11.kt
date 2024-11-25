package superstitio.cards.maso

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect
import superstitio.DataManager.SPTT_DATA.MasoEnums
import superstitio.cards.CardOwnerPlayerManager.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower
import superstitio.delayHpLose.RemoveDelayHpLoseBlock

abstract class MasoCard
/**
 * 当需要自定义卡牌存储的二级目录名时
 */
/**
 * 普通的方法
 *
 * @param id         卡牌ID
 * @param cardType   卡牌类型
 * @param cost       卡牌消耗
 * @param cardRarity 卡牌稀有度
 * @param cardTarget 卡牌目标
 */
@JvmOverloads constructor(
    id: String, cardType: CardType, cost: Int, cardRarity: CardRarity, cardTarget: CardTarget,
    imgSubFolder: String = CardTypeToString(cardType)
) : SuperstitioCard(id, cardType, cost, cardRarity, cardTarget, MasoEnums.MASO_CARD, imgSubFolder), IsMasoCard {
    private val isDelayRemoveDelayHpLoseBlock = false
    private val isRemoveDelayHpLoseBlock = false

    override fun addToBot_gainCustomBlock(amount: Int, blockModifier: AbstractBlockModifier) {
        if (blockModifier is DelayRemoveDelayHpLoseBlock) {
            addToBot_applyPower(DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount))
            AbstractDungeon.effectList.add(
                FlashAtkImgEffect(
                    AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                    AttackEffect.SHIELD
                )
            )
            return
        }
        if (blockModifier is RemoveDelayHpLoseBlock) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true)
            AbstractDungeon.effectList.add(
                FlashAtkImgEffect(
                    AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                    AttackEffect.SHIELD
                )
            )
            return
        }
        super.addToBot_gainCustomBlock(amount, blockModifier)
    }
}
