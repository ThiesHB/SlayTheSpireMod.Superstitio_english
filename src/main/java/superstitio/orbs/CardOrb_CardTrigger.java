package superstitio.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import org.apache.logging.log4j.util.BiConsumer;
import superstitio.cards.DamageActionMaker;
import superstitio.utils.ActionUtility;

public abstract class CardOrb_CardTrigger extends CardOrb {
    protected final BiConsumer<CardOrb_CardTrigger, AbstractCard> action;
    public CardOrb_CardTrigger(AbstractCard card, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard) {
        super(card);
        this.action = action_thisCard_targetCard;
    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return DamageActionMaker.getMonsterOrFirstMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }



//    public CardOrb_CardTrigger setCardTargetType(AbstractCard.CardTarget cardTargetType) {
//        this.cardTargetType = cardTargetType;
//        return this;
//    }



    @Override
    protected void onRemoveCard() {
        AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
    }

    @Override
    public abstract boolean shouldRemove();

    @Override
    public void update() {
        super.update();
        if (movingType != CardOrb.CardOrbMovingType.Idle)
            this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        checkMovingType();

    }

    @Override
    protected void checkMovingType() {
        if (movingType != CardOrb.CardOrbMovingType.Idle && canNotInterrupt || AbstractDungeon.player.hoveredCard == null) {
            if (movingIsStop) {
                this.movingType = CardOrb.CardOrbMovingType.Idle;
                this.canNotInterrupt = false;
            }
            card.stopGlowing();
            card.costForTurn = passiveAmount;
            card.isCostModified = false;
            return;
        }
        card.costForTurn = evokeAmount;
        card.isCostModified = true;
        card.beginGlowing();
        switch (card.target) {
            case ENEMY:
            case ALL_ENEMY:
                updateIfFocusOnMonster();
                break;
            case SELF:
                updateIfFocusOnSelf();
                break;
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                updateIfFocusOnNothing();
        }
    }

    private void updateIfFocusOnSelf() {
        if (this.movingType == CardOrbMovingType.focusOnSelf) return;
        if (!ActionUtility.isAlive(AbstractDungeon.player)) return;
        this.movingType = CardOrbMovingType.focusOnSelf;
        this.tryMoveTo(new Vector2(
                this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()));
    }

    private void updateIfFocusOnNothing() {
        if (this.movingType == CardOrbMovingType.focusOnNothing) return;
        if (!ActionUtility.isAlive(AbstractDungeon.player)) return;
        this.movingType = CardOrbMovingType.focusOnNothing;
        this.tryMoveTo(new Vector2(
                this.cX,
                this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()));
    }

    private void updateIfFocusOnMonster() {
        if (this.movingType == CardOrbMovingType.focusOnMonster) return;
        AbstractCreature target =
                getHoveredMonsterSafe();
        if (!ActionUtility.isAlive(target)) return;
        this.movingType = CardOrbMovingType.focusOnMonster;
        this.tryMoveTo(new Vector2(
                this.cX - (this.cX - target.hb.cX) / 3,
                this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()));
    }

    public void StartHitCreature(AbstractCreature target) {
        this.movingType = CardOrbMovingType.tryHitCreature;
        AbstractCreature creature = DamageActionMaker.getTargetOrFirstMonster(target);
        this.tryMoveTo(new Vector2(
                this.cX - (this.cX - creature.hb.cX) / 1.2f,
                this.cY - (this.cY - creature.hb.cY) / 1.2f));
        setCanNotInterrupt();
    }


    protected static class CardOrbMovingType {
        @SpireEnum
        public static CardOrb.CardOrbMovingType focusOnMonster;
        @SpireEnum
        public static CardOrb.CardOrbMovingType tryHitCreature;
        @SpireEnum
        public static CardOrb.CardOrbMovingType focusOnSelf;
        @SpireEnum
        public static CardOrb.CardOrbMovingType focusOnNothing;
    }
}
