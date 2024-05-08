package superstitio.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.BiConsumer;
import superstitio.Logger;
import superstitio.cards.DamageActionMaker;
import superstitio.utils.ActionUtility;
import superstitio.utils.ActionUtility.FunctionReturnSelfType;
import superstitio.utils.CardUtility;

import java.util.Optional;


public abstract class CardOrb_CardTrigger extends CardOrb {

    protected final BiConsumer<CardOrb_CardTrigger, AbstractCard> action;
    //    protected List<Soul> souls = new ArrayList<>();
    public AbstractCreature lastTarget;

    public CardOrb_CardTrigger(AbstractCard card, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard) {
        super(card);
        this.action = action_thisCard_targetCard;
    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return DamageActionMaker.getMonsterOrFirstMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }

    public static Optional<AbstractMonster> getHoveredMonster() {
        AbstractMonster hoveredMonster = ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster");
        if (hoveredMonster == null)
            return Optional.empty();
        return Optional.of(hoveredMonster);
    }

    @Override
    protected void onRemoveCard() {
//        switch (targetType) {
//            case ENEMY:
//            case ALL_ENEMY:
//                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
//                break;
//            case SELF:
//                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
//                break;
//            case NONE:
//            case ALL:
//            case SELF_AND_ENEMY:
//            default:
//                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
//                break;
//        }

    }

    @Override
    public abstract boolean shouldRemove();

    @Override
    public void update() {
        super.update();
//        this.souls.forEach(Soul::update);
        this.movingType = this.movingType.get();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
//        this.souls.forEach(soul -> soul.render(spriteBatch));
    }

    protected ActionUtility.VoidSupplier checkAndSetTheHoverType() {
        switch (targetType) {
            case ENEMY:
            case ALL_ENEMY:
                return this::State_WhenHoverCard_OnMonster;
            case SELF:
                return this::State_WhenHoverCard_OnSelf;
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                return this::State_WhenHoverCard_OnNothing;
        }
    }

    protected FunctionReturnSelfType State_Idle() {
        showPassiveNum();
        updateAnimationIdle();
        if (ifHoveredRightCard()) {
            return this::State_WhenHoverCard;
        }
        return this::State_Idle;
    }

    protected FunctionReturnSelfType State_WhenHoverCard() {
        showEvokeNum();
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        checkAndSetTheHoverType().get();
        this.drawOrder = DrawOrder.middle;
        if (!ifHoveredRightCard()) {
            return this::State_Idle;
        }
        return this::State_WhenHoverCard;
    }

    protected FunctionReturnSelfType State_Moving() {
        showPassiveNum();
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        this.drawOrder = DrawOrder.top;
        if (checkShouldStopMoving()) {
            return this::State_Idle;
        }
        return this::State_Moving;
    }

    protected void State_WhenHoverCard_OnMonster() {
        updateIfFocusOnMonster();
        this.card.glowColor = CardUtility.getColorFormCard(card);
    }

    protected void State_WhenHoverCard_OnSelf() {
        updateIfFocusOnSelf();
        this.card.glowColor = CardUtility.getColorFormCard(card);
    }

    protected void State_WhenHoverCard_OnNothing() {
        updateIfFocusOnNothing();
    }

    public CardOrb setTargetType(AbstractCard.CardTarget cardTarget) {
        this.targetType = cardTarget;
        return this;
    }

    //    public void makeSoul(Soul soul) {
//        soul.card = card;
//        soul.group = AbstractDungeon.player.drawPile;
//        if (!visualOnly) {
//            if (randomSpot) {
//                this.group.addToRandomSpot(card);
//            } else {
//                this.group.addToTop(card);
//            }
//        }
//
//        soul. = new Vector2(card.current_x, card.current_y);
//        soul.target = new Vector2(DRAW_PILE_X, DRAW_PILE_Y);
//        soul.setSharedVariables();
//        soul.rotation = card.angle + 270.0F;
//        soul.rotateClockwise = true;
//    }

    public void StartHitCreature(AbstractCreature target) {
        this.movingType = this::State_Moving;
        AbstractCreature creature = DamageActionMaker.getTargetOrFirstMonster(target);
        this.tryMoveTo(new Vector2(this.cX - (this.cX - creature.hb.cX) / 1.2f, this.cY - (this.cY - creature.hb.cY) / 1.2f));
        this.card.superFlash(CardUtility.getColorFormCard(card));

//        Soul soul = new Soul();
//        this.souls.add(soul);
        setCanNotInterrupt();
    }

    protected boolean ifHoveredRightCard() {
        return (boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")
                && (AbstractDungeon.player.hoveredCard != null) && this.cardMatcher.test(AbstractDungeon.player.hoveredCard);
    }

    protected void updateIfFocusOnMonster() {
        AbstractCreature target = CardOrb_CardTrigger.getHoveredMonster().orElse(null);
        if (!ActionUtility.isAlive(target)) return;
        this.lastTarget = target;
        Logger.temp("choose" + lastTarget);
        this.tryMoveTo(new Vector2(this.cX - (this.cX - target.hb.cX) / 3, this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnSelf() {
        if (!ActionUtility.isAlive(AbstractDungeon.player)) return;
        this.lastTarget = AbstractDungeon.player;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnNothing() {
        this.tryMoveTo(new Vector2(this.cX, this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()));
    }
}
