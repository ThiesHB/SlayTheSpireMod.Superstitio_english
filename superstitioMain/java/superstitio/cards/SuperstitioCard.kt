package superstitio.cards

import basemod.abstracts.CustomCard
import basemod.helpers.TooltipInfo
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet
import com.megacrit.cardcrawl.helpers.input.InputActionSet
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect
import superstitio.DataManager
import superstitio.Logger
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.CardStringsWillMakeFlavorSet
import superstitio.delayHpLose.*
import superstitioapi.DataUtility
import superstitioapi.cards.DamageActionMaker
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.UpdateDescriptionAdvanced
import superstitioapi.utils.getFormattedDescription
import java.util.*
import java.util.stream.Collectors

abstract class SuperstitioCard(
    id: String,
    cardType: CardType,
    cost: Int,
    cardRarity: CardRarity,
    cardTarget: CardTarget,
    cardColor: CardColor,
    imgSubFolder: String
) : CustomCard(
    id, getCardStringsWithSFWAndFlavor(id).getNAME(), CardOwnerPlayerManager.getImgPath(imgSubFolder, id), cost,
    getCardStringsWithSFWAndFlavor(id).getDESCRIPTION(), cardType, cardColor, cardRarity, cardTarget
), UpdateDescriptionAdvanced {

    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    var cardStrings: CardStringsWillMakeFlavorSet = getCardStringsWithSFWAndFlavor(id)
    private var damageAutoUpgrade = 0
    private var blockAutoUpgrade = 0
    private var magicAutoUpgrade = 0

    override var descriptionArgs: Array<out Any>? = null

    init {
        Logger.debug("loadCard$id")
        val flavor = cardStrings.getFLAVOR()
        flavor.let { string: String? -> FlavorText.AbstractCardFlavorFields.flavor[this] = string }
        FlavorText.AbstractCardFlavorFields.textColor[this] = Color.PINK.cpy()
        FlavorText.AbstractCardFlavorFields.flavorBoxType[this] = FlavorText.boxType.TRADITIONAL
        updateRawDescription()
        initializeDescription()
    }

    override fun initializeDescription() {
        try {
            super.initializeDescription()
        } catch (e: Exception) {
            Logger.error(e)
        }
    }

    fun updateRawDescription() {
        try {
            this.rawDescription = getFormattedDescription()
        } catch (e: Exception) {
            Logger.error(e)
            this.rawDescription = ""
        }
    }

    fun upgradeCardsToPreview() {
        if (this.cardsToPreview == null) return
        val nameUnUpgrade = cardsToPreview.name
        cardsToPreview.upgrade()
        if (rawDescription.contains(nameUnUpgrade)) {
            this.rawDescription = rawDescription.replace(nameUnUpgrade, cardsToPreview.name)
        }
    }

    //    public String getEXTENDED_DESCRIPTION(int index) {
    //        return cardStrings.getEXTENDED_DESCRIPTION(index);
    //    }
    abstract fun upgradeAuto()

    open fun addToBot_gainCustomBlock(amount: Int, blockModifier: AbstractBlockModifier) {
        this.addToBot(GainCustomBlockAction(BlockModContainer(this, blockModifier), AbstractDungeon.player, amount))
    }

    open fun calculateCardDamageForSelfOrEnemyTargeting(): AbstractCreature? {
        if (CustomTargeting.targetingMap[target] !is SelfOrEnemyTargeting) {
            applyPowers()
            return null
        }
        val selfOrEnemyTargeting = CustomTargeting.targetingMap[target] as SelfOrEnemyTargeting
        updateSelfOrEnemyTargetingTargetHovered(AbstractDungeon.player, selfOrEnemyTargeting)
        val target = selfOrEnemyTargeting.hovered
        if (target == null) {
            applyPowers()
            return null
        }

        if (target is AbstractMonster) {
            super.calculateCardDamage(target)
            return target
        }
        this.applyPowersToBlock()
        val player = AbstractDungeon.player
        this.isDamageModified = false
        if (!this.isMultiDamage) {
            calculateSingleDamage(player, target)
        } else {
            calculateMultipleDamage(player)
        }
        //        selfOrEnemyTargeting.clearHovered();
        return target
    }

    protected fun setCostToCostMap_ForTurn(amount: Int) {
        if (InBattleDataManager.costMap.containsKey(this.uuid)) {
            InBattleDataManager.costMap[uuid] = amount
        }
        this.setCostForTurn(amount)
    }

    protected fun setCostToCostMap_ForBattle(amount: Int) {
        if (InBattleDataManager.costMap.containsKey(this.uuid)) {
            InBattleDataManager.costMap[uuid] = amount
        }
        this.updateCost(amount)
    }

    fun addToBot_dealDamage(target: AbstractCreature?) {
        DamageActionMaker.maker(this.damage, target!!).addToBot()
    }

    fun addToBot_dealDamage(target: AbstractCreature?, effect: AttackEffect) {
        DamageActionMaker.maker(this.damage, target!!).setEffect(effect).addToBot()
    }

    fun addToBot_dealDamage(target: AbstractCreature?, damageAmount: Int, effect: AttackEffect) {
        DamageActionMaker.maker(damageAmount, target!!).setEffect(effect).addToBot()
    }

    fun addToBot_dealDamage(
        target: AbstractCreature?, damageAmount: Int, damageType: DamageType,
        effect: AttackEffect
    ) {
        DamageActionMaker.maker(damageAmount, target!!).setDamageType(damageType).setEffect(effect).addToBot()
    }

    fun addToBot_dealDamage(
        target: AbstractCreature?, damageAmount: Int, damageType: DamageType,
        damageModifier: AbstractDamageModifier, effect: AttackEffect
    ) {
        DamageActionMaker.maker(damageAmount, target!!).setDamageModifier(this, damageModifier)
            .setDamageType(damageType)
            .setEffect(effect).addToBot()
    }

    fun addToBot_dealDamageToAllEnemies(effect: AttackEffect) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name)
            return
        }
        DamageActionMaker.makeAoeDamage(this)
            .setEffect(effect)
            .addToBot()
    }

    fun addToBot_dealDamageToAllEnemies(effect: AttackEffect, vararg damageModifiers: AbstractDamageModifier) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name)
            return
        }
        DamageActionMaker.makeAoeDamage(this)
            .setEffect(effect)
            .setDamageModifier(this, *damageModifiers)
            .addToBot()
    }

    //    public final void addToBot_dealDamageToAllEnemies(final AttackEffect effect,
    //                                                      Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker,
    //                                                      AbstractDamageModifier... damageModifiers) {
    //        if (!this.isMultiDamage) {
    //            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name);
    //            return;
    //        }
    //        DamageActionMaker.makeAoeDamage(this)
    //                .setEffect(effect)
    //                .setDamageModifier(this, damageModifiers)
    //                .addToBot();
    //    }
    @JvmOverloads
    fun addToBot_gainBlock(amount: Int = this.block) {
        addToBot_gainBlock(this, amount)
    }

    fun addToBot_gainCustomBlock(blockModifier: AbstractBlockModifier) {
        this.addToBot_gainCustomBlock(this.block, blockModifier)
    }

    fun addToBot_drawCards(amount: Int) {
        this.addToBot(DrawCardAction(amount))
    }

    fun addToBot_drawCards() {
        this.addToBot(DrawCardAction(1))
    }

    fun addToBot_applyPower(power: AbstractPower) {
        ActionUtility.addToBot_applyPower(power)
    }

    fun addToBot_reducePower(powerID: String, amount: Int) {
        ActionUtility.addToBot_reducePower(powerID, amount, AbstractDungeon.player, AbstractDungeon.player)
    }

    fun addToBot_removeSpecificPower(power: AbstractPower) {
        ActionUtility.addToBot_removeSpecificPower(power, AbstractDungeon.player)
    }

    protected fun setupDamage(amount: Int, vararg damageModifiers: AbstractDamageModifier) {
        this.setupDamage(amount, 0, *damageModifiers)
    }

    protected fun setupDamage(amount: Int, amountOfAutoUpgrade: Int, vararg damageModifiers: AbstractDamageModifier) {
        this.baseDamage = amount
        this.damage = amount
        this.damageAutoUpgrade = amountOfAutoUpgrade
        if (damageModifiers.isEmpty()) return
        DamageModifierManager.addModifiers(this, Arrays.stream(damageModifiers).collect(Collectors.toList()))
    }

    protected fun setupBlock(amount: Int, vararg blockModifiers: AbstractBlockModifier) {
        setupBlock(amount, 0, *blockModifiers)
    }

    protected fun setupBlock(amount: Int, amountOfAutoUpgrade: Int, vararg blockModifiers: AbstractBlockModifier) {
        setupBlock(this, amount, amountOfAutoUpgrade, *blockModifiers)
    }

    protected fun setupMagicNumber(amount: Int, amountOfAutoUpgrade: Int = 0) {
        this.baseMagicNumber = amount
        this.magicNumber = amount
        this.magicAutoUpgrade = amountOfAutoUpgrade
    }

    protected fun calculateMultipleDamage(player: AbstractPlayer) {
        val monsters = AbstractDungeon.getCurrRoom().monsters.monsters
        val tmpDamages = FloatArray(monsters.size)
        Arrays.fill(tmpDamages, baseDamage.toFloat())
        this.multiDamage = IntArray(tmpDamages.size)
        for (i in tmpDamages.indices) {
            var tmpDamage = tmpDamages[i]
            if (monsters[i].isDying || monsters[i].isEscaping) continue
            tmpDamage = getTmpDamage(player, tmpDamage, monsters[i])
            multiDamage[i] = MathUtils.floor(tmpDamage)
        }
        this.damage = multiDamage[0]
    }

    //    protected final void addToBot_reducePower(final AbstractPower power) {
    //        ActionUtility.addToBot_reducePower(power, AbstractDungeon.player);
    //    }
    protected fun getTmpDamage(player: AbstractPlayer, baseDamage: Float, creature: AbstractCreature): Float {
        var tempDamage = baseDamage
        for (r2 in player.relics) {
            tempDamage = r2.atDamageModify(tempDamage, this)
            if (this.baseDamage != tempDamage.toInt()) {
                this.isDamageModified = true
            }
        }
        for (p2 in player.powers) {
            tempDamage = p2.atDamageGive(tempDamage, this.damageTypeForTurn, this)
        }
        tempDamage = player.stance.atDamageGive(tempDamage, this.damageTypeForTurn, this)
        if (this.baseDamage != tempDamage.toInt()) {
            this.isDamageModified = true
        }
        for (p2 in creature.powers) {
            tempDamage = p2.atDamageReceive(tempDamage, this.damageTypeForTurn, this)
        }
        for (p2 in player.powers) {
            tempDamage = p2.atDamageFinalGive(tempDamage, this.damageTypeForTurn, this)
        }
        for (p2 in creature.powers) {
            tempDamage = p2.atDamageFinalReceive(tempDamage, this.damageTypeForTurn, this)
        }
        if (tempDamage < 0.0f) {
            tempDamage = 0.0f
        }
        if (this.baseDamage != MathUtils.floor(tempDamage)) {
            this.isDamageModified = true
        }
        return tempDamage
    }

    protected fun calculateSingleDamage(player: AbstractPlayer, creature: AbstractCreature) {
        var tmpDamage = baseDamage.toFloat()
        tmpDamage = getTmpDamage(player, tmpDamage, creature)
        this.damage = MathUtils.floor(tmpDamage)
    }

    /**
     * 使用后别忘了updateRawDescription
     */
    override fun updateDescriptionArgs() {
    }

    override fun getDescriptionStrings(): String {
        if (this.upgraded && cardStrings.getUPGRADE_DESCRIPTION()
                .isNotEmpty()
        )
            return cardStrings.getUPGRADE_DESCRIPTION()
        return cardStrings.getDESCRIPTION()
    }


    override fun upgrade() {
        if (!this.upgraded) {
            this.upgradeName()
            if (this.blockAutoUpgrade != 0) this.upgradeBlock(this.blockAutoUpgrade)
            if (this.damageAutoUpgrade != 0) this.upgradeDamage(this.damageAutoUpgrade)
            if (this.magicAutoUpgrade != 0) this.upgradeMagicNumber(this.magicAutoUpgrade)
            this.updateRawDescription()
            this.upgradeAuto()
            this.initializeDescription()
        }
    }

    abstract override fun use(player: AbstractPlayer?, monster: AbstractMonster?)

    override fun hover() {
        val temp = this.drawScale
        super.hover()
        this.drawScale = temp
    }

    override fun getCustomTooltips(): ArrayList<TooltipInfo> {
        val list: ArrayList<TooltipInfo> = ArrayList()
        cardStrings.needAddKeywords?.forEach { superstitioKeyWord ->
            val tooltipInfo = TooltipInfo(superstitioKeyWord.findPROPER_NAME(), superstitioKeyWord.findDESCRIPTION())
            list.add(tooltipInfo)
        }
        return list
    }

    override fun getCustomTooltipsTop(): ArrayList<TooltipInfo> {
        return ArrayList()
    }

    companion object {
        private val DESC_LINE_WIDTH = 418.0f * Settings.scale
        fun getCardStringsWithSFWAndFlavor(cardId: String): CardStringsWillMakeFlavorSet {
            val cardStringsSet = StringSetUtility.getCustomStringsWithSFW(
                cardId,
                DataManager.cards,
                CardStringsWillMakeFlavorSet::class.java
            )
            if (cardStringsSet.getNAME() != CardStrings.getMockCardString().NAME) return cardStringsSet
            return StringSetUtility.getCustomStringsWithSFW(
                "${DataManager.getModID()}:${DataUtility.getIdOnly(cardId)}",
                DataManager.cards, CardStringsWillMakeFlavorSet::class.java
            )
        }

        fun setupBlock(
            card: SuperstitioCard,
            amount: Int,
            amountOfAutoUpgrade: Int,
            vararg blockModifiers: AbstractBlockModifier
        ) {
            card.baseBlock = amount
            card.block = amount
            card.blockAutoUpgrade = amountOfAutoUpgrade
            if (blockModifiers.isEmpty()) return
            BlockModifierManager.addModifiers(
                card,
                Arrays.stream(blockModifiers).collect(Collectors.toList()) as ArrayList<AbstractBlockModifier>
            )
            if (Arrays.stream(blockModifiers)
                    .anyMatch { block: AbstractBlockModifier -> block is DelayRemoveDelayHpLoseBlock }
            ) GainBlockTypeFields.ifDelayReduceDelayHpLose[card] = true
            if (Arrays.stream(blockModifiers)
                    .anyMatch { block: AbstractBlockModifier -> block is RemoveDelayHpLoseBlock }
            ) GainBlockTypeFields.ifReduceDelayHpLose[card] = true
        }

        fun addToBot_gainBlock(card: SuperstitioCard?, amount: Int) {
            if (GainBlockTypeFields.ifReduceDelayHpLose[card]!! && GainBlockTypeFields.ifDelayReduceDelayHpLose[card]!!) Logger.warning(
                "Do not use 'addToBot_gainBlock(int amount)' when setup this two block type."
            )

            if (GainBlockTypeFields.ifReduceDelayHpLose[card]!!) {
                DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true)
                AbstractDungeon.effectList.add(
                    FlashAtkImgEffect(
                        AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                        AttackEffect.SHIELD
                    )
                )
                return
            }
            if (GainBlockTypeFields.ifDelayReduceDelayHpLose[card]!!) {
                ActionUtility.addToBot_applyPower(DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount))
                AbstractDungeon.effectList.add(
                    FlashAtkImgEffect(
                        AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                        AttackEffect.SHIELD
                    )
                )
                return
            }
            AbstractDungeon.actionManager.addToBottom(GainBlockAction(AbstractDungeon.player, amount))
        }

        fun updateSelfOrEnemyTargetingTargetHovered(player: AbstractPlayer, targeting: SelfOrEnemyTargeting) {
            if (player.isInKeyboardMode) {
                if (!InputActionSet.releaseCard.isJustPressed && !CInputActionSet.cancel.isJustPressed) {
                    targeting.updateKeyboardTarget()
                } else {
                    val cardFromHotkey = player.hoveredCard
                    player.inSingleTargetMode = false
                    player.toHover = cardFromHotkey
                    if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
                        player.toHover = null
                    }

                    if (cardFromHotkey != null && !player.inspectMode) {
                        Gdx.input.setCursorPosition(
                            cardFromHotkey.hb.cX.toInt(),
                            (Settings.HEIGHT.toFloat() - AbstractPlayer.HOVER_CARD_Y_POSITION).toInt()
                        )
                    }
                }
            } else {
                targeting.updateHovered()
            }
        }

        @JvmStatic
        protected fun CardTypeToString(t: CardType?): String {
            val type = when (t) {
                CardType.ATTACK -> {
                    "attack"
                }

                CardType.POWER -> {
                    "power"
                }

                CardType.CURSE -> {
                    "curse"
                }

                CardType.SKILL -> {
                    "skill"
                }

                else -> {
                    "special"
                }
            }
            return type
        }

        @JvmStatic
        protected fun sumAllDelayHpLosePower(): Int {
            if (ActionUtility.isNotInBattle) return 0
            return AbstractDungeon.player.powers
                .filterIsInstance<DelayHpLosePower>()
                .sumOf(AbstractPower::amount)
        }
    }


}
