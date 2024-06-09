//package superstitioapi.pet;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpireField;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.monsters.MonsterGroup;
//import com.megacrit.cardcrawl.monsters.exordium.Cultist;
//
//import java.util.List;
//import java.util.Objects;
//
//import static superstitioapi.pet.CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster;
//
//public class MinionHelper {
//
//    public static MonsterGroup getMinions(AbstractPlayer player) {
//        return PlayerAddFieldsPatch.f_minions.get(player);
//    }
//
//    public static MonsterGroup getMinions() {
//        return PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
//    }
//
//    public static List<AbstractMonster> getMinionMonsters() {
//        return PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player).monsters;
//    }
//
//    public static void changeMaxMinionAmount(AbstractPlayer player, int newMax) {
//        PlayerAddFieldsPatch.f_maxMinions.set(player, newMax);
//    }
//
//    public static int getAliveMinionsNum() {
//        return (int)
//                MinionHelper.getMinions().monsters.stream().filter(m -> !m.isDeadOrEscaped()).count();
//    }
//
//    public static boolean addMinion(AbstractPlayer player, AbstractMonster minionToAdd) {
//        MonsterGroup minions = PlayerAddFieldsPatch.f_minions.get(player);
//        int maxMinions = PlayerAddFieldsPatch.f_maxMinions.get(player);
//        if (minions.monsters.size() == maxMinions) {
//            return false;
//        }
//        else {
//            minions.add(minionToAdd);
//            minionToAdd.init();
//            minionToAdd.usePreBattleAction();
//            minionToAdd.showHealthBar();
//            return true;
//        }
//    }
//
//    public static boolean removeMinion(AbstractPlayer player, AbstractMonster minionToRemove) {
//        return PlayerAddFieldsPatch.f_minions.get(player).monsters.remove(minionToRemove);
//    }
//
//    public static boolean hasMinions(AbstractPlayer player) {
//        return !PlayerAddFieldsPatch.f_minions.get(player).monsters.isEmpty();
//    }
//
//    public static boolean hasMinions() {
//        return !PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player).monsters.isEmpty();
//    }
//
//    public static boolean hasMinion(AbstractMonster m) {
//        return PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player).monsters.contains(m);
//    }
//
//    public static int getMaxMinions(AbstractPlayer player) {
//        return PlayerAddFieldsPatch.f_maxMinions.get(player);
//    }
//
//    public static void clearMinions(AbstractPlayer player) {
//        MonsterGroup minions = new MonsterGroup(new AbstractMonster[]{});
//        minions.monsters.removeIf(Objects::isNull);
//        PlayerAddFieldsPatch.f_minions.set(player, minions);
//    }
//
//    public void summonMinion(Class<? extends AbstractMonster> monsterClass) {
//        AbstractMonster monster = motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass);
//        newPet = false;
//
//        if (monster == null) {
//            monster = new Cultist(0, 0);
//            Invoker.setField(monster, "talky", true);
//            System.out.println("抓不到的都变成咔咔");
//        }
//
//
//        AbstractMonster monster1;
//        //一层
//        switch (monster.id) {
//
//            case "Hexaghost":
//                monster = new Hexaghost1();
//                break;
//            //case"Cultist":monster=new Cultist(0,0);
//            // break;
//            case "Sentry":
//                monster = new Sentry1(0, 0);
//                break;
//            case "Looter":
//                monster = new Looter1(0, 0);
//                break;
//            case "GremlinTsundere":
//                monster = new GremlinTsundere1(0, 0);
//                break;
//            case "SlimeBoss":
//                monster = new SlimeBoss1();
//                break;
//            case "TheCollector":
//                monster = new TheCollector1();
//                monster1 = new TorchHead1(0, 0);
//                monster1.drawY = AbstractDungeon.player.drawY + (int) site[slot].y;
//                //monster1.drawX = AbstractDungeon.player.drawX - 175 + (int) site[slot].x-200;
//                monster1.drawX = 800;
//
//                monster1.flipHorizontal = true;
//                SummonHelper.summonMinion(monster1);
//                break;
//            case "BronzeAutomaton":
//                monster = new BronzeAutomaton1();
//                AbstractMonster monster2 = new BronzeOrb1(-300.0F, 200.0F, 0);
//                SummonHelper.summonMinion(monster2);
//                break;
//            case "BronzeOrb":
//                monster = new BronzeOrb1(-300.0F, 200.0F, 0);
//                break;
//            case "GremlinLeader":
//                monster = new GremlinLeader1();
//                break;
//            case "Healer":
//                monster = new Healer1(0, 0);
//                break;
//            case "SlaverBoss":
//                monster = new Taskmaster1(0, 0);
//                break;
//
//            case "Centurion":
//                monster = new Centurion1(0, 0);
//                break;
//            case "Chosen":
//                monster = new Chosen1(0, 0);
//                break;
//            case "Snecko":
//                monster = new Snecko1(0, 0);
//                break;
//            case "Exploder":
//                monster = new Exploder1(0, 0);
//                break;
//            case "Repulsor":
//                monster = new Repulsor1(0, 0);
//                break;
//            case "WrithingMass":
//                monster = new WrithingMass1();
//                break;
//            case "Reptomancer":
//                monster = new Reptomancer1();
//                break;
//            case "SpireGrowth":
//                monster = new SpireGrowth1();
//                break;
//            case "Nemesis":
//                monster = new Nemesis1();
//                break;
//            case "Donu":
//                monster = new Donu1();
//                break;
//            case "Deca":
//                monster = new Deca1();
//                break;
//            case "AwakenedOne":
//                monster = new AwakenedOne1(0, 0);
//                break;
//            case "SpireShield":
//                monster = new SpireShield1();
//                break;
//            case "SpireSpear":
//                monster = new SpireSpear1();
//                break;
//            case "Orb Walker":
//                monster = new OrbWalker1(0, 0);
//                break;
//            case "CorruptHeart":
//                monster = new CorruptHeart1();
//                break;
//            case "TheGuardian":
//                monster = new TheGuardian1();
//                break;
//
//
//            ///monster.drawY = AbstractDungeon.player.drawY + (int) site[slot].y;
//            ///monster.drawX = AbstractDungeon.player.drawX - 175 + (int) site[slot].x;
//            pet = monster;
//            monster.maxHealth = monster.currentHealth = counter;
//            AbstractDungeon.actionManager.addToTop(new SummonMinionAction(monster));
//
//            AbstractDungeon.actionManager.addToBottom(new ApplyEntryAction(this, monster));
//            ScreenPartition.assignSequentialPosition(monster, null);
//            BgmReplay(monster);
//            System.out.println("本PokeGo的slot为" + slot);
//            System.out.println("已在" + +monster.drawX + "," + monster.drawY + "生成" + this.pet.name);
//
//        }
//        else{
//            monsterClass = null;
//            pet = null;
//        }
//    }
//
//    @SpirePatch(
//            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
//            method = "<class>"
//    )
//    public static class PlayerAddFieldsPatch {
//        private static Integer maxMinions = Integer.MAX_VALUE;
//        public static SpireField<Integer> f_maxMinions = new SpireField<>(() -> maxMinions);
//        private static MonsterGroup minions = new MonsterGroup(new AbstractMonster[]{});
//        public static SpireField<MonsterGroup> f_minions = new SpireField<>(() -> minions);
//
//        public PlayerAddFieldsPatch() {
//        }
//    }
//}