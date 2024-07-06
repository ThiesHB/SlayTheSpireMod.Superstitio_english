package superstitioapi.hangUpCard;

import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.OnPlayerTurnStartSubscriber;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.renderManager.inBattleManager.InBattleDataManager;
import superstitioapi.renderManager.inBattleManager.RenderInBattle;
import superstitioapi.utils.ActionUtility;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HangUpCardGroup implements RenderInBattle,
        OnCardUseSubscriber, OnPowersModifiedSubscriber, OnPlayerTurnStartSubscriber,
        SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber {
    private final Hitbox hitbox;
    private final ArrayList<CardOrb> cards = new ArrayList<>();
    private int remove_check_counter = 10;
    private CardOrb hoveredCard;

    public HangUpCardGroup(Hitbox hitbox) {
        this.hitbox = new Hitbox(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        RenderInBattle.Register(RenderType.Normal, this);
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width * 2);
        this.hitbox.moveY(this.hitbox.cY + this.hitbox.height * 0.5f);
    }

    public static ArrayList<AbstractMonster> monsters() {
        return AbstractDungeon.getMonsters().monsters;
    }

    public static void addToBot_AddCardOrbToOrbGroup(CardOrb orb) {
        forHangUpCardGroup(hangUpCardGroup ->
                hangUpCardGroup.hangUpNewCard(orb)).addToBotAsAbstractAction();
    }

    public static ActionUtility.VoidSupplier forHangUpCardGroup(Consumer<HangUpCardGroup> cardGroupConsumer) {
        return () -> InBattleDataManager.getHangUpCardOrbGroup().ifPresent(cardGroupConsumer);
    }

    public static <T> ActionUtility.VoidSupplier forEachHangUpCard(BiConsumer<CardOrb, T> consumer, T arg) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                consumer.accept(orb, arg);
            }
        });
    }

    public static ActionUtility.VoidSupplier forEachHangUpCard(BiConsumer<HangUpCardGroup, CardOrb> consumer) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                consumer.accept(hangUpCardGroup, orb);
            }
        });
    }

    public static ActionUtility.VoidSupplier forEachHangUpCard(Consumer<CardOrb> consumer) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                consumer.accept(orb);
            }
        });
    }


    public static <TArg, TOrb extends CardOrb> ActionUtility.VoidSupplier forEachHangUpCard(
            Class<TOrb> OrbClass, BiConsumer<TOrb, TArg> consumer, TArg arg) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                if (OrbClass.isInstance(orb)) {
                    consumer.accept((TOrb) orb, arg);
                }
            }
        });
    }

    public static <TOrb extends CardOrb> ActionUtility.VoidSupplier forEachHangUpCard(Class<TOrb> OrbClass,
                                                                                      BiConsumer<HangUpCardGroup, TOrb> consumer) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                if (OrbClass.isInstance(orb)) {
                    TOrb tOrb = (TOrb) orb;
                    consumer.accept(hangUpCardGroup, tOrb);
                }
            }
        });
    }

    public static <TOrb extends CardOrb> ActionUtility.VoidSupplier forEachHangUpCard(Class<TOrb> OrbClass,
                                                                                      Consumer<TOrb> consumer) {
        return forHangUpCardGroup(hangUpCardGroup -> {
            for (CardOrb orb : hangUpCardGroup.cards) {
                if (OrbClass.isInstance(orb)) {
                    TOrb tOrb = (TOrb) orb;
                    consumer.accept(tOrb);
                }
            }
        });
    }

    public boolean hasOrb() {
        return !cards.isEmpty();
    }

    public void removeCard(CardOrb cardOrb) {
        cards.remove(cardOrb);
        cardOrb.onRemove();
        letEachOrbToSlotPlaces();
    }

    protected void hangUpNewCard(CardOrb orb) {
        cards.add(orb);
        letEachOrbToSlotPlaces();
    }

    private <T> void forEachOrbInThisOrbGroup(BiConsumer<CardOrb, T> consumer, T arg) {
        for (CardOrb orb : this.cards) {
            consumer.accept(orb, arg);
        }
    }

    private void forEachOrbInThisOrbGroup(Consumer<CardOrb> consumer) {
        for (CardOrb orb : this.cards) {
            consumer.accept(orb);
        }
    }

    private <TArg, TOrb extends CardOrb> void forEachOrbInThisOrbGroup(
            Class<TOrb> OrbClass, BiConsumer<TOrb, TArg> consumer, TArg arg) {
        for (CardOrb orb : this.cards) {
            if (OrbClass.isInstance(orb)) {
                consumer.accept((TOrb) orb, arg);
            }
        }
    }

    private <TOrb extends CardOrb> void forEachOrbInThisOrbGroup(
            Class<TOrb> OrbClass, Consumer<TOrb> consumer) {
        for (CardOrb orb : this.cards) {
            if (OrbClass.isInstance(orb)) {
                TOrb tOrb = (TOrb) orb;
                consumer.accept(tOrb);
            }
        }
    }

    private Stream<CardOrb> getCardOrbStream() {
        return this.cards.stream();
    }

    private void removeUselessCard() {
        this.remove_check_counter--;
        if (this.remove_check_counter >= 0) return;
        this.remove_check_counter = 10;
        this.forEachOrbInThisOrbGroup(orb -> {
            orb.checkShouldRemove();
            if (orb.ifShouldRemove() || ActionUtility.isNotInBattle())
                AutoDoneInstantAction.addToBotAbstract(() -> removeCard(orb));
        });
    }

    //设置球的位置的函数，自动移动球的位置
    public final void letOrbToSlotPlace(final CardOrb orb, final int slotIndex) {
        final Vector2 orbPlace = makeSlotPlaceLine(this.hitbox.width, slotIndex);
        orb.tX = orbPlace.x + this.hitbox.cX;
        orb.tY = orbPlace.y + this.hitbox.cY;
        orb.hb.move(orb.tX, orb.tY);
    }

    public final void initOrbToSlotPlace(final CardOrb orb, final int slotIndex) {
        final Vector2 orbPlace = makeSlotPlaceLine(this.hitbox.width, slotIndex);
        orb.cX = orbPlace.x + this.hitbox.cX;
        orb.cY = orbPlace.y + this.hitbox.cY;
        orb.hb.move(orb.cX, orb.cY);
    }

    public final void letEachOrbToSlotPlaces() {
        int bound = cards.size();
        for (int i = 0; i < bound; i++) {
            letOrbToSlotPlace(cards.get(i), i);
        }
    }

    public final int getCardsAmount() {
        return this.cards.size();
    }

    protected final Vector2 makeSlotPlaceLine(final float totalLength, int slotIndex) {
        final float offsetX = OffsetPercentageBySlotIndex_TwoEnd(slotIndex) * totalLength;
        Vector2 vector2 = new Vector2();
        vector2.x = offsetX;
        vector2.y = this.hitbox.height / 2.0f;
        return vector2;
    }

    protected final float OffsetPercentageBySlotIndex_TwoEnd(float slotIndex) {
        final float maxOrbs = getCardsAmount();
        return ((slotIndex + 1) / (maxOrbs + 1) - 0.5f);
    }

    protected final float OffsetPercentageBySlotIndex_Cycle(float slotIndex) {
        final float maxOrbs = getCardsAmount();
        return (slotIndex) / (maxOrbs);
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        this.forEachOrbInThisOrbGroup(AbstractOrb::onStartOfTurn);
    }

    @Override
    public void receivePowersModified() {
        this.forEachOrbInThisOrbGroup(AbstractOrb::updateDescription);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (ActionUtility.isNotInBattle()) return;
//        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.bottom).forEach(orb -> orb.render(sb));
//        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.middle).forEach(orb -> orb.render(sb));
        getCardOrbStream().forEach(orb -> orb.render(sb));
        if (hoveredCard != null)
            hoveredCard.render(sb);
    }

//    private void checkIfPlayerHoverHandCard() {
//        AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
//        if (!((boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")))
//            return;
//        if (hoveredCard == null) return;
//        forEachOrbInThisOrbGroup(CardOrb_CardTrigger.class, CardOrb_CardTrigger::onPlayerHoveringHandCard,hoveredCard);
//    }

    @Override
    public void update() {
        if (ActionUtility.isNotInBattle()) return;

        for (CardOrb cardOrb : this.cards) {
            if (cardOrb.isCardHovered()) {
                this.hoveredCard = cardOrb;
                break;
            }
        }

        if (hoveredCard != null && hoveredCard.ifShouldRemove())
            hoveredCard = null;

        this.forEachOrbInThisOrbGroup(orb -> {
            orb.update();
            orb.updateAnimation();
        });
//        checkIfPlayerHoverHandCard();
        removeUselessCard();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        this.forEachOrbInThisOrbGroup(CardOrb_CardTrigger.class, CardOrb_CardTrigger::onCardUsed, abstractCard);
    }

    @Override
    public void receiveAtEndOfPlayerTurnPreCard() {
        this.forEachOrbInThisOrbGroup(AbstractOrb::onEndOfTurn);
    }
}
