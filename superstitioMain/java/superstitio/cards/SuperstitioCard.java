package superstitio.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.Logger;
import superstitio.customStrings.CardStringsWithFlavorSet;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.DataUtility;
import superstitioapi.actions.DamageAllEnemiesAction;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.updateDescriptionAdvanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import static superstitio.DataManager.cards;
import static superstitio.DataManager.getModID;
import static superstitio.cards.CardOwnerPlayerManager.getImgPath;
import static superstitio.customStrings.HasSFWVersion.getCustomStringsWithSFW;
import static superstitio.delayHpLose.DelayHpLosePatch.GainBlockTypeFields.ifDelayReduceDelayHpLose;
import static superstitio.delayHpLose.DelayHpLosePatch.GainBlockTypeFields.ifReduceDelayHpLose;

public abstract class SuperstitioCard extends CustomCard implements updateDescriptionAdvanced {
    private final static float DESC_LINE_WIDTH = 418.0f * Settings.scale;
    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    public final CardStringsWithFlavorSet cardStrings;
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
        FlavorText.AbstractCardFlavorFields.flavor.set(this, this.cardStrings.getFLAVOR());
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.PINK.cpy());
        FlavorText.AbstractCardFlavorFields.flavorBoxType.set(this, FlavorText.boxType.TRADITIONAL);
        updateRawDescription();
        initializeDescription();
    }

    public static CardStringsWithFlavorSet getCardStringsWithSFWAndFlavor(String cardId) {
        CardStringsWithFlavorSet cardStringsSet = getCustomStringsWithSFW(cardId, cards, CardStringsWithFlavorSet.class);
        if (cardStringsSet != null && !Objects.equals(cardStringsSet.getNAME(), CardStrings.getMockCardString().NAME))
            return cardStringsSet;
        return getCustomStringsWithSFW(getModID() + ":" + DataUtility.getIdOnly(cardId), cards, CardStringsWithFlavorSet.class);
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

    public String makeFormatDESCRIPTION() {
        this.updateDescriptionArgs();
        if (descriptionArgs == null) return getDescriptionStrings();
        return String.format(getDescriptionStrings(), descriptionArgs);
    }

    @Override
    public void setDescriptionArgs(Object... args) {
        if (args[0] instanceof Object[])
            descriptionArgs = (Object[]) args[0];
        else
            descriptionArgs = args;
    }

    public void updateRawDescription() {
        if (cardStrings != null)
            this.rawDescription = makeFormatDESCRIPTION();
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

    public void upgradeCardsToPreview() {
        if (this.cardsToPreview == null) return;
        String nameUnUpgrade = cardsToPreview.name;
        cardsToPreview.upgrade();
        if (this.rawDescription.contains(nameUnUpgrade)) {
            this.rawDescription = this.rawDescription.replace(nameUnUpgrade, cardsToPreview.name);
        }
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

    public String[] getEXTENDED_DESCRIPTION() {
        return cardStrings.getEXTENDED_DESCRIPTION();
    }

    public abstract void upgradeAuto();

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

    @Override
    public void hover() {
        float temp = this.drawScale;
        super.hover();
        this.drawScale = temp;
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
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, DamageType.NORMAL, effect));
//        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, DamageType.NORMAL, effect));
    }

    public final void addToBot_dealDamageToAllEnemies(final AttackEffect effect, AbstractDamageModifier... damageModifiers) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name);
            return;
        }
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, DamageType.NORMAL, effect, this, damageModifiers));
    }

    public final void addToBot_dealDamageToAllEnemies(final AttackEffect effect,
                                                      Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker,
                                                      AbstractDamageModifier... damageModifiers) {
        if (!this.isMultiDamage) {
            Logger.warning("错误，未设置isMultiDamage，可能会出错，所以群体攻击自动禁用，错误卡片：" + this.name);
            return;
        }
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, DamageType.NORMAL, effect,
                DamageAllEnemiesAction.makeNewInfoWithModifier(this, AbstractDungeon.player, DamageType.NORMAL, damageModifiers))
                .setNewAttackEffectMaker(newAttackEffectMaker)
        );
    }

    public final void addToBot_gainBlock() {
        this.addToBot_gainBlock(this.block);
    }

    public final void addToBot_gainBlock(final int amount) {
        addToBot_gainBlock(this, amount);
    }

    public final void addToBot_gainCustomBlock(AbstractBlockModifier blockModifier) {
        this.addToBot_gainCustomBlock(this.block, blockModifier);
    }

    public void addToBot_gainCustomBlock(final int amount, AbstractBlockModifier blockModifier) {
        this.addToBot(new GainCustomBlockAction(new BlockModContainer(this, blockModifier), AbstractDungeon.player, amount));
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

//    protected final void addToBot_reducePower(final AbstractPower power) {
//        ActionUtility.addToBot_reducePower(power, AbstractDungeon.player);
//    }

    public final void addToBot_reducePower(final String powerID, int amount) {
        ActionUtility.addToBot_reducePower(powerID, amount, AbstractDungeon.player, AbstractDungeon.player);
    }

    public final void addToBot_removeSpecificPower(final AbstractPower power) {
        ActionUtility.addToBot_removeSpecificPower(power, AbstractDungeon.player);
    }

    protected void setCostToCostMap_ForTurn(int amount) {
        if (superstitioapi.InBattleDataManager.costMap.containsKey(this.uuid)) {
            superstitioapi.InBattleDataManager.costMap.put(this.uuid, amount);
        }
        this.setCostForTurn(amount);
    }

    protected void setCostToCostMap_ForBattle(int amount) {
        if (superstitioapi.InBattleDataManager.costMap.containsKey(this.uuid)) {
            superstitioapi.InBattleDataManager.costMap.put(this.uuid, amount);
        }
        this.updateCost(amount);
    }

    @Override
    public abstract void use(AbstractPlayer player, AbstractMonster monster);


}
