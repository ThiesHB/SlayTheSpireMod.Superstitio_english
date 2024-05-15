package superstitio.cards.maso;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.Logger;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.SuperstitioCard;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

import java.util.Arrays;

import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_CARD;

public abstract class MasoCard extends SuperstitioCard implements CardOwnerPlayerManager.IsMasoCard {
    private boolean isDelayRemoveDelayHpLoseBlock;
    private boolean isRemoveDelayHpLoseBlock;

    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public MasoCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public MasoCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget,
                    String imgSubFolder) {
        super(id, cardType, cost, cardRarity, cardTarget, MASO_CARD, imgSubFolder);
    }


    @Override
    protected void setupBlock(int amount, int amountOfAutoUpgrade, AbstractBlockModifier... blockModifiers) {
        super.setupBlock(amount, amountOfAutoUpgrade, blockModifiers);
        if (Arrays.stream(blockModifiers).anyMatch(block -> block instanceof DelayRemoveDelayHpLoseBlock))
            isDelayRemoveDelayHpLoseBlock = true;
        if (Arrays.stream(blockModifiers).anyMatch(block -> block instanceof RemoveDelayHpLoseBlock))
            isRemoveDelayHpLoseBlock = true;
    }

    @Override
    public void addToBot_gainBlock(int amount) {
        if (isRemoveDelayHpLoseBlock && isDelayRemoveDelayHpLoseBlock)
            Logger.warning("Maso: Do not use 'addToBot_gainBlock(int amount)' when setup this two block type.");

        if (isRemoveDelayHpLoseBlock) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
            return;
        }
        if (isDelayRemoveDelayHpLoseBlock) {
            addToBot_applyPower(new DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount));
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
            return;
        }
        super.addToBot_gainBlock(amount);
    }

    @Override
    public void addToBot_gainBlock(int amount, AbstractBlockModifier blockModifier) {
        if (blockModifier instanceof DelayRemoveDelayHpLoseBlock) {
            addToBot_applyPower(new DelayRemoveDelayHpLosePower(AbstractDungeon.player, amount));
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
            return;
        }
        if (blockModifier instanceof RemoveDelayHpLoseBlock) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
            return;
        }
        super.addToBot_gainBlock(amount, blockModifier);
    }
}
