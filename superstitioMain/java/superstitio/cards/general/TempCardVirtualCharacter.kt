package superstitio.cards.general

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.cutscenes.CutscenePanel
import com.megacrit.cardcrawl.screens.CharSelectInfo
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA.TempCardEnums
import superstitio.cards.general.BaseCard.Kiss
import superstitio.cards.lupa.BaseCard.Masturbate
import superstitio.characters.BaseCharacter
import superstitio.relics.blight.JokeDescription

// 继承CustomPlayer类
class TempCardVirtualCharacter(name: String) : BaseCharacter(ID, name, TempCardEnums.TempCard_Virtual_Character)
{
    override fun getStartingDeck(): ArrayList<String>
    {
        val startingDeck = ArrayList<String>()
        for (x in 0..4)
        {
            startingDeck.add(Kiss.ID)
        }
        startingDeck.add(Masturbate.ID)
        return startingDeck
    }

    // 初始遗物
    override fun getStartingRelics(): ArrayList<String>
    {
        val retVal = ArrayList<String>()
        retVal.add(JokeDescription.ID)
        return retVal
    }

    override fun getLoadout(): CharSelectInfo
    {
        return CharSelectInfo(
            characterStrings!!.NAMES[0],  // 人物名字
            characterStrings.TEXT[0],  // 人物介绍
            0,  // 当前血量
            60,  // 最大血量
            0,  // 初始充能球栏位
            99,  // 初始携带金币
            5,  // 每回合抽牌数量
            this,  // 别动
            this.startingRelics,  // 初始遗物
            this.startingDeck,  // 初始卡组
            false // 别动
        )
    }


    // 你的卡牌颜色（这个枚举在最下方创建）
    override fun getCardColor(): CardColor
    {
        return TempCardEnums.TempCard_CARD
    }

    // 碎心图片
    override fun getCutscenePanels(): MutableList<CutscenePanel>
    {
        return ArrayList()
    }

    // 创建人物实例，照抄
    override fun newInstance(): AbstractPlayer
    {
        return TempCardVirtualCharacter(this.name)
    }

    override fun isCardCanAdd(card: AbstractCard?): Boolean
    {
        return false
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID("TempCard")
    }
}