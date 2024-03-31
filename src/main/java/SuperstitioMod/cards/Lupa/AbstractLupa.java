package SuperstitioMod.cards.Lupa;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.characters.Lupa;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.lang.reflect.Field;
import java.util.Objects;

public abstract class AbstractLupa extends CustomCard {
    private final static float DESC_LINE_WIDTH = 418.0f * Settings.scale;

    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    protected CardStrings cardStrings;

    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractLupa(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public AbstractLupa(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardType) {
        super(
                id,
                CardCrawlGame.languagePack.getCardStrings(id).NAME,
                Objects.equals(customCardType, "default")
                        ? getImgPath("", "default")
                        : getImgPath(customCardType, SuperstitioModSetup.getIdOnly(id)),
                cost,
                CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION,
                cardType,
                Lupa.Enums.LUPA_CARD,
                cardRarity,
                cardTarget);

        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
    }

    public static String getImgPath(final String tag, final String id) {
        String path;
        if (Objects.equals(tag, "")) {
            path = SuperstitioModSetup.getImgFilesPath() + "cards_Lupa/" + id + ".png";
        } else {
            path = SuperstitioModSetup.getImgFilesPath() + "cards_Lupa/" + tag + "/" + id + ".png";
        }
        SuperstitioModSetup.logger.info("loadCardImg:" + path);
        return path;
    }

    public static String CardTypeToString(final AbstractCard.CardType t) {
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

    private static void renderQuote(final SpriteBatch sb, AbstractLupa card) {
        String flavorText = card.cardStrings.EXTENDED_DESCRIPTION[0];
        if (flavorText != null) {
            FontHelper.renderWrappedText(sb, FontHelper.SRV_quoteFont, flavorText, Settings.WIDTH / 2.0f,
                    Settings.HEIGHT / 2.0f - 430.0f * Settings.scale, DESC_LINE_WIDTH, Settings.CREAM_COLOR, 1.0f);
        } else {
            FontHelper.renderWrappedText(sb, FontHelper.SRV_quoteFont, "\"Missing quote...\"", Settings.WIDTH / 2.0f,
                    Settings.HEIGHT / 2.0f - 430.0f * Settings.scale, DESC_LINE_WIDTH, Settings.CREAM_COLOR, 1.0f);
        }
    }

    protected void setupDamage(final int amount) {
        this.baseDamage = amount;
        this.damage = amount;
    }

    protected void setupBlock(final int amount) {
        this.baseBlock = amount;
        this.block = amount;
    }

    protected void setupMagicNumber(final int amount) {
        this.baseMagicNumber = amount;
        this.magicNumber = amount;
    }

    public void damageToEnemy(final AbstractMonster monster, final AbstractGameAction.AttackEffect effect) {
        this.addToBot(new DamageAction(monster, new DamageInfo(AbstractDungeon.player, this.damage), effect));
    }

    public void damageToAllEnemies(final AbstractGameAction.AttackEffect effect) {
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect));
    }

    public void gainBlock() {
        gainBlock(this.block);
    }

    public void gainBlock(final int amount) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, amount));
    }

    public void drawCards(final int amount) {
        this.addToBot(new DrawCardAction(amount));
    }

    public void gainPowerToPlayer(final AbstractPower power) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardBack",
            paramtypez = {
                    SpriteBatch.class,
            }
    )
    public static class CardRenderPatch {
        @SpirePrefixPatch
        public static void Prefix(SingleCardViewPopup __instance, SpriteBatch sb) {
            Field privateField = null;
            try {
                privateField = SingleCardViewPopup.class.getDeclaredField("card");
            } catch (NoSuchFieldException e) {
                return;
            }
            privateField.setAccessible(true); // 允许访问私有字段
            AbstractCard fieldValue = null; // 获得私有字段值
            try {
                fieldValue = (AbstractCard) privateField.get(__instance);
            } catch (IllegalAccessException e) {
                return;
            }
            if (fieldValue instanceof AbstractLupa) {
                AbstractLupa lupa_card = (AbstractLupa) fieldValue;
                renderQuote(sb, lupa_card);
            }
        }
    }
}
