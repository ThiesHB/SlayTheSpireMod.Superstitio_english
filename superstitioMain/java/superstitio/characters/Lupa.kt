package superstitio.characters

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.screens.CharSelectInfo
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA.LupaEnums
import superstitio.Logger
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.general.BaseCard.Kiss
import superstitio.cards.lupa.BaseCard.DrySemen
import superstitio.cards.lupa.BaseCard.Invite_Lupa
import superstitio.cards.lupa.BaseCard.Masturbate
import superstitio.relics.a_starter.StartWithSexToy
import superstitio.relics.blight.DevaBody_Lupa
import superstitio.relics.blight.JokeDescription
import superstitio.relics.blight.SemenMagician
import superstitio.relics.blight.Sensitive
import superstitioapi.player.PlayerInitPostDungeonInitialize
import superstitioapi.relicToBlight.InfoBlight

// 继承CustomPlayer类
class Lupa(name: String) : BaseCharacter(ID, name, LupaEnums.LUPA_Character), PlayerInitPostDungeonInitialize
{
    init
    {
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
            LUPA_CHARACTER,  // 人物图片
            LUPA_CHARACTER_SHOULDER_2, LUPA_CHARACTER_SHOULDER_1, BlondHairBlueEyes_CORPSE_IMAGE,  // 人物死亡图像
            this.loadout, 0.0f, 0.0f, 250.0f, 375.0f,  // 人物碰撞箱大小，越大的人物模型这个越大
            EnergyManager(3) // 初始每回合的能量
        )
    }


    // 初始遗物
    override fun getStartingRelics(): ArrayList<String>
    {
        val retVal = ArrayList<String>()
        retVal.add(StartWithSexToy.ID)
        //        retVal.add(JokeDescription.ID);
//        retVal.add(DevaBody_Lupa.ID);
//        retVal.add(Sensitive.ID);
//        retVal.add(SorM.ID);
        return retVal
    }

    override fun getLoadout(): CharSelectInfo
    {
        return CharSelectInfo(
            characterStrings!!.NAMES[0],  // 人物名字
            characterStrings.TEXT[0],  // 人物介绍
            characterInfo.currentHp,  // 当前血量
            characterInfo.maxHp,  // 最大血量
            0,  // 初始充能球栏位
            characterInfo.gold,  // 初始携带金币
            5,  // 每回合抽牌数量
            this,  // 别动
            this.startingRelics,  // 初始遗物
            this.startingDeck,  // 初始卡组
            false // 别动
        )
    }

    override fun getStartingDeck(): ArrayList<String>
    {
        Logger.run("Begin loading starter Deck Strings")
        return LupaStartDeck()
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    override fun getCardColor(): CardColor
    {
        return LupaEnums.LUPA_CARD
    }

    override fun newInstance(): AbstractPlayer
    {
        return Lupa(this.name)
    }

    override fun getAscensionMaxHPLoss(): Int
    {
        return 5
    }

    override fun initPostDungeonInitialize()
    {
        InfoBlight.addAsInfoBlight(JokeDescription())
        InfoBlight.addAsInfoBlight(DevaBody_Lupa())
        InfoBlight.addAsInfoBlight(Sensitive())
        InfoBlight.addAsInfoBlight(SemenMagician())
    }

    override fun isCardCanAdd(card: AbstractCard?): Boolean
    {
        return CardOwnerPlayerManager.isLupaCard(card)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Lupa::class.java.simpleName)
        val characterInfo: CharacterSelectInfo = CharacterSelectInfo(65, 65, 99)

        fun LupaStartDeck(): ArrayList<String>
        {
            val startingDeck = ArrayList<String>()
            for (x in 0..4)
            {
                startingDeck.add(Kiss.ID)
            }
            for (x in 0..3)
            {
                startingDeck.add(Invite_Lupa.ID)
            }
            startingDeck.add(Masturbate.ID)
            startingDeck.add(DrySemen.ID)
            return startingDeck
        }
    }
}