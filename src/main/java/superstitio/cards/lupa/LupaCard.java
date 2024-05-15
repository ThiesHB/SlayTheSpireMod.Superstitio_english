package superstitio.cards.lupa;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

import java.util.Arrays;

import static superstitio.cards.CardOwnerPlayerManager.IsLupaCard;

public abstract class LupaCard extends SuperstitioCard implements IsLupaCard {
    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String imgSubFolder) {
        this(id, cardType, cost, cardRarity, cardTarget, DataManager.SPTT_DATA.LupaEnums.LUPA_CARD, imgSubFolder);
    }

    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                    String imgSubFolder) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, imgSubFolder);
    }

    @Override
    protected final void setupBlock(int amount, int amountOfAutoUpgrade, AbstractBlockModifier... blockModifiers) {
        super.setupBlock(amount, amountOfAutoUpgrade, blockModifiers);
        if (blockModifiers == null || blockModifiers.length == 0) return;
        if (Arrays.stream(blockModifiers).anyMatch(blockModifier -> blockModifier instanceof RemoveDelayHpLoseBlock)) {
            DelayHpLosePatch.GainBlockTypeFields.ifTransGainBlockToReduceDelayHpLose.set(this, true);
        }
    }

    @Override
    public final void addToBot_gainBlock(int amount) {
        if (DelayHpLosePatch.GainBlockTypeFields.ifTransGainBlockToReduceDelayHpLose.get(this)) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
        }
        else
            super.addToBot_gainBlock(amount);
    }
}
