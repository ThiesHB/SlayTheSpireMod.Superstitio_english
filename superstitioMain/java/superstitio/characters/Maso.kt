package superstitio.characters

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.dungeons.Exordium
import com.megacrit.cardcrawl.screens.CharSelectInfo
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA.MasoEnums
import superstitio.Logger
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.general.BaseCard.Kiss
import superstitio.cards.maso.BaseCard.FistIn
import superstitio.cards.maso.BaseCard.Invite_Maso
import superstitio.cards.maso.BaseCard.Spark
import superstitio.relics.a_starter.VulnerableTogetherRelic
import superstitio.relics.blight.DevaBody_Masochism
import superstitio.relics.blight.EnjoyAilment
import superstitio.relics.blight.JokeDescription
import superstitio.relics.blight.MasochismMode
import superstitioapi.player.PlayerInitPostDungeonInitialize
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect

// 继承CustomPlayer类
class Maso(name: String) : BaseCharacter(ID, name, MasoEnums.MASO_Character), PlayerInitPostDungeonInitialize,
    RenderInCharacterSelect
{
    init
    {
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
            MASO_CHARACTER,  // 人物图片
            MASO_CHARACTER_SHOULDER_2, MASO_CHARACTER_SHOULDER_2, BlondHairBlueEyes_CORPSE_IMAGE,  // 人物死亡图像
            this.loadout, 0.0f, 0.0f, 250.0f, 375.0f,  // 人物碰撞箱大小，越大的人物模型这个越大
            EnergyManager(3) // 初始每回合的能量
        )
    }

    override fun getAscensionMaxHPLoss(): Int
    {
        return 5
    }

    // 初始遗物
    override fun getStartingRelics(): ArrayList<String>
    {
        val retVal = ArrayList<String>()
        retVal.add(VulnerableTogetherRelic.ID)
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
        return MasoStartDeck()
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    override fun getCardColor(): CardColor
    {
        return MasoEnums.MASO_CARD
    }

    // 创建人物实例，照抄
    override fun newInstance(): AbstractPlayer
    {
        return Maso(this.name)
    }

    override fun initPostDungeonInitialize()
    {
        setUpMaso()
        InfoBlight.addAsInfoBlight(JokeDescription())
        InfoBlight.addAsInfoBlight(DevaBody_Masochism())
        InfoBlight.addAsInfoBlight(MasochismMode())
        InfoBlight.addAsInfoBlight(EnjoyAilment())
    }

    override fun renderInCharacterSelectScreen(characterOption: CharacterOption, sb: SpriteBatch)
    {
    }

    override fun updateInCharacterSelectScreen(characterOption: CharacterOption)
    {
        unableByGuroSetting()
    }

    override fun isCardCanAdd(card: AbstractCard?): Boolean
    {
        return CardOwnerPlayerManager.isMasoCard(card)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Maso::class.java.simpleName)
        val characterInfo: CharacterSelectInfo = CharacterSelectInfo(60, 70, 110)

        fun setUpMaso()
        {
            if (AbstractDungeon.floorNum > 1 || CardCrawlGame.dungeon !is Exordium) return
            AbstractDungeon.player.currentHealth = AbstractDungeon.player.loadout.currentHp
            if (AbstractDungeon.ascensionLevel >= 6)
            {
                AbstractDungeon.player.currentHealth =
                    MathUtils.round(AbstractDungeon.player.currentHealth.toFloat() * 0.9f)
            }
        }

        fun MasoStartDeck(): ArrayList<String>
        {
            val startingDeck = ArrayList<String>()
            for (x in 0..4)
            {
                startingDeck.add(Kiss.ID)
            }
            for (x in 0..3)
            {
                startingDeck.add(Invite_Maso.ID)
            }
            //        startingDeck.add(Masturbate.ID);
            startingDeck.add(Spark.ID)
            startingDeck.add(FistIn.ID)
            return startingDeck
        }
    }
}