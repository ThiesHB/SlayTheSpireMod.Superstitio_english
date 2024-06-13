//package superstitioapi.pet;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
//import com.megacrit.cardcrawl.actions.common.DamageAction;
//import com.megacrit.cardcrawl.actions.common.RollMoveAction;
//import com.megacrit.cardcrawl.actions.utility.WaitAction;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.characters.Defect;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import superstitioapi.Logger;
//import superstitioapi.utils.CardUtility;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static basemod.ReflectionHacks.getPrivate;
//
//public class MinionPlayer extends Minion {
//    public static final float SCALE = 3f;
//    private static final AbstractPlayer DefaultPlayer;
//
//    static {
//        try {
//            DefaultPlayer = Defect.class.getDeclaredConstructor(String.class).newInstance("Loser");
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
//                 NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public MinionPlayer(AbstractPlayer petCore) {
//        super(petCore);
//
////        for (DamageInfo d : this.getPetCorePlayer().damage) {
////            d.base /= (int) SCALE;
////            d.output = d.base;
////        }
////        this.getPetCorePlayer().refreshIntentHbLocation();
//
//    }
//
//
//    @Override
//    protected Texture setupImg() {
//        Texture img = getPetCorePlayer().img;
//        getPetCorePlayer().img = null;
//        return img;
//    }
//
//    @Override
//    public void createIntent() {
//
//    }
//
//    @Override
//    public void applyPowers() {
//    }
//
//    @Override
//    protected void updatePetCore() {
//        this.getPetCorePlayer().update();
//    }
//
//    @Override
//    public void renderTip(SpriteBatch sb) {
//        this.getPetCorePlayer().renderPowerTips(sb);
//    }
//
//    @Override
//    public void takeTurn() {
//        if (getPetCorePlayer() == null) {
//            Logger.warning("no symbol monster for minion " + this.name);
//            return;
//        }
//
//    }
//
//    @Override
//    public void updatePowers() {
//        this.getPetCorePlayer().updatePowers();
//    }
//
//    @Override
//    public void usePreBattleAction() {
//    }
//
//    @Override
//    public void useUniversalPreBattleAction() {
//    }
//
//    @Override
//    protected void getMove(int i) {
//    }
//
//    public AbstractPlayer getPetCorePlayer() {
//        if (petCore instanceof AbstractPlayer)
//            return (AbstractPlayer) petCore;
//        else
//            return DefaultPlayer;
//    }
//
//}
