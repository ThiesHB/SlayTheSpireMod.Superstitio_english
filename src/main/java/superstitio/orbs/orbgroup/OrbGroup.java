package superstitio.orbs.orbgroup;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import superstitio.InBattleDataManager;
import superstitio.orbs.actions.AnimationOrbOnMonsterAction;
import superstitio.orbs.actions.ChannelOnOrbGroupAction;
import superstitio.orbs.actions.EvokeOnMonsterAction;
import superstitio.orbs.actions.FlashOrbEffect;
import superstitio.utils.ActionUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class OrbGroup {
    private static final String[] TEXT = new String[]{"  A  "};
    private static final int MAX_MAX_ORB = 10;
    public AbstractOrb CustomEmptyOrb;
    public ArrayList<AbstractOrb> orbs = new ArrayList<>();
    public Hitbox hitbox;
    private int _maxOrbs;

    public OrbGroup(Hitbox hitbox, int initMaxOrbs) {
        this(hitbox, initMaxOrbs, new EmptyOrbSlot());
    }

    public OrbGroup(Hitbox hitbox, int initMaxOrbs, AbstractOrb customEmptyOrb) {
        this.hitbox = new Hitbox(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        this.CustomEmptyOrb = customEmptyOrb;
        this.increaseMaxOrbs(initMaxOrbs, false);
    }

    public static <T extends AbstractOrb> T makeOrbCopy(final T orb, final Class<T> clazz) {
        final AbstractOrb tmp = orb.makeCopy();
        final int passive = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "basePassiveAmount");
        final int evoke = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "baseEvokeAmount");
        ReflectionHacks.setPrivate(tmp, AbstractOrb.class, "basePassiveAmount", passive);
        ReflectionHacks.setPrivate(tmp, AbstractOrb.class, "baseEvokeAmount", evoke);
        return clazz.cast(tmp);
    }

    public static ArrayList<AbstractMonster> monsters() {
        return AbstractDungeon.getMonsters().monsters;
    }

    public static void forEachOrbInEachOrbGroup(Consumer<AbstractOrb> consumer) {
        for (OrbGroup orbGroup : InBattleDataManager.orbGroups) {
            for (AbstractOrb orb : orbGroup.orbs) {
                consumer.accept(orb);
            }
        }
    }

    public static <T> void forEachOrbInEachOrbGroup(BiConsumer<AbstractOrb, T> consumer, T arg) {
        for (OrbGroup orbGroup : InBattleDataManager.orbGroups) {
            for (AbstractOrb orb : orbGroup.orbs) {
                consumer.accept(orb, arg);
            }
        }
    }

    public void EnhanceOrb(final AbstractOrb orb, final int amount) {
        if (this.isEmptySlot(orb) || orb instanceof Plasma) return;
        final int passive = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "basePassiveAmount");
        final int evoke = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "baseEvokeAmount");
        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "basePassiveAmount", passive + amount);
        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "baseEvokeAmount", evoke + amount);
        orb.updateDescription();
        ActionUtility.addEffect(new FlashOrbEffect(orb.cX, orb.cY));
        ActionUtility.addEffect(new TextAboveCreatureEffect(orb.hb.cX, orb.hb.cY, OrbGroup.TEXT[0], Color.WHITE.cpy()));
    }

    public AbstractOrb getNewCustomEmptyOrb() {
        return CustomEmptyOrb.makeCopy();
    }

    //设置球的位置的函数
    protected Vector2 makeSlotPlace(final int slotIndex) {
        Vector2 orbPlace;
        orbPlace = makeSlotPlaceAsRound(this.hitbox.width / 2.0f, slotIndex);
        if (GetMaxOrbs() == 1) {
            orbPlace = makeSlotPlaceAsSingle(this.hitbox.width / 2.0f);
        }
        return orbPlace;
    }

    private Vector2 makeSlotPlaceAsSingle(final float height) {
        return new Vector2(0, height + this.hitbox.height / 2.0f);
    }

    protected final Vector2 makeSlotPlaceAsSpiral(final float startRadius, int slotIndex) {
        final float eachAddRadius = 3.0f;
        final float distanceToOrbGroupCenter = startRadius + slotIndex * eachAddRadius * Settings.scale;//螺线
        final float fullAngle = 100.0f + GetMaxOrbs() * 12.0f;
        return makeSlotPlacePolar(slotIndex, distanceToOrbGroupCenter, fullAngle);
    }

    protected final Vector2 makeSlotPlaceAsRound(final float radius, int slotIndex) {
        final float distanceToOrbGroupCenter = radius + GetMaxOrbs() * 10.0f * Settings.scale;//圆
        final float fullAngle = 60.0f + GetMaxOrbs() * 12.0f;
        return makeSlotPlacePolar(slotIndex, distanceToOrbGroupCenter, fullAngle);
    }

    protected final Vector2 makeSlotPlaceLine(final float totalLength, int slotIndex) {
        final float offsetX = OffsetPercentageBySlotIndex_TwoEnd(slotIndex) * totalLength;
        Vector2 vector2 = new Vector2();
        vector2.x = offsetX;
        vector2.y = this.hitbox.height / 2.0f;
        return vector2;
    }


    protected final Vector2 makeSlotPlacePolar(int slotIndex, float distanceToOrbGroupCenter, float fullAngle) {
        float slotAngle = fullAngle;
        final float offsetAngle = slotAngle / 2.0f;
        slotAngle *= slotIndex / (GetMaxOrbs() - 1.0f);
        slotAngle += 90.0f - offsetAngle;
        Vector2 vector2 = new Vector2();
        vector2.x = distanceToOrbGroupCenter * MathUtils.cosDeg(slotAngle);
        vector2.y = distanceToOrbGroupCenter * MathUtils.sinDeg(slotAngle) + this.hitbox.height / 2.0f;
        return vector2;
    }

    protected final float OffsetPercentageBySlotIndex_TwoEnd(float slotIndex) {
        final float maxOrbs = GetMaxOrbs();
        return ((slotIndex + 1) / (maxOrbs + 1) - 0.5f);
    }

    protected final float OffsetPercentageBySlotIndex_Cycle(float slotIndex) {
        final float maxOrbs = GetMaxOrbs();
        return (slotIndex) / (maxOrbs ) ;
    }

    //设置球的位置的函数，自动移动球的位置
    public final void letOrbToSlotPlace(final AbstractOrb orb, final int slotIndex) {
        final Vector2 orbPlace = makeSlotPlace(slotIndex);
        orb.tX = orbPlace.x + this.hitbox.cX;
        orb.tY = orbPlace.y + this.hitbox.cY;
        orb.hb.move(orb.tX, orb.tY);
    }

//    public final void letOrbToSlotPlace(final int slotIndex) {
//        letOrbToSlotPlace(orbs.get(slotIndex), slotIndex);
//    }

    public final void letEachOrbToSlotPlaces() {
        int bound = orbs.size();
        for (int i = 0; i < bound; i++) {
            letOrbToSlotPlace(orbs.get(i), i);
        }
    }

    public final boolean hasNoEmptySlot() {
        return !hasEmptySlot();
    }

    public final boolean hasEmptySlot() {
        return this.orbs.stream().anyMatch(this::isEmptySlot);
    }

    public boolean isEmptySlot(AbstractOrb orb) {
        return Objects.equals(orb.ID, CustomEmptyOrb.ID);
    }

    public int findFirstEmptyOrb() {
        int index = -1;
        for (int i = 0; i < orbs.size(); ++i) {
            AbstractOrb o = orbs.get(i);
            if (isEmptySlot(o)) {
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     * 塞入球
     */
    public void channelOrb(final AbstractOrb orb) {
        if (GetMaxOrbs() <= 0) return;
        if (hasNoEmptySlot()) {
            AbstractDungeon.actionManager.addToTop(new ChannelOnOrbGroupAction(this, orb));
            AbstractDungeon.actionManager.addToTop(new EvokeOnMonsterAction(this, 1));
            AbstractDungeon.actionManager.addToTop(new AnimationOrbOnMonsterAction(this, 1));
            return;
        }
        int index = findFirstEmptyOrb();
        final AbstractOrb target = orbs.get(index);
        orb.cX = target.cX;
        orb.cY = target.cY;
        orbs.set(index, orb);
        letOrbToSlotPlace(orb, index);
        orb.updateDescription();
        orb.playChannelSFX();
        AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orb);
        AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orb);
        OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS.forEach(sub -> sub.onOrbChannel(orb, AbstractDungeon.player));
        orb.applyFocus();
        this.onOrbChannel(orb);
    }

    protected abstract void onOrbChannel(final AbstractOrb channeledOrb);

    /**
     * 激发球
     */
    public void evokeOrb() {
        if (hasNoOrb()) return;
        AbstractOrb orbEvoked = orbs.get(0);
        orbEvoked.onEvoke();
        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(sub -> sub.onOrbEvoke(orbEvoked, AbstractDungeon.player));
        int newSize = orbs.size() - 1;
        for (int i = 0; i < newSize; i++) {
            Collections.swap(orbs, i + 1, i);
        }
        orbs.set(newSize, getNewCustomEmptyOrb());
        letEachOrbToSlotPlaces();
        this.onOrbEvoke(orbEvoked);
    }

    /**
     * 激发球，不填补空缺
     */
    public void evokeOrb(int slotIndex) {
        if (hasNoOrb()) return;
        AbstractOrb orbEvoked = orbs.get(slotIndex);
        orbEvoked.onEvoke();
        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(sub -> sub.onOrbEvoke(orbEvoked, AbstractDungeon.player));
        orbs.set(slotIndex, getNewCustomEmptyOrb());
        letEachOrbToSlotPlaces();
        this.onOrbEvoke(orbEvoked);
    }

    protected abstract void onOrbEvoke(final AbstractOrb evokedOrb);

    private boolean hasNoOrb() {
        return orbs.isEmpty() || orbs.stream().allMatch(this::isEmptySlot);
    }

    public final void increaseMaxOrbs(final int amount, final boolean playSfx) {
        if (_maxOrbs + amount >= MAX_MAX_ORB) {
            return;
        }
        if (playSfx) {
            CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1f);
        }
        _maxOrbs += amount;

        for (int i = 0; i < amount; i++) {
            orbs.add(getNewCustomEmptyOrb());
        }

        letEachOrbToSlotPlaces();
    }

    public final void decreaseMaxOrbs(final int amount) {
        if (this._maxOrbs <= 0) return;
        _maxOrbs -= amount;
        if (this._maxOrbs < 0) this._maxOrbs = 0;

        if (!this.orbs.isEmpty()) {
            this.orbs.remove(this.orbs.size() - 1);
        }

        letEachOrbToSlotPlaces();
    }

    public final void triggerEvokeAnimation(final int index) {
        if (GetMaxOrbs() <= 0) {
            return;
        }
        if (!orbs.isEmpty()) {
            orbs.get(index).triggerEvokeAnimation();
        }
    }

    public final int GetMaxOrbs() {
        return this._maxOrbs;
    }
}
