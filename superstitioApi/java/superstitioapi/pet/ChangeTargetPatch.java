//package superstitioapi.pet;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.rooms.MonsterRoom;
//import superstitioapi.Logger;
//import superstitioapi.utils.CardUtility;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType.*;
//import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
//import static superstitioapi.pet.MinionHelper.getMinionMonsters;
//import static superstitioapi.pet.MinionHelper.hasMinion;
//
//
//public class ChangeTargetPatch {
//
//    public static List<AbstractCreature> targetPet = new ArrayList<>();
//    public static List<AbstractCreature> sourceEnemy = new ArrayList<>();
//    public static boolean last = false;
//
//    @SpirePatch(
//            clz = AbstractGameAction.class,
//            method = "setValues",
//            paramtypez = {AbstractCreature.class, DamageInfo.class}
//    )
//    public static class ChangeDamageTarget {
//        @SpirePostfixPatch
//        public static void Postfix(AbstractGameAction action, AbstractCreature target, DamageInfo info) {
////            if (AbstractDungeon.overlayMenu.endTurnButton.enabled) return;
//            List<AbstractMonster> livePet = getMinionMonsters().stream()
//                    .filter(monster -> !monster.isDeadOrEscaped())
//                    .collect(Collectors.toList());
//            if (info.type == THORNS) return;
//            //敌人为发出者的情况下，目标变为宠物
//            if (ChangeTargetPatch.targetPet != null && target == AbstractDungeon.player && ChangeTargetPatch.sourceEnemy.contains(info.owner)) {
//                action.target = CardUtility;
//                return;
//            }
//
//            if (livePet.isEmpty()) return;
//
//            if (info.owner != pokeGo.pet) {
//                return;
//            }
//            action.target = (AbstractCreature) pokeGo.target;
//            if (action.target == null && action instanceof VampireDamageAction) {
//                action.target = info.owner;
//            }
//            if (action.target == null) {
//                try {
//                    final Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration", (Class<?>[]) new Class[0]);
//                    method.setAccessible(true);
//                    method.invoke(action, new Object[0]);
//                } catch (NoSuchMethodException | InvocationTargetException |
//                         IllegalAccessException ex2) {
//                    final ReflectiveOperationException ex;
//                    final ReflectiveOperationException e = ex;
//                    e.printStackTrace();
//                }
//            }
//            else {
//                info.applyPowers((AbstractCreature) pokeGo.pet, action.target);
//            }
//
//
//            //            //在怪物回合开始后
////
////            //忽略反伤的情况
////            if (info.type == THORNS) return;
////            if (info.owner == null) return;
//////            if (info.owner.hasPower(EntanglePower.POWER_ID)) {
//////                //怪物不打人，打宠物
//////                action.target = null;
//////                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(info.owner, info.owner, EntanglePower.POWER_ID));
//////                return;
//////            }
////
////            List<AbstractMonster> livePet = getMinionMonsters().stream()
////                    .filter(monster -> !monster.isDeadOrEscaped())
////                    .collect(Collectors.toList());
////
////            if (livePet.isEmpty()) return;
////            if (!(info.owner instanceof AbstractMonster)) return;
////            if (!livePet.contains(info.owner)) return;
////
////            AbstractMonster randomMonsterAsTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
////            action.target = randomMonsterAsTarget;
////            if (randomMonsterAsTarget != null && !randomMonsterAsTarget.isDeadOrEscaped() && !randomMonsterAsTarget.halfDead) {
////                info.isModified = false;
////                info.applyPowers(info.owner, randomMonsterAsTarget);
////
////                //带壳寄生怪
////                if (action.target == null && action instanceof VampireDamageAction) {
////                    if (!randomMonsterAsTarget.isDeadOrEscaped()) {
////                        action.target = randomMonsterAsTarget;
////                    }
////                }
////                if (action.target == null) {
////                    try {
////                        Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
////                        method.setAccessible(true);
////                        method.invoke(action);
////                    } catch (NoSuchMethodException | InvocationTargetException |
////                             IllegalAccessException e) {
////                        Logger.error(e);
////                    }
////                }
////                else {
////                    info.applyPowers(info.owner, action.target);
////                }
////            }
//
////            List<AbstractMonster> livepet = getMinionMonsters().stream()
////                    .filter(monster -> !monster.isDeadOrEscaped())
////                    .collect(Collectors.toList());
////            int minionNum = livepet.size();
////            if (minionNum == 0) return;
////            int n = AbstractDungeon.aiRng.random(minionNum - 1);
////            AbstractMonster monster = livepet.get(n);
////            if (info.owner instanceof AbstractMonster)
////                if (getMinionMonsters().contains((AbstractMonster) info.owner) && action.target == player) {
////                    if (last) {
////                        action.target = monster;
////                        info.isModified = false;
////                        info.applyPowers(info.owner, monster);
////                    }
////                    else {
////                        info.isModified = false;
////                        info.applyPowers(info.owner, player);
////                        action.target = player;
////                    }
////                }
//        }
//
//        @SpirePatch(
//                clz = AbstractGameAction.class,
//                method = "setValues",
//                paramtypez = {AbstractCreature.class, AbstractCreature.class, int.class}
//        )
//        public static class ChangeBuffTarget {
//            public static void Postfix(AbstractGameAction action, AbstractCreature target, AbstractCreature source, int amount) throws
//            NoSuchFieldException {
//                AbstractMonster randomMonsterAsTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//                List<AbstractMonster> livepet1 = getMinionMonsters().stream()
//                        .filter(monster -> !monster.isDeadOrEscaped())
//                        .collect(Collectors.toList());
//                if (livepet1.isEmpty()) return;
//                if (!(action.source instanceof AbstractMonster)) return;
//                if (!livepet1.contains(action.source)) {
//                    if (target == player && ChangeTargetPatch.sourceEnemy.contains(source)) {
////                        if (AbstractDungeon.aiRng.randomBoolean(0.5F)) {
//                        int newTarget = AbstractDungeon.aiRng.random(livepet1.size() - 1);
//                        action.target = livepet1.get(newTarget);
////                        }
//                    }
//                    return;
//                }
//
//                if (action.source != action.target) {
//                    action.target = randomMonsterAsTarget;
//                }
//
//                //带壳寄生怪
//                if (action.target == null && action instanceof VampireDamageAction) {
//                    action.target = randomMonsterAsTarget;
//                }
//
//                if (action.target == null) {
//                    try {
//                        Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
//                        method.setAccessible(true);
//                        if (action.actionType != AbstractGameAction.ActionType.CARD_MANIPULATION) {
//                            method.invoke(action);
//                        }
//                    } catch (NoSuchMethodException | InvocationTargetException |
//                             IllegalAccessException e) {
//                        Logger.error(e);
//                    }
//                }
//            }
//        }
//
//
//        @SpirePatch(
//                clz = AbstractGameAction.class,
//                method = "setValues",
//                paramtypez = {AbstractCreature.class, AbstractCreature.class}
//        )
//        public static class ChangeBuff2Target {
//            @SpirePostfixPatch
//            public static void Postfix(AbstractGameAction action, AbstractCreature target, AbstractCreature source) throws NoSuchFieldException {
//                if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoom)) return;
//                AbstractMonster randomMonsterAsTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//                List<AbstractMonster> livePet = getMinionMonsters()
//                        .stream()
//                        .filter(monster -> !monster.isDeadOrEscaped())
//                        .collect(Collectors.toList());
//                int minionNum = livePet.size();
//
//                if (livePet.isEmpty()) return;
//                if (!livePet.contains(action.source)) {
//                    if (target == player && ChangeTargetPatch.sourceEnemy.contains(source)) {
//                        if (AbstractDungeon.aiRng.randomBoolean(0.5F)) {
//
//                            int n = AbstractDungeon.aiRng.random(minionNum - 1);
//                            action.target = livePet.get(n);
//
//                        }
//                    }
//                    return;
//                }
//
//                if (action.source != action.target) {
//                    action.target = randomMonsterAsTarget;
//                }
//
//                //带壳寄生怪
//                if (action.target == null && action instanceof VampireDamageAction) {
//                    action.target = randomMonsterAsTarget;
//                }
//
//                if (action.target == null) {
//                    try {
//                        Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
//                        method.setAccessible(true);
//                        if (action.actionType != AbstractGameAction.ActionType.CARD_MANIPULATION) {
//                            method.invoke(action);
//                        }
//
//                    } catch (NoSuchMethodException | InvocationTargetException |
//                             IllegalAccessException e) {
//                        Logger.error(e);
//                    }
//                }
//            }
//        }
//
//        @SpirePatch(
//                clz = ApplyPowerAction.class,
//                method = SpirePatch.CONSTRUCTOR,
//                paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction
//                .AttackEffect.class}
//        )
//        public static class ChangeApplyBuffTarget {
//            @SpirePostfixPatch
//            public static void Postfix(ApplyPowerAction action, AbstractCreature target, AbstractCreature source, AbstractPower power, int n,
//            boolean b, AbstractGameAction.AttackEffect e) {
//                if (!(power.owner instanceof AbstractMonster)) return;
//                if (!hasMinion((AbstractMonster) power.owner)) return;
//                if (power.owner != action.target) {
//                    power.owner = action.target;
//                }
//            }
//        }
//    }
//}
