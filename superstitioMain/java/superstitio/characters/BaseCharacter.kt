package superstitio.characters

import basemod.ReflectionHacks
import basemod.abstracts.CustomPlayer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.cutscenes.CutscenePanel
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.city.Vampires
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.ScreenShake
import com.megacrit.cardcrawl.localization.CharacterStrings
import com.megacrit.cardcrawl.localization.UIStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.unlock.UnlockTracker
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA
import superstitio.DataManager.SPTT_DATA.GeneralEnums
import superstitio.Logger
import superstitio.SuperstitioConfig
import superstitio.cards.general.BaseCard.Kiss
import superstitio.cards.general.TempCard.NullCard
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.TipsUtility

// 继承CustomPlayer类
abstract class BaseCharacter(ID: String, name: String, playerClass: PlayerClass) :
    CustomPlayer(name, playerClass, EnergyBall_TEXTURES, EnergyBall_VFX_Path, LAYER_SPEED, null, null)
{
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    protected val characterStrings: CharacterStrings? = CardCrawlGame.languagePack.getCharacterString(ID)
    private var offsetX = 0f
    private var offsetY = 0f
    private var simpleAnim = 0.0f

    init
    {
        this.title = getTitle(playerClass)


        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 0.0f * Settings.scale)
        this.dialogY = (this.drawY + 150.0f * Settings.scale)


        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
            LUPA_CHARACTER,  // 人物图片
            LUPA_CHARACTER_SHOULDER_2, LUPA_CHARACTER_SHOULDER_1, LUPA_CORPSE_IMAGE,  // 人物死亡图像
            this.loadout, 0.0f, 0.0f, 250.0f, 375.0f,  // 人物碰撞箱大小，越大的人物模型这个越大
            EnergyManager(3) // 初始每回合的能量
        )

        if (!SuperstitioConfig.isEnableSFW())
        {
            this.setMoveOffset(0f, -hb.height / 3.0f)
        }

        // 如果你的人物没有动画，那么这些不需要写
        // this.loadAnimation(SuperstitioModSetup.getImgFilesPath()+"char/character.atlas", SuperstitioModSetup.getImgFilesPath()+"char/character
        // .json", 1.8F);
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
        // e.setTimeScale(1.2F);
    }

    fun setMoveOffset(x: Float, y: Float)
    {
        this.offsetX = x
        this.offsetY = y
    }

    val otherAddedCardColors: MutableList<CardColor?>
        get()
        {
            val cardColors = ArrayList<CardColor?>()
            cardColors.add(GeneralEnums.GENERAL_CARD)
            return cardColors
        }

    protected fun addCardByCardFilter(originCardPool: MutableList<AbstractCard>)
    {
        CardLibrary.cards.forEach { (string: String?, card: AbstractCard) ->
            if (UnlockTracker.isCardLocked(string) && !Settings.treatEverythingAsUnlocked())
                return@forEach
            if (card.rarity == CardRarity.BASIC)
                return@forEach
            if (originCardPool.contains(card))
                return@forEach
            if (isCardCanAdd(card))
            {
                originCardPool.add(card)
            }
        }
    }

    protected abstract fun isCardCanAdd(card: AbstractCard?): Boolean

    private fun drawImg(sb: SpriteBatch)
    {
        val scaleX = 1.0f
        val v = 0.005f * MathUtils.sinDeg(MathUtils.sinDeg(simpleAnim * 360) * 15)
        val scaleY = 1.0f + v
        val rotation = 0f
        sb.draw(
            this.img,
            this.drawX - img.width.toFloat() * Settings.scale / 2.0f + this.animX,
            this.drawY + hb.height * v,
            0f,
            0f,
            img.width.toFloat() * Settings.scale,
            img.height.toFloat() * Settings.scale,
            scaleX,
            scaleY,
            rotation,
            0,
            0,
            img.width,
            img.height,
            this.flipHorizontal,
            this.flipVertical
        )
    }

    override fun useCard(c: AbstractCard, monster: AbstractMonster?, energyOnUse: Int)
    {
        super.useCard(c, monster, energyOnUse)
    }

    override fun getCardPool(tmpPool: ArrayList<AbstractCard>): ArrayList<AbstractCard>
    {
        val originCardPool = super.getCardPool(tmpPool)
        addCardByCardFilter(originCardPool)
        checkAndFillErrorCardPool(originCardPool)
        return originCardPool
    }

    override fun update()
    {
        super.update()
        simpleAnim += Gdx.graphics.deltaTime
        if (simpleAnim >= 1.0f) simpleAnim = 0f
    }

    override fun render(sb: SpriteBatch)
    {
        sb.color = Color.WHITE
        if (AbstractDungeon.getCurrRoom() is RestRoom)
        {
            this.renderShoulderImg(sb)
            return
        }
        stance.render(sb)
        if (this.atlas == null || ReflectionHacks.getPrivate(this, AbstractPlayer::class.java, "renderCorpse"))
            this.drawImg(sb)
        else
            this.renderPlayerImage(sb)

        hb.render(sb)
        healthHb.render(sb)
        if (ActionUtility.isNotInBattle || this.isDead)
            return
        this.renderHealth(sb)

        this.orbs.forEach { it.render(sb) }
    }

    override fun movePosition(x: Float, y: Float)
    {
        super.movePosition(x + offsetX, y + offsetY)
    }

    // 人物名字（出现在游戏左上角）
    override fun getTitle(playerClass: PlayerClass): String
    {
        if (characterStrings == null) return "[MISSING_Title]"
        return characterStrings.NAMES[1]
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    override fun getStartCardForEvent(): AbstractCard
    {
        return Kiss()
    }

    // 卡牌轨迹颜色
    override fun getCardTrailColor(): Color
    {
        return SPTT_DATA.SEX_COLOR
    }

    // 高进阶带来的生命值损失
    override fun getAscensionMaxHPLoss(): Int
    {
        return 5
    }

    // 卡牌的能量字体，没必要修改
    override fun getEnergyNumFont(): BitmapFont
    {
        return FontHelper.energyNumFontBlue
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    override fun doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA("SELECT_WATCHER", MathUtils.random(-0.15f, 0.15f))
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false)
    }

    // 碎心图片
    override fun getCutscenePanels(): MutableList<CutscenePanel>
    {
        val panels = ArrayList<CutscenePanel>()
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(CutscenePanel(DataManager.makeImgFilesPath_Character("Victory1"), "ATTACK_MAGIC_FAST_1"))
        panels.add(CutscenePanel(DataManager.makeImgFilesPath_Character("Victory2")))
        panels.add(CutscenePanel(DataManager.makeImgFilesPath_Character("Victory3")))
        return panels
    }

    // 自定义模式选择你的人物时播放的音效
    override fun getCustomModeCharacterButtonSoundKey(): String
    {
        return "ATTACK_HEAVY"
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    override fun getLocalizedCharacterName(): String
    {
        return characterStrings!!.NAMES[0]
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    override fun getSpireHeartText(): String
    {
        return characterStrings!!.TEXT[1]
    }

    // 打心脏的颜色，不是很明显
    override fun getSlashAttackColor(): Color
    {
        return SPTT_DATA.SEX_COLOR
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    override fun getVampireText(): String
    {
        return Vampires.DESCRIPTIONS[1]
    }

    // 卡牌选择界面选择该牌的颜色
    override fun getCardRenderColor(): Color
    {
        return SPTT_DATA.SEX_COLOR
    }

    // 第三章面对心脏造成伤害时的特效
    override fun getSpireHeartSlashEffect(): Array<AttackEffect>
    {
        return arrayOf(
            AttackEffect.SLASH_HEAVY, AttackEffect.FIRE,
            AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_HEAVY, AttackEffect.FIRE,
            AttackEffect.SLASH_DIAGONAL
        )
    }

    override fun refreshHitboxLocation()
    {
        super.refreshHitboxLocation()
        hb.move(hb.cX - this.offsetX, hb.cY - this.offsetY)
        healthHb.move(
            hb.cX,
            hb.cY - (this.hb_h / 2.0f) - (healthHb.height / 2.0f)
        )
    }

    class CharacterSelectInfo(var currentHp: Int, var maxHp: Int, var gold: Int)
    {
        fun makeHpString(): String
        {
            return "$currentHp/$maxHp"
        } //        public void refreshCharacterSelectScreen(CharacterOption characterOption) {
        //
        //        }
    }

    companion object
    {
        // 人物立绘
        val LUPA_CHARACTER: String = DataManager.makeImgFilesPath_Character("character")

        // 火堆的人物立绘（行动前）
        val LUPA_CHARACTER_SHOULDER_1: String = DataManager.makeImgFilesPath_Character("shoulder1")

        // 火堆的人物立绘（行动后）
        val LUPA_CHARACTER_SHOULDER_2: String = DataManager.makeImgFilesPath_Character("shoulder2")

        // 人物死亡图像
        val LUPA_CORPSE_IMAGE: String = DataManager.makeImgFilesPath_Character("corpse")
        val GuroText: UIStrings = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("GuroText"))
        val GuroTip: PowerTip = PowerTip(GuroText.TEXT[0], GuroText.TEXT[1])
        private const val EnergyBall_Path = "EnergyBall_Lupa/"
        private val EnergyBall_VFX_Path: String = DataManager.makeImgFilesPath_UI(EnergyBall_Path + "vfx")

        // 战斗界面左下角能量图标的每个图层
        private val EnergyBall_TEXTURES = arrayOf(
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer5"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer4"),
            DataManager.makeImgFilesPath_UI(
                EnergyBall_Path + "layer3"
            ),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer2"),
            DataManager.makeImgFilesPath_UI(
                EnergyBall_Path + "layer1"
            ),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer6"),
            DataManager.makeImgFilesPath_UI(
                EnergyBall_Path + "layer5d"
            ),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer4d"),
            DataManager.makeImgFilesPath_UI(
                EnergyBall_Path + "layer3d"
            ),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer2d"),
            DataManager.makeImgFilesPath_UI(
                EnergyBall_Path + "layer1d"
            )
        )

        // 每个图层的旋转速度
        private val LAYER_SPEED = floatArrayOf(-40.0f, -32.0f, 20.0f, -20.0f, 0.0f, -10.0f, -8.0f, 5.0f, -5.0f, 0.0f)

        @JvmStatic
        protected fun unableByGuroSetting()
        {
            if (!SuperstitioConfig.isEnableGuroCharacter() && !SuperstitioConfig.isForcePlayGuroCharacter)
            {
                try
                {
                    CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isDisabled = true
                    if (CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isHovered)
                    {
                        TipsUtility.renderTipsWithMouse(GuroTip)
                    }
                }
                catch (e: Exception)
                {
                    Logger.warning("no confirmButton found")
                }
            }
        }

        @JvmStatic
        protected fun updateIsUnableByGuroSetting(moreCheck: Boolean)
        {
            if (moreCheck)
            {
                unableByGuroSetting()
            }
            else CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isDisabled = false
        }

        @JvmStatic
        protected fun checkAndFillErrorCardPool(originCardPool: MutableList<AbstractCard>)
        {
            if (NullCard.needMakeNullCardToFill(originCardPool))
            {
                originCardPool.clear()
                originCardPool.addAll(NullCard.makeNullCardPool())
            }
        }
    }
}