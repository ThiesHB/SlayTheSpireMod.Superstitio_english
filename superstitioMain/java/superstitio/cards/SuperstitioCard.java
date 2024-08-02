package superstitio.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.Logger;
import superstitio.customStrings.SuperstitioKeyWord;
import superstitio.customStrings.stringsSet.CardStringsWillMakeFlavorSet;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.DataUtility;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.renderManager.inBattleManager.InBattleDataManager;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.UpdateDescriptionAdvanced;

import java.util.*;
import java.util.stream.Collectors;

import static com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting.targetingMap;
import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import static superstitio.DataManager.cards;
import static superstitio.DataManager.getModID;
import static superstitio.cards.CardOwnerPlayerManager.getImgPath;
import static superstitio.customStrings.interFace.StringSetUtility.getCustomStringsWithSFW;
import static superstitio.delayHpLose.DelayHpLosePatch.GainBlockTypeFields.ifDelayReduceDelayHpLose;
import static superstitio.delayHpLose.DelayHpLosePatch.GainBlockTypeFields.ifReduceDelayHpLose;

public abstract class SuperstitioCard extends CustomCard implements UpdateDescriptionAdvanced {
    private final static float DESC_LINE_WIDTH = 418.0f * Settings.scale;
    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    public final CardStringsWillMakeFlavorSet cardStrings;
    private int damageAutoUpgrade = 0;
    private int blockAutoUpgrade = 0;
    private int magicAutoUpgrade = 0;
    private Object[] descriptionArgs;

    public SuperstitioCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                           String imgSubFolder) {
        super(id, getCardStringsWithSFWAndFlavor(id).getNAME(), getImgPath(imgSubFolder, id), cost,
                getCardStringsWithSFWAndFlavor(id).getDESCRIPTION(), cardType, cardColor, cardRarity, cardTarget);
        Logger.debug("loadCard" + id);
        this.cardStrings = getCardStringsWithSFWAndFlavor(id);
        Optional<String> flavor = this.cardStrings.getFLAVOR();
        flavor.ifPresent(string -> FlavorText.AbstractCardFlavorFields.flavor.set(this, string));
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.PINK.cpy());
        FlavorText.AbstractCardFlavorFields.flavorBoxType.set(this, FlavorText.boxType.TRADITIONAL);
        updateRawDescription();
        initializeDescription();
    }

    public static CardStringsWillMakeFlavorSet getCardStringsWithSFWAndFlavor(String cardId) {
        CardStringsWillMakeFlavorSet cardStringsSet = getCustomStringsWithSFW(cardId, cards, CardStringsWillMakeFlavorSet.class);
        if (cardStringsSet != null && !Objects.equals(cardStringsSet.getNAME(), CardStrings.getMockCardString().NAME))
            return cardStringsSet;
        return getCustomStringsWithSFW(getModID() + ":" + DataUtility.getIdOnly(cardId), cards, CardStringsWillMakeFlavorSet.class);
    }

    public static void setupBlock(SuperstitioCard card, int amount, int amountOfAutoUpgrade, AbstractBlockModifier... blockModifiers) {
        card.baseBlock = amount;
        card.block = amount;
        card.blockAutoUpgrade = amountOfAutoUpgrade;
        if (blockModifiers == null || blockModifiers.length == 0) return;
        BlockModifierManager.addModifiers(card, (ArrayList<AbstractBlockModifier>)
                Arrays.stream(blockModifiers).collect(Collectors.toList()));
        if (Arrays.stream(blockModifiers).anyMatch(block -> block instanceof DelayRemoveDelayHpLoseBlock))
            ifDelayReduceDelayHpLose.set(card, true);
        if (Arrays.stream(blockModifiers).anyMatch(block -> block instanceof RemoveDelayHpLoseBlock))
            ifReduceDelayHpLose.set(card, true);
    }

    public static void addToBot_gainBlock(SuperstitioCard card, int amount) {
        if (ifReduceDelayHpLose.get(card) && ifDelayReduceDelayHpLose.get(card))
            Logger.warning("Do not use 'addToBot_gainBlock(int amount)' when setup this two block type.");

        if (ifReduceDelayHpLose.get(card)) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AttackEffect.SHIELD));
            return;
        }
        if (ifDelayReduceDelayHpLose.get(card)) {
            ActionUtility.addToBot_applyPower(new DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount));
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AttackEffect.SHIELD));
            return;
        }
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, amount));
    }

    public static void updateSelfOrEnemyTargetingTargetHovered(AbstractPlayer player, SelfOrEnemyTargeting targeting) {
        if (player.isInKeyboardMode) {
            if (!InputActionSet.releaseCard.isJustPressed() && !CInputActionSet.cancel.isJustPressed()) {
                targeting.updateKeyboardTarget();
            } else {
                AbstractCard cardFromHotkey = player.hoveredCard;
                player.inSingleTargetMode = false;
                player.toHover = cardFromHotkey;
                if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
                    player.toHover = null;
                }

                if (cardFromHotkey != null && !player.inspectMode) {
                    Gdx.input.setCursorPosition((int) cardFromHotkey.hb.cX, (int) ((float) Settings.HEIGHT - AbstractPlayer.HOVER_CARD_Y_POSITION));
                }
            }
        } else {
            targeting.updateHovered();
        }
    }

    protected static String CardTypeToString(final CardType t) {
        String type;
        switch (t) {
            case ATTACK: {
                type = "attack";
                break;
            }
            case POWER: {
                type = "power";
                break;
            }
            case CURSE: {
                type = "curse";
                break;
            }
            case SKILL: {
                type = "skill";
                break;
            }
            default: {
                type = "special";
                break;
            }
        }
        return type;
    }

    protected static int sumAllDelayHpLosePower() {
        if (ActionUtility.isNotInBattle()) return 0;
        return AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof DelayHpLosePower)
                .mapToInt(power -> power.amount).sum();
    }

    public String makeFormatDESCRIPTION() {
        this.updateDescriptionArgs();
        if (descriptionArgs == null)
            return getDescriptionStrings();
        return String.format(getDescriptionStrings(), descriptionArgs);
    }

    public void updateRawDescription() {
        if (cardStrings != null)
            this.rawDescription = makeFormatDESCRIPTION();
    }

    public void upgradeCardsToPreview() {
        if (this.cardsToPreview == null) return;
        String nameUnUpgrade = cardsToPreview.name;
        cardsToPreview.upgrade();
        if (this.rawDescription.contains(nameUnUpgrade)) {
            this.rawDescription = this.rawDescription.replace(nameUnUpgrade, cardsToPreview.name);
        }
    }

    public String[] getEXTENDED_DESCRIPTION() {
        return cardStrings.getEXTENDED_DESCRIPTION();
    }

    public abstract void upgradeAuto();

    public void addToBot_gainCustomBlock(final int amount, AbstractBlockModifier blockModifier) {
        this.addToBot(new GainCustomBlockAction(new BlockModContainer(this, blockModifier), AbstractDungeon.player, amount));
    }

    public AbstractCreature calculateCardDamageForSelfOrEnemyTargeting() {
        if (!(targetingMap.get(this.target) instanceof SelfOrEnemyTargeting)) {
            applyPowers();
            return null;
        }
        SelfOrEnemyTargeting selfOrEnemyTargeting = (SelfOrEnemyTargeting) targetingMap.get(this.target);
        updateSelfOrEnemyTargetingTargetHovered(AbstractDungeon.player, selfOrEnemyTargeting);
        AbstractCreature target = selfOrEnemyTargeting.getHovered();
        if (target == null) {
            applyPowers();
            return null;
        }

        if (target instanceof AbstractMonster) {
            super.calculateCardDamage((AbstractMonster) target);
            return target;
        }
        this.applyPowersToBlock();
        final AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (!this.isMultiDamage) {
            calculateSingleDamage(player, target);
        } else {
            calculateMultipleDamage(player);
        }
//        selfOrEnemyTargeting.clearHovered();
        return target;
    }

    protected void setCostToCostMap_ForTurn(int amount) {
        if (InBattleDataManager.costMap.containsKey(this.uuid)) {
            InBattleDataManager.costMap.put(this.uuid, amount);
        }
        this.setCostForTurn(amount);
    }

    protected void setCostToCostMap_ForBattle(int amount) {
        if (InBattleDataManager.costMap.containsKey(this.uuid)) {
            InBattleDataManager.costMap.put(this.uuid, amount);
        }
        this.updateCost(amount);
    }

    public final void addToBot_dealDamage(final AbstractCreature target) {
        DamageActionMaker.maker(this.damage, target).addToBot();
    }

    public final void addToBot_dealDamage(final AbstractCreature target, final AttackEffect effect) {
        DamageActionMaker.maker(this.damage, target).setEffect(effect).addToBot();
    }

    public final void addToBot_dealDamage(final AbstractCreature target, final int damageAmount, final AttackEffect effect) {
        DamageActionMaker.maker(damageAmount, target).setEffect(effect).addToBot();
    }

    public final void addToBot_dealDamage(final AbstractCreature target, final int damageAmount, final DamageType damageType,
                                          final AttackEffect effect) {
        DamageActionMaker.maker(damageAmount, target).setDamageType(damageType).setEffect(effect).addToBot();
    }

    public final void addToBot_dealDamage(final AbstractCreature target, final int damageAmount, final DamageType damageType,
                                          AbstractDamageModifier damageModifier, final AttackEffect effect) {
        DamageActionMaker.maker(damageAmount, target).setDamageModifier(this, damageModifier).setDamageType(damageType).setEffect(effect).addToBot();
    }

    public final void addToBot_dealDamageToAllEnemies(final AttackEffect effect) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name);
            return;
        }
        DamageActionMaker.makeAoeDamage(this)
                .setEffect(effect)
                .addToBot();
    }

    public final void addToBot_dealDamageToAllEnemies(final AttackEffect effect, AbstractDamageModifier... damageModifiers) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name);
            return;
        }
        DamageActionMaker.makeAoeDamage(this)
                .setEffect(effect)
                .setDamageModifier(this, damageModifiers)
                .addToBot();
    }

    public final void addToBot_gainBlock() {
        this.addToBot_gainBlock(this.block);
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

    public final void addToBot_gainBlock(final int amount) {
        addToBot_gainBlock(this, amount);
    }

    public final void addToBot_gainCustomBlock(AbstractBlockModifier blockModifier) {
        this.addToBot_gainCustomBlock(this.block, blockModifier);
    }

    public final void addToBot_drawCards(final int amount) {
        this.addToBot(new DrawCardAction(amount));
    }

    public final void addToBot_drawCards() {
        this.addToBot(new DrawCardAction(1));
    }

    public final void addToBot_applyPower(final AbstractPower power) {
        ActionUtility.addToBot_applyPower(power);
    }

    public final void addToBot_reducePower(final String powerID, int amount) {
        ActionUtility.addToBot_reducePower(powerID, amount, AbstractDungeon.player, AbstractDungeon.player);
    }

    public final void addToBot_removeSpecificPower(final AbstractPower power) {
        ActionUtility.addToBot_removeSpecificPower(power, AbstractDungeon.player);
    }

    protected final void setupDamage(final int amount, AbstractDamageModifier... damageModifiers) {
        this.setupDamage(amount, 0, damageModifiers);

    }

    protected final void setupDamage(final int amount, int amountOfAutoUpgrade, AbstractDamageModifier... damageModifiers) {
        this.baseDamage = amount;
        this.damage = amount;
        this.damageAutoUpgrade = amountOfAutoUpgrade;
        if (damageModifiers == null || damageModifiers.length == 0) return;
        DamageModifierManager.addModifiers(this, Arrays.stream(damageModifiers).collect(Collectors.toList()));

    }

    protected final void setupBlock(final int amount, AbstractBlockModifier... blockModifiers) {
        setupBlock(amount, 0, blockModifiers);
    }

    protected final void setupBlock(final int amount, int amountOfAutoUpgrade, AbstractBlockModifier... blockModifiers) {
        setupBlock(this, amount, amountOfAutoUpgrade, blockModifiers);
    }

    protected final void setupMagicNumber(final int amount) {
        this.setupMagicNumber(amount, 0);
    }

    protected final void setupMagicNumber(final int amount, int amountOfAutoUpgrade) {
        this.baseMagicNumber = amount;
        this.magicNumber = amount;
        this.magicAutoUpgrade = amountOfAutoUpgrade;
    }

    protected final void calculateMultipleDamage(AbstractPlayer player) {
        final ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        final float[] tmpDamages = new float[monsters.size()];
        Arrays.fill(tmpDamages, (float) this.baseDamage);
        this.multiDamage = new int[tmpDamages.length];
        for (int i = 0; i < tmpDamages.length; ++i) {
            float tmpDamage = tmpDamages[i];
            if (monsters.get(i).isDying || monsters.get(i).isEscaping) continue;
            tmpDamage = getTmpDamage(player, tmpDamage, monsters.get(i));
            this.multiDamage[i] = MathUtils.floor(tmpDamage);
        }
        this.damage = this.multiDamage[0];
    }

//    protected final void addToBot_reducePower(final AbstractPower power) {
//        ActionUtility.addToBot_reducePower(power, AbstractDungeon.player);
//    }

    protected final float getTmpDamage(AbstractPlayer player, float baseDamage, AbstractCreature creature) {
        float tempDamage = baseDamage;
        for (final AbstractRelic r2 : player.relics) {
            tempDamage = r2.atDamageModify(tempDamage, this);
            if (this.baseDamage != (int) tempDamage) {
                this.isDamageModified = true;
            }
        }
        for (final AbstractPower p2 : player.powers) {
            tempDamage = p2.atDamageGive(tempDamage, this.damageTypeForTurn, this);
        }
        tempDamage = player.stance.atDamageGive(tempDamage, this.damageTypeForTurn, this);
        if (this.baseDamage != (int) tempDamage) {
            this.isDamageModified = true;
        }
        for (final AbstractPower p2 : creature.powers) {
            tempDamage = p2.atDamageReceive(tempDamage, this.damageTypeForTurn, this);
        }
        for (final AbstractPower p2 : player.powers) {
            tempDamage = p2.atDamageFinalGive(tempDamage, this.damageTypeForTurn, this);
        }
        for (final AbstractPower p2 : creature.powers) {
            tempDamage = p2.atDamageFinalReceive(tempDamage, this.damageTypeForTurn, this);
        }
        if (tempDamage < 0.0f) {
            tempDamage = 0.0f;
        }
        if (this.baseDamage != MathUtils.floor(tempDamage)) {
            this.isDamageModified = true;
        }
        return tempDamage;
    }

    protected final void calculateSingleDamage(AbstractPlayer player, AbstractCreature creature) {
        float tmpDamage = (float) this.baseDamage;
        tmpDamage = getTmpDamage(player, tmpDamage, creature);
        this.damage = MathUtils.floor(tmpDamage);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (ActionUtility.isNotInBattle()) return;
        try {
            initializeDescription();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public void initializeDescription() {
        if (!ActionUtility.isNotInBattle()) {
            try {
                updateRawDescription();
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        super.initializeDescription();
    }

    /**
     * 使用后别忘了updateRawDescription
     */
    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public String getDescriptionStrings() {
        if (this.upgraded && cardStrings.getUPGRADE_DESCRIPTION() != null && !cardStrings.getUPGRADE_DESCRIPTION().isEmpty())
            return cardStrings.getUPGRADE_DESCRIPTION();
        return cardStrings.getDESCRIPTION();
    }

    @Override
    public Object[] getDescriptionArgs() {
        return descriptionArgs;
    }

    @Override
    public void setDescriptionArgs(Object... args) {
        setDescriptionToArgs(allArgs -> descriptionArgs = allArgs, args);
    }

    @Override
    public final void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (this.blockAutoUpgrade != 0)
                this.upgradeBlock(this.blockAutoUpgrade);
            if (this.damageAutoUpgrade != 0)
                this.upgradeDamage(this.damageAutoUpgrade);
            if (this.magicAutoUpgrade != 0)
                this.upgradeMagicNumber(this.magicAutoUpgrade);
            updateRawDescription();
            this.upgradeAuto();
            this.initializeDescription();
        }
    }

    @Override
    public abstract void use(AbstractPlayer player, AbstractMonster monster);

    @Override
    public void hover() {
        float temp = this.drawScale;
        super.hover();
        this.drawScale = temp;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> list = new ArrayList<>();
        for (SuperstitioKeyWord superstitioKeyWord : cardStrings.getNeedAddKeywords()) {
            TooltipInfo tooltipInfo = new TooltipInfo(superstitioKeyWord.getPROPER_NAME(), superstitioKeyWord.getDESCRIPTION());
            list.add(tooltipInfo);
        }
        return list;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        return new ArrayList<>();
    }

    public interface HasMultiCardsToPreview {

        float SHOW_TIME = 1.0f;

        List<AbstractCard> getMultiCardsToPreview();

        float getCardsToPreviewTimer();

        void setCardsToPreviewTimer(float cardsToPreviewTimer);

        void setCardsToPreview(AbstractCard cardsToPreview);

        default void update() {
            if (getMultiCardsToPreview().isEmpty()) return;
            float timer = getCardsToPreviewTimer();
            if (timer < 0) {
                setCardsToPreviewTimer(getMultiCardsToPreview().size() * SHOW_TIME);
            }
            int index = MathUtils.floor(timer / SHOW_TIME);
            if (index < 0)
                index = 0;
            if (index > getMultiCardsToPreview().size() - 1)
                index = getMultiCardsToPreview().size() - 1;
            AbstractCard newCardsToPreview = getMultiCardsToPreview().get(index);
            setCardsToPreview(newCardsToPreview);

            setCardsToPreviewTimer(getCardsToPreviewTimer() - Gdx.graphics.getDeltaTime());
        }
    }
}
