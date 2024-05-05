package superstitio.orbs;

import superstitio.InBattleDataManager;
import superstitio.Logger;
import superstitio.orbs.actions.AnimationOrbOnMonsterAction;
import superstitio.orbs.actions.ChannelOnMonsterAction;
import superstitio.orbs.actions.EvokeOnMonsterAction;
import superstitio.orbs.actions.FlashOrbEffect;
import superstitio.utils.ActionUtility;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class OrbGroup {
    private static final String[] TEXT = new String[]{"  A  "};
    private static final int MAX_MAX_ORB = 10;
    public ArrayList<AbstractOrb> orbs = new ArrayList<>();
    public Hitbox hitbox;
    private int _maxOrbs;
    private final float drawX;
    private final float drawY;

    public OrbGroup(Hitbox hitbox) {
        this.hitbox = hitbox;
        this.drawX = AbstractDungeon.player.drawX;//(float) Settings.WIDTH * 0.75F;// + offsetX * Settings.xScale;
        this.drawY = AbstractDungeon.player.drawY;//AbstractDungeon.floorY;//+ offsetY * Settings.yScale;
        this.increaseMaxOrbs(5, false);
        Logger.temp("addSexMarkManager");
    }

    public static void EnhanceOrb(final AbstractOrb orb, final int amount) {
        if (orb instanceof EmptyOrbSlot || orb instanceof Plasma) return;
        final int passive = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "basePassiveAmount");
        final int evoke = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "baseEvokeAmount");
        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "basePassiveAmount", passive + amount);
        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "baseEvokeAmount", evoke + amount);
        orb.updateDescription();
        ActionUtility.addEffect(new FlashOrbEffect(orb.cX, orb.cY));
        ActionUtility.addEffect(new TextAboveCreatureEffect(orb.hb.cX, orb.hb.cY, OrbGroup.TEXT[0], Color.WHITE.cpy()));
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

    //设置球的位置的函数
    protected void setSlotPlace(final AbstractOrb orb, final int slotIndex, final int maxOrbs) {
        if (AbstractDungeon.player.hasRelic("REME_LimitBreaker")) {
            final float r = 160.0f;
            final float dist = r * Settings.scale + slotIndex * 3.0f * Settings.scale;
            final float angle = 100.0f + maxOrbs * 12.0f;
            setSlotPlaceAsRound(orb, slotIndex, maxOrbs, r, dist, angle);
        } else {
            final float r = this.hitbox.width / 2.0f;
            final float dist = r * Settings.scale + maxOrbs * 10.0f * Settings.scale;
            final float angle = 60.0f + maxOrbs * 12.0f;
            setSlotPlaceAsRound(orb, slotIndex, maxOrbs, r, dist, angle);
        }
        orb.hb.move(orb.tX, orb.tY);
    }

    private void setSlotPlaceAsRound(AbstractOrb orb, int slotIndex, int maxOrbs, float r, float dist, float angle) {
        float slotAngle = angle;
        final float offsetAngle = slotAngle / 2.0f;
        slotAngle *= slotIndex / (maxOrbs - 1.0f);
        slotAngle += 90.0f - offsetAngle;
        orb.tX = dist * MathUtils.cosDeg(slotAngle) + this.drawX;
        orb.tY = dist * MathUtils.sinDeg(slotAngle) + this.drawY + this.hitbox.height / 2.0f;
        if (maxOrbs == 1) {
            orb.tX = this.drawX;
            orb.tY = r * Settings.scale + this.drawY + this.hitbox.height / 2.0f;
        }
    }

    //设置球的位置的函数
    public final void setSlotPlace(final AbstractOrb orb, final int slotIndex) {
        setSlotPlace(orb, slotIndex, GetMaxOrbs());
    }

    public final void setSlotPlace(final int slotIndex) {
        setSlotPlace(orbs.get(slotIndex), slotIndex, GetMaxOrbs());
    }

    public final void setEachSlotOnMonster() {
        IntStream.range(0, orbs.size()).forEach(this::setSlotPlace);
    }

    public final boolean hasEmptyOrb() {
        return this.orbs.stream().anyMatch(abstractOrb -> abstractOrb instanceof EmptyOrbSlot);
    }

    public final int findFirstEmptyOrb() {
        int index = -1;
        for (int i = 0; i < orbs.size(); ++i) {
            AbstractOrb o = orbs.get(i);
            if (o instanceof EmptyOrbSlot) {
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
        if (!hasEmptyOrb()) {
            AbstractDungeon.actionManager.addToTop(new ChannelOnMonsterAction(this, orb));
            AbstractDungeon.actionManager.addToTop(new EvokeOnMonsterAction(this, 1));
            AbstractDungeon.actionManager.addToTop(new AnimationOrbOnMonsterAction(this, 1));
            return;
        }
        int index = findFirstEmptyOrb();
        final AbstractOrb target = orbs.get(index);
        orb.cX = target.cX;
        orb.cY = target.cY;
        orbs.set(index, orb);
        setSlotPlace(index);
        orb.updateDescription();
        orb.playChannelSFX();
        AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orb);
        AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orb);
        OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS.forEach(sub -> sub.onOrbChannel(orb, AbstractDungeon.player));
        orb.applyFocus();
    }

    /**
     * 激发球
     */
    public void evokeOrb() {
        if (hasOrb()) return;
        orbs.get(0).onEvoke();
        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(sub -> sub.onOrbEvoke(orbs.get(0), AbstractDungeon.player));
        final EmptyOrbSlot newSlot = new EmptyOrbSlot();
        int newSize = orbs.size() - 1;
        for (int i = 0; i < newSize; i++) {
            Collections.swap(orbs, i + 1, i);
        }
        orbs.set(newSize, newSlot);
        setEachSlotOnMonster();
    }

    private boolean hasOrb() {
        return orbs.isEmpty() || orbs.get(0) instanceof EmptyOrbSlot;
    }

    public final void increaseMaxOrbs(final int amount, final boolean playSfx) {
        if (_maxOrbs + amount >= MAX_MAX_ORB) {
            return;
        }
        if (playSfx) {
            CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1f);
        }
        _maxOrbs += amount;

        for (int i = 0; i < amount; ++i) {
            orbs.add(new EmptyOrbSlot());
        }

        setEachSlotOnMonster();
    }

    public final void decreaseMaxOrbs(final int amount) {
        if (this._maxOrbs <= 0) return;
        _maxOrbs -= amount;
        if (this._maxOrbs < 0) this._maxOrbs = 0;

        if (!this.orbs.isEmpty()) {
            this.orbs.remove(this.orbs.size() - 1);
        }

        setEachSlotOnMonster();
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
