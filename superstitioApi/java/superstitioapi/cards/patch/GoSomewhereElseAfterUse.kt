package superstitioapi.cards.patch

import com.megacrit.cardcrawl.cards.CardGroup

/**
 * 该接口用于定义在使用后中断移动到卡组的行为。
 * 它提供了一个方法来指定一个卡组，中断移动的单位将会前往该卡组。
 */
interface GoSomewhereElseAfterUse
{
    /**
     * 在中断移动后，将单位转移到指定的卡组。
     * @param cardGroup 接收单位的卡组对象，单位将移动到这个卡组。
     */
    fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
}

