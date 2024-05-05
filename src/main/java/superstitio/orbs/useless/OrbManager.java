//package SuperstitioMod.orbs;
//
//import SuperstitioMod.actions.AutoDoneAction;
//import SuperstitioMod.orbs.actions.*;
//import SuperstitioMod.utils.ActionUtility;
//import basemod.ReflectionHacks;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.MathUtils;
//import com.evacipated.cardcrawl.modthespire.lib.SpireField;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.orbs.AbstractOrb;
//import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
//import com.megacrit.cardcrawl.orbs.Plasma;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
//import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.function.Function;
//
//public class OrbManager {
//    private static final String[] TEXT;
//
//    static {
//        TEXT = CardCrawlGame.languagePack.getUIString("Goldenglow_UI").TEXT;
//    }
//
//    public static void EnhanceOrb(final AbstractOrb orb, final int amount) {
//        if (orb instanceof EmptyOrbSlot || orb instanceof Plasma) return;
//        final int passive = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "basePassiveAmount");
//        final int evoke = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "baseEvokeAmount");
//        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "basePassiveAmount", passive + amount);
//        ReflectionHacks.setPrivate(orb, AbstractOrb.class, "baseEvokeAmount", evoke + amount);
//        orb.updateDescription();
//        ActionUtility.addEffect(new FlashOrbEffect(orb.cX, orb.cY));
//        ActionUtility.addEffect(new TextAboveCreatureEffect(orb.hb.cX, orb.hb.cY, OrbManager.TEXT[5], Color.WHITE.cpy()));
//    }
//
//    public static <T extends AbstractOrb> T makeOrbCopy(final T orb, final Class<T> clazz) {
//        final AbstractOrb tmp = orb.makeCopy();
//        final int passive = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "basePassiveAmount");
//        final int evoke = ReflectionHacks.getPrivate(orb, AbstractOrb.class, "baseEvokeAmount");
//        ReflectionHacks.setPrivate(tmp, AbstractOrb.class, "basePassiveAmount", passive);
//        ReflectionHacks.setPrivate(tmp, AbstractOrb.class, "baseEvokeAmount", evoke);
//        return clazz.cast(tmp);
//    }
//
//    public static ArrayList<AbstractOrb> GetOrbs(AbstractMonster monster) {
//        return DroneFieldsPatch.orbs.get(monster);
//    }
//
//    public static int GetMaxOrbs(AbstractMonster monster) {
//        return DroneFieldsPatch.maxOrbs.get(monster);
//    }
//
//    public static void SetOwner(AbstractOrb orb, AbstractCreature creature) {
//        OrbFieldsPatch.owner.set(orb, creature);
//    }
//
//    public static AbstractMonster getRandomMonsterSafe() {
//        final AbstractMonster m = AbstractDungeon.getRandomMonster();
//        if (m != null && !m.isDeadOrEscaped() && !m.isDead) {
//            return m;
//        }
//        return null;
//    }
//    public static ArrayList<AbstractMonster> monsters() {
//        return AbstractDungeon.getMonsters().monsters;
//    }
//
//
//    public static void foreachAliveMonster(final Function<AbstractMonster, Boolean> func) {
//        for (final AbstractMonster m : monsters()) {
//            if (ActionUtility.isAlive(m) && func.apply(m)) {
//                return;
//            }
//        }
//    }
//
//    public static void SetMaxOrbs(AbstractMonster monster, int amount) {
//        DroneFieldsPatch.maxOrbs.set(monster, amount);
//    }
//
//    public static void SwapOrbs(final AbstractCreature source) {
//        if (source instanceof AbstractPlayer) {
//            AutoDoneAction.addToBotAbstract(() -> {
//                final AbstractMonster monster = getRandomMonsterSafe();
//                if (monster == null) return;
//                AbstractPlayer player = (AbstractPlayer) source;
//                if (player.orbs.isEmpty() || player.orbs.get(0) instanceof EmptyOrbSlot) return;
//                AbstractOrb orb = player.orbs.remove(0);
//                int index = -1;
//                int i = 0;
//                while (i < GetOrbs(monster).size()) {
//                    AbstractOrb o = GetOrbs(monster).get(i);
//                    if (o instanceof EmptyOrbSlot) {
//                        index = i;
//                        break;
//                    } else {
//                        i++;
//                    }
//                }
//                if (index > 0) {
//                    for (AbstractGameEffect e : AbstractDungeon.effectList) {
//                        if (e instanceof SimLocationEffect && ((SimLocationEffect) e).orb == orb) {
//                            e.isDone = true;
//                        }
//                    }
//                    ActionUtility.addEffect(new SimLocationEffect(orb, index, monster));
//                }
//                player.orbs.add(new EmptyOrbSlot());
//                channelOrb(monster, orb);
//            });
//
//            AutoDoneAction.addToBotAbstract(() -> {
//                List<AbstractOrb> orbs = ((AbstractPlayer) source).orbs;
//                for (int j = 0; j < orbs.size(); ++j) {
//                    orbs.get(j).setSlot(j, orbs.size());
//                }
//            });
//            return;
//        }
//        if (!(source instanceof AbstractMonster)) return;
//
//        AutoDoneAction.addToBotAbstract(() -> {
//            final AbstractMonster monster = (AbstractMonster) source;
//            if (GetOrbs(monster).isEmpty()) return;
//            if (GetOrbs(monster).get(0) instanceof EmptyOrbSlot) return;
//            AbstractOrb orb2 = GetOrbs(monster).remove(0);
//            int index2 = -1;
//            int k = 0;
//            while (k < AbstractDungeon.player.orbs.size()) {
//                if (AbstractDungeon.player.orbs.get(k) instanceof EmptyOrbSlot) {
//                    index2 = k;
//                    break;
//                } else {
//                    ++k;
//                }
//            }
//            if (index2 > 0) {
//                for (AbstractGameEffect e2 : AbstractDungeon.effectList) {
//                    if (e2 instanceof SimLocationEffect && ((SimLocationEffect) e2).orb == orb2) {
//                        e2.isDone = true;
//                    }
//                }
//                ActionUtility.addEffect(new SimLocationEffect(orb2, index2, AbstractDungeon.player));
//            }
//            GetOrbs(monster).add(new EmptyOrbSlot());
//            AbstractDungeon.player.channelOrb(orb2);
//        });
//
//        AutoDoneAction.addToBotAbstract(() -> {
//            AbstractMonster monster = (AbstractMonster) source;
//            for (int i2 = 0; i2 < GetOrbs(monster).size(); ++i2) {
//                setSlotOnMonster(monster, GetOrbs(monster).get(i2), i2, GetMaxOrbs(monster));
//            }
//        });
//    }
//
//    public static void channelOrb(final AbstractMonster monster, final AbstractOrb orb) {
//        if (GetMaxOrbs(monster) <= 0) return;
//        int index = -1;
//        for (int i = 0; i < GetOrbs(monster).size(); ++i) {
//            final AbstractOrb o = GetOrbs(monster).get(i);
//            if (o instanceof EmptyOrbSlot) {
//                index = i;
//                break;
//            }
//        }
//        if (index != -1) {
//            final AbstractOrb target = GetOrbs(monster).get(index);
//            orb.cX = target.cX;
//            orb.cY = target.cY;
//            GetOrbs(monster).set(index, orb);
//            setSlotOnMonster(monster, orb, index, GetMaxOrbs(monster));
//            SetOwner(orb, monster);
//            orb.updateDescription();
//            orb.playChannelSFX();
//            AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orb);
//            AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orb);
//            OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS.forEach(sub -> sub.onOrbChannel(orb, monster));
//            orb.applyFocus();
//        } else {
//            AbstractDungeon.actionManager.addToTop(new ChannelOnMonsterAction(monster, orb));
//            AbstractDungeon.actionManager.addToTop(new EvokeOnMonsterAction(monster, 1));
//            AbstractDungeon.actionManager.addToTop(new AnimationOrbOnMonsterAction(monster, 1));
//        }
//    }
//
//    public static void increaseMaxOrbs(final AbstractMonster monster, final int amount, final boolean playSfx) {
//        final int maxOrbs = GetMaxOrbs(monster);
//        if (maxOrbs + amount >= 10 && !AbstractDungeon.player.hasRelic("REME_LimitBreaker")) {
//            return;
//        }
//        if (playSfx) {
//            CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1f);
//        }
//        SetMaxOrbs(monster, maxOrbs + amount);
//        for (int i = 0; i < amount; ++i) {
//            GetOrbs(monster).add(new EmptyOrbSlot());
//        }
//        for (int i = 0; i < GetOrbs(monster).size(); ++i) {
//            setSlotOnMonster(monster, GetOrbs(monster).get(i), i, maxOrbs + amount);
//        }
//    }
//
//    public static void evokeOrb(final AbstractMonster monster) {
//        final ArrayList<AbstractOrb> list = GetOrbs(monster);
//        if (list.isEmpty() || list.get(0) instanceof EmptyOrbSlot) return;
//        list.get(0).onEvoke();
//        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(sub -> sub.onOrbEvoke(list.get(0), monster));
//        final EmptyOrbSlot newSlot = new EmptyOrbSlot();
//        for (int i = 1; i < list.size(); ++i) {
//            Collections.swap(GetOrbs(monster), i, i - 1);
//        }
//        GetOrbs(monster).set(list.size() - 1, newSlot);
//        for (int i = 0; i < list.size(); ++i) {
//            setSlotOnMonster(monster, GetOrbs(monster).get(i), i, list.size());
//        }
//    }
//
//    public static void triggerEvokeAnimation(final AbstractMonster monster, final int index) {
//        if (GetMaxOrbs(monster) <= 0) {
//            return;
//        }
//        if (!GetOrbs(monster).isEmpty()) {
//            GetOrbs(monster).get(index).triggerEvokeAnimation();
//        }
//    }
//
//    //设置球的位置的函数
//    public static void setSlotOnMonster(final AbstractMonster monster, final AbstractOrb orb, final int slotNum, final int maxOrbs) {
//        if (!AbstractDungeon.player.hasRelic("REME_LimitBreaker")) {
//            final float r = monster.hb_w / 2.0f;
//            final float dist = r * Settings.scale + maxOrbs * 10.0f * Settings.scale;
//            float angle = 60.0f + maxOrbs * 12.0f;
//            final float offsetAngle = angle / 2.0f;
//            angle *= slotNum / (maxOrbs - 1.0f);
//            angle += 90.0f - offsetAngle;
//            orb.tX = dist * MathUtils.cosDeg(angle) + monster.drawX;
//            orb.tY = dist * MathUtils.sinDeg(angle) + monster.drawY + monster.hb_h / 2.0f;
//            if (maxOrbs == 1) {
//                orb.tX = monster.drawX;
//                orb.tY = r * Settings.scale + monster.drawY + monster.hb_h / 2.0f;
//            }
//        } else {
//            final float dist2 = 160.0f * Settings.scale + slotNum * 3.0f * Settings.scale;
//            float angle2 = 100.0f + maxOrbs * 12.0f;
//            final float offsetAngle2 = angle2 / 2.0f;
//            angle2 *= slotNum / (maxOrbs - 1.0f);
//            angle2 += 90.0f - offsetAngle2;
//            orb.tX = dist2 * MathUtils.cosDeg(angle2) + monster.drawX;
//            orb.tY = dist2 * MathUtils.sinDeg(angle2) + monster.drawY + monster.hb_h / 2.0f;
//            if (maxOrbs == 1) {
//                orb.tX = monster.drawX;
//                orb.tY = 160.0f * Settings.scale + monster.drawY + monster.hb_h / 2.0f;
//            }
//        }
//        orb.hb.move(orb.tX, orb.tY);
//    }
//
//
//    @SpirePatch(clz = AbstractMonster.class, method = "<class>")
//    public static class DroneFieldsPatch {
//        public static SpireField<ArrayList<AbstractOrb>> orbs = new SpireField<>(ArrayList::new);
//        public static SpireField<Integer> maxOrbs = new SpireField<>(() -> 3);
//    }
//
//    @SpirePatch(clz = AbstractOrb.class, method = "<class>")
//    public static class OrbFieldsPatch {
//        public static SpireField<AbstractCreature> owner = new SpireField<>(() -> null);
//
//    }
//
//    @SpirePatch(clz = AbstractMonster.class, method = "init")
//    public static class DroneInitPatch {
//        public static void Postfix(final AbstractMonster _inst) {
//            GetOrbs(_inst).clear();
//            SetMaxOrbs(_inst, 0);
//            OrbManager.increaseMaxOrbs(_inst, 3, false);
//        }
//    }
//
//    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
//    public static class ClearDronePatch {
//        public static void Prefix(final AbstractMonster _inst, final boolean trigger) {
//            if (!_inst.isDying) {
//                GetOrbs(_inst).clear();
//                SetMaxOrbs(_inst, 0);
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
//    public static class ChannelPatch {
//        public static void Postfix(final AbstractPlayer _inst, final AbstractOrb orbToSet) {
//            if (_inst.orbs.contains(orbToSet)) {
//                SetOwner(orbToSet, _inst);
//                orbToSet.updateDescription();
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
//    public static class ModifyPowerFocusPatch {
//        public static void Postfix() {
//            if (AbstractDungeon.player.hasPower("Focus")) {
//                foreachAliveMonster(monster -> {
//                    GetOrbs(monster).forEach(AbstractOrb::updateDescription);
//                    return Boolean.FALSE;
//                });
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractRoom.class, method = "update")
//    public static class MonsterUpdateOrbPatch {
//        public static void Postfix(final AbstractRoom _inst) {
//            if (_inst.monsters == null) return;
//            for (AbstractMonster monster : monsters()) {
//                if (ActionUtility.isAlive(monster)) {
//                    for (AbstractOrb orb : GetOrbs(monster)) {
//                        orb.update();
//                        orb.updateAnimation();
//                    }
//                }
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
//    public static class MonsterRenderOrbPatch {
//        @SpireInsertPatch(rloc = 13)
//        public static void Insert(final AbstractRoom _inst, final SpriteBatch sb) {
//            if (_inst.monsters == null) return;
//            sb.setColor(Color.WHITE);
//            for (AbstractMonster monster : monsters()) {
//                if (ActionUtility.isAlive(monster)) {
//                    GetOrbs(monster).forEach(orb -> orb.render(sb));
//                }
//            }
//        }
//    }
//
//    @SpirePatch(clz = TriggerEndOfTurnOrbsAction.class, method = "update")
//    public static class EndTurnEffectOrbPatch {
//        public static void Postfix(final TriggerEndOfTurnOrbsAction _inst) {
//            for (AbstractMonster monster : monsters()) {
//                if (ActionUtility.isAlive(monster)) {
//                    for (AbstractOrb abstractOrb : GetOrbs(monster)) {
//                        abstractOrb.onEndOfTurn();
//                    }
//                }
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnOrbs")
//    public static class StartTurnEffectOrbPatch {
//        public static void Postfix(final AbstractPlayer _inst) {
//            for (AbstractMonster monster : monsters()) {
//                if (ActionUtility.isAlive(monster)) {
//                    for (AbstractOrb abstractOrb : GetOrbs(monster)) {
//                        abstractOrb.onStartOfTurn();
//                    }
//                }
//            }
//        }
//    }
//}
