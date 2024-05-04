package SuperstitioMod.cards.Lupa;

import SuperstitioMod.DataManager;
import SuperstitioMod.InBattleDataManager;
import SuperstitioMod.Logger;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.DamageActionMaker;
import SuperstitioMod.customStrings.CardStringsWithSFWAndFlavor;
import SuperstitioMod.customStrings.HasSFWVersion;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

public abstract class AbstractLupaCard extends CustomCard {
    private final static float DESC_LINE_WIDTH = 418.0f * Settings.scale;
    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    protected CardStringsWithSFWAndFlavor cardStrings;

    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractLupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));

    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public AbstractLupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardType) {
        this(id, cardType, cost, cardRarity, cardTarget, SuperstitioModSetup.LupaEnums.LUPA_CARD, customCardType);
    }

    public AbstractLupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractLupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                            String customCardType) {
        super(
                id,
                getCardStringsWithSFWAndFlavor(id).getNAME(),
                getImgPath(customCardType, id),
                cost,
                getCardStringsWithSFWAndFlavor(id).getDESCRIPTION(),
                cardType,
                cardColor,
                cardRarity,
                cardTarget);
        Logger.debug("loadCard" + id);
        this.cardStrings = getCardStringsWithSFWAndFlavor(id);
        FlavorText.AbstractCardFlavorFields.flavor.set(this, this.cardStrings.getFLAVOR());
//        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.DARK_GRAY.cpy());
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.PINK.cpy());
        FlavorText.AbstractCardFlavorFields.flavorBoxType.set(this, FlavorText.boxType.TRADITIONAL);
    }

    public static CardStringsWithSFWAndFlavor getCardStringsWithSFWAndFlavor(String cardName) {
        try {
            return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.cards, CardStringsWithSFWAndFlavor.class);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String getImgPath(final String tag, final String id) {
        return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_LupaCard, tag, id);
    }

    protected static String CardTypeToString(final AbstractCard.CardType t) {
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

    public void upgradeCardsToPreview() {
        if (this.cardsToPreview != null)
            cardsToPreview.upgrade();
    }


    @Override
    public final void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (cardStrings.getUPGRADE_DESCRIPTION() != null && !cardStrings.getUPGRADE_DESCRIPTION().isEmpty())
                this.rawDescription = cardStrings.getUPGRADE_DESCRIPTION();
            this.upgradeAuto();
            this.initializeDescription();
        }
    }

    public String[] getEXTENDED_DESCRIPTION() {
        return cardStrings.getEXTENDED_DESCRIPTION();
    }

    public abstract void upgradeAuto();

    protected final void setupDamage(final int amount) {
        this.baseDamage = amount;
        this.damage = amount;
    }

    protected final void setupBlock(final int amount) {
        this.baseBlock = amount;
        this.block = amount;
    }

    protected final void setupMagicNumber(final int amount) {
        this.baseMagicNumber = amount;
        this.magicNumber = amount;
    }

    protected final void addToBot_dealDamage(final AbstractCreature target) {
        DamageActionMaker.make(this.damage, target).addToBot();
    }

    protected final void addToBot_dealDamage(final AbstractCreature target, final AttackEffect effect) {
        DamageActionMaker.make(this.damage, target).setupEffect(effect).addToBot();
    }

    protected final void addToBot_dealDamage(final AbstractCreature target, final int damageAmount, final AttackEffect effect) {
        DamageActionMaker.make(damageAmount, target).setupEffect(effect).addToBot();
    }

    protected final void addToBot_dealDamage(final AbstractCreature target, final int damageAmount, final DamageType damageType,
                                             final AttackEffect effect) {
        DamageActionMaker.make(damageAmount, target).setupDamageType(damageType).setupEffect(effect).addToBot();
    }

    protected final void addToBot_dealDamageToAllEnemies(final AttackEffect effect) {
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect));
    }

    protected final void addToBot_gainBlock() {
        addToBot_gainBlock(this.block);
    }

    protected final void addToBot_gainBlock(final int amount) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, amount));
    }

    protected final void addToBot_drawCards(final int amount) {
        this.addToBot(new DrawCardAction(amount));
    }

    protected final void addToBot_drawCards() {
        this.addToBot(new DrawCardAction(1));
    }

    protected final void addToBot_applyPower(final AbstractPower power) {
        this.addToBot(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

    protected final void addToBot_reducePowerToPlayer(final String powerID, int amount) {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, powerID, amount));
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

    @Override
    public abstract void use(AbstractPlayer player, AbstractMonster monster);

    public enum BattleCardPlace {
        Hand,
        DrawPile,
        Discard;

        BattleCardPlace() {
        }
    }

}
