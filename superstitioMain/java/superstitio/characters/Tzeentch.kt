package superstitio.characters

import basemod.BaseMod
import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.localization.CharacterStrings
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.screens.CharSelectInfo
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA.*
import superstitio.Logger
import superstitio.characters.cardpool.CardPoolManager
import superstitio.characters.cardpool.LupaCardPool
import superstitio.characters.cardpool.MasoCardPool
import superstitio.relics.a_starter.StartWithSexToy
import superstitio.relics.a_starter.VulnerableTogetherRelic
import superstitio.relics.blight.*
import superstitioapi.cardPool.BaseCardPool
import superstitioapi.player.PlayerInitPostDungeonInitialize
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.renderManager.characterSelectScreenRender.RelicSelectionUI
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect
import java.io.IOException
import java.io.Serializable
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

//
class Tzeentch(name: String) : BaseCharacter(ID, name, TzeentchEnums.TZEENTCH_Character),
    PlayerInitPostDungeonInitialize, RenderInCharacterSelect
{
    private fun refreshInit()
    {
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
            LUPA_CHARACTER,  // 人物图片
            LUPA_CHARACTER_SHOULDER_2,
            LUPA_CHARACTER_SHOULDER_1,
            LUPA_CORPSE_IMAGE,  // 人物死亡图像
            loadout,
            0.0f,
            0.0f,
            250.0f,
            375.0f,  // 人物碰撞箱大小，越大的人物模型这个越大
            EnergyManager(3) // 初始每回合的能量
        )
    }

    // 初始遗物
    override fun getStartingRelics(): ArrayList<String>
    {
        val retVal = ArrayList<String>()
        //        retVal.add(StartWithSexToy.ID);
//        retVal.add(JokeDescription.ID);
//        retVal.add(DevaBody_Lupa.ID);
//        retVal.add(Sensitive.ID);
//        retVal.add(SorM.ID);
        return retVal
    }

    override fun getLoadout(): CharSelectInfo
    {
        return CharSelectInfo(
            TezeentchCharacterStrings.NAMES[0],  // 人物名字
            TezeentchCharacterStrings.TEXT[0],  // 人物介绍
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
        if (Lupa::class.java.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.selectRelic))) return Lupa.LupaStartDeck()
        if (Maso::class.java.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.selectRelic))) return Maso.MasoStartDeck()
        return Lupa.LupaStartDeck()
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    override fun getCardColor(): CardColor
    {
        return MasoEnums.MASO_CARD
    }

    override fun getCardPool(tmpPool: ArrayList<AbstractCard>): ArrayList<AbstractCard>
    {
        addCardByCardFilter(tmpPool)
        checkAndFillErrorCardPool(tmpPool)
        return tmpPool
    }

    override fun newInstance(): AbstractPlayer
    {
        return Tzeentch(this.name)
    }

    override fun getAscensionMaxHPLoss(): Int
    {
        return 5
    }

    override fun initPostDungeonInitialize()
    {
        TzeentchSave.saveConfig()
        STARTER_RELIC_Selection_UI.selectRelic.makeCopy()
            .instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size, false)
        InfoBlight.addAsInfoBlight(JokeDescription())
        InfoBlight.addAsInfoBlight(DEVABODY_RELIC_Selection_UI.selectRelic)
        InfoBlight.addAsInfoBlight(SEXUAL_HEAT_RELIC_Selection_UI.selectRelic)
        CardPoolManager.instance.cardPools.forEach(Consumer { baseCardPool: BaseCardPool? ->
            if (baseCardPool is LupaCardPool) InfoBlight.addAsInfoBlight(SemenMagician())
            if (baseCardPool is MasoCardPool) InfoBlight.addAsInfoBlight(EnjoyAilment())
        })
        if (Maso::class.java.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.selectRelic)))
        {
            Maso.setUpMaso()
        }
    }

    override fun doCharSelectScreenSelectEffect()
    {
        super.doCharSelectScreenSelectEffect()
        isCharacterInfoChanged = true
    }

    override fun renderInCharacterSelectScreen(characterOption: CharacterOption, sb: SpriteBatch)
    {
        STARTER_RELIC_Selection_UI.render(sb)
        DEVABODY_RELIC_Selection_UI.render(sb)
        SEXUAL_HEAT_RELIC_Selection_UI.render(sb)
        CardPoolManager.instance.render(sb)
    }

    override fun updateInCharacterSelectScreen(characterOption: CharacterOption)
    {
        updateIsUnableByGuroSetting(
            CardPoolManager.instance.cardPools.stream().anyMatch(
                Predicate { baseCardPool: BaseCardPool -> baseCardPool is MasoCardPool && baseCardPool.isSelect })
        )
        CardPoolManager.instance.update()
        STARTER_RELIC_Selection_UI.update()
        DEVABODY_RELIC_Selection_UI.update()
        SEXUAL_HEAT_RELIC_Selection_UI.update()
        //        if (InputHelper.justClickedLeft || InputHelper.justClickedRight)
//            Tzeentch.isCharacterInfoChanged = true;
        if (isCharacterInfoChanged)
        {
            isCharacterInfoChanged = false
            refreshInit()
            ReflectionHacks.setPrivate(characterOption, CharacterOption::class.java, "hp", characterInfo.makeHpString())
            ReflectionHacks.setPrivate(characterOption, CharacterOption::class.java, "gold", gold)
            if (Lupa::class.java.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.selectRelic))) CardCrawlGame.mainMenuScreen.charSelectScreen.bgCharImg =
                ImageMaster.loadImage(
                    BaseMod.playerPortraitMap[LupaEnums.LUPA_Character]
                )
            if (Maso::class.java.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.selectRelic))) CardCrawlGame.mainMenuScreen.charSelectScreen.bgCharImg =
                ImageMaster.loadImage(
                    BaseMod.playerPortraitMap[MasoEnums.MASO_Character]
                )
            TzeentchSave.saveConfig()
        }
    }

    //    public static RelicSelect relicSelect;
    override fun isCardCanAdd(card: AbstractCard?): Boolean
    {
        return CardPoolManager.instance.getAddedCard()
            .test(card) && !CardPoolManager.instance.getAddedCard().test(card)
    }

    class TzeentchSave(
        var starterRelicId: String,
        var devaBodyRelicId: String,
        var sexualHeatRelicId: String,
        var cardPoolId_IsSelect: HashMap<String, Boolean>
    ) : Serializable
    {
        companion object
        {
            private const val TzeentchSave_STRING = "TzeentchSave"
            var config: SpireConfig? = null
            var theDefaultSettings: Properties = Properties()
            var saveFileGson: Gson = Gson()
            fun loadConfig()
            {
                theDefaultSettings.setProperty(TzeentchSave_STRING, "")
                try
                {
                    config = SpireConfig(
                        DataManager.getModID() + TzeentchSave::class.java.simpleName,
                        DataManager.getModID() + TzeentchSave::class.java.simpleName + "Config",
                        theDefaultSettings
                    )
                    config!!.load()
                    val tzeentchString = config!!.getString(TzeentchSave_STRING)
                    val tzeentchSave = saveFileGson.fromJson(tzeentchString, TzeentchSave::class.java)
                    onLoad(tzeentchSave)
                }
                catch (e: Exception)
                {
                    Logger.error(e)
                }
            }

            fun saveConfig()
            {
                val tzeentchString = saveFileGson.toJsonTree(onSave(), TzeentchSave::class.java).toString()
                config!!.setString(TzeentchSave_STRING, tzeentchString)
                try
                {
                    config!!.save()
                }
                catch (e: IOException)
                {
                    Logger.error(e)
                }
            }

            private fun onSaveRaw(): JsonElement
            {
                return saveFileGson.toJsonTree(onSave())
            }

            private fun onLoadRaw(value: JsonElement?)
            {
                if (value != null)
                {
                    val parsed = saveFileGson.fromJson(value, TzeentchSave::class.java)
                    onLoad(parsed)
                }
                else
                {
                    onLoad(null)
                }
            }

            private fun onSave(): TzeentchSave
            {
                val cardPoolData = HashMap<String, Boolean>()
                CardPoolManager.instance.cardPools.forEach(Consumer { cardPool: BaseCardPool ->
                    cardPoolData[cardPool.id] = cardPool.isSelect
                })

                return TzeentchSave(
                    STARTER_RELIC_Selection_UI.selectRelic.relicId,
                    DEVABODY_RELIC_Selection_UI.selectRelic.relicId,
                    SEXUAL_HEAT_RELIC_Selection_UI.selectRelic.relicId,
                    cardPoolData
                )
            }

            private fun onLoad(tzeentchSave: TzeentchSave?)
            {
                if (tzeentchSave == null) return
                STARTER_RELIC_Selection_UI.setSelectRelic(tzeentchSave.starterRelicId)
                DEVABODY_RELIC_Selection_UI.setSelectRelic(tzeentchSave.devaBodyRelicId)
                SEXUAL_HEAT_RELIC_Selection_UI.setSelectRelic(tzeentchSave.sexualHeatRelicId)
                tzeentchSave.cardPoolId_IsSelect.forEach { (cardPoolId: String, isSelect: Boolean) ->
                    CardPoolManager.instance.cardPools.forEach(
                        Consumer { cardPool: BaseCardPool ->
                            if (cardPool.id == cardPoolId) cardPool.isSelect = isSelect
                        })
                }
            }
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Tzeentch::class.java.simpleName)
        val TezeentchCharacterStrings: CharacterStrings = CardCrawlGame.languagePack.getCharacterString(ID)
        val Relic_Selection_Y: Float = Settings.HEIGHT.toFloat() / 2 - 100 * Settings.yScale
        var STARTER_RELIC_Selection_UI: RelicSelectionUI
        var DEVABODY_RELIC_Selection_UI: RelicSelectionUI
        var SEXUAL_HEAT_RELIC_Selection_UI: RelicSelectionUI
        val Relic_Selection_X: Float = 240 * Settings.xScale
        var characterInfo: CharacterSelectInfo = CharacterSelectInfo(
            65,  // 当前血量
            65,  // 最大血量
            99 // 初始携带金币
        )
        var isCharacterInfoChanged: Boolean = false

        init
        {
            STARTER_RELIC_Selection_UI = RelicSelectionUI(
                Relic_Selection_X,
                Relic_Selection_Y,
                Arrays.stream(arrayOf<AbstractRelic>(StartWithSexToy(), VulnerableTogetherRelic()))
                    .collect(Collectors.toList()),
                PowerTip(TezeentchCharacterStrings.TEXT[2], TezeentchCharacterStrings.TEXT[3])
            )
                .setRefreshAfterSelect { relic: AbstractRelic ->
                    if (Lupa::class.java.isAssignableFrom(getOwnerFromRelic(relic)))
                    {
                        characterInfo.gold = Lupa.characterInfo.gold
                        isCharacterInfoChanged = true
                    }
                    if (Maso::class.java.isAssignableFrom(getOwnerFromRelic(relic)))
                    {
                        characterInfo.gold = Maso.characterInfo.gold
                        isCharacterInfoChanged = true
                    }
                }

            DEVABODY_RELIC_Selection_UI = RelicSelectionUI(
                Relic_Selection_X + STARTER_RELIC_Selection_UI.totalWidth,
                Relic_Selection_Y,
                Arrays.stream(arrayOf<AbstractRelic>(DevaBody_Lupa(), DevaBody_Masochism()))
                    .collect(Collectors.toList()),
                PowerTip(TezeentchCharacterStrings.TEXT[4], TezeentchCharacterStrings.TEXT[5])
            )
                .setRefreshAfterSelect { relic: AbstractRelic ->
                    if (Lupa::class.java.isAssignableFrom(getOwnerFromRelic(relic)))
                    {
                        characterInfo.currentHp = Lupa.characterInfo.currentHp
                        characterInfo.maxHp = Lupa.characterInfo.maxHp
                        isCharacterInfoChanged = true
                    }
                    if (Maso::class.java.isAssignableFrom(getOwnerFromRelic(relic)))
                    {
                        characterInfo.currentHp = Maso.characterInfo.currentHp
                        characterInfo.maxHp = Maso.characterInfo.maxHp
                        isCharacterInfoChanged = true
                    }
                }

            SEXUAL_HEAT_RELIC_Selection_UI = RelicSelectionUI(
                Relic_Selection_X + STARTER_RELIC_Selection_UI.totalWidth + DEVABODY_RELIC_Selection_UI.totalWidth,
                Relic_Selection_Y,
                Arrays.stream(arrayOf<AbstractRelic>(Sensitive(), MasochismMode())).collect(Collectors.toList()),
                PowerTip(TezeentchCharacterStrings.TEXT[6], TezeentchCharacterStrings.TEXT[7])
            )

            TzeentchSave.loadConfig()
        }

        private fun getOwnerFromRelic(relic: AbstractRelic): Class<out BaseCharacter?>
        {
            if (relic is DevaBody_Lupa || relic is Sensitive || relic is StartWithSexToy)
            {
                return Lupa::class.java
            }
            if (relic is DevaBody_Masochism || relic is MasochismMode || relic is VulnerableTogetherRelic)
            {
                return Maso::class.java
            }
            return Lupa::class.java
        }
    }
}