////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package SuperstitioMod.orbs;
//
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
//import com.megacrit.cardcrawl.actions.defect.ChannelAction;
//import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.orbs.AbstractOrb;
//import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.vfx.ThoughtBubble;
//
//public class AddOrbAction extends AbstractGameAction {
//    private final AbstractOrb orbType;
//
//    public AddOrbAction(AbstractOrb newOrbType) {
//        this.duration = Settings.ACTION_DUR_FAST;
//        this.orbType = newOrbType;
//    }
//
//    public void update() {
//        if (this.duration == Settings.ACTION_DUR_FAST) {
//            AbstractDungeon.player.channelOrb(this.orbType);
//
//            if (Settings.FAST_MODE) {
//                this.isDone = true;
//                return;
//            }
//        }
//
//        this.tickDuration();
//    }
//
//    public void channelOrb(AbstractPlayer player, AbstractOrb orbToSet) {
//        if (player.maxOrbs <= 0) {
//            AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, AbstractPlayer.MSG[4], true));
//            return;
//        }
//
//        int index = -1;
//
//        int plasmaCount;
//        for (plasmaCount = 0; plasmaCount < player.orbs.size(); ++plasmaCount) {
//            if (player.orbs.get(plasmaCount) instanceof EmptyOrbSlot) {
//                index = plasmaCount;
//                break;
//            }
//        }
//
//        if (index != -1) {
//            orbToSet.cX = player.orbs.get(index).cX;
//            orbToSet.cY = player.orbs.get(index).cY;
//            player.orbs.set(index, orbToSet);
//            player.orbs.get(index).setSlot(index, player.maxOrbs);
//            orbToSet.playChannelSFX();
//
//            for (AbstractPower p : player.powers) {
//                p.onChannel(orbToSet);
//            }
//
//            AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orbToSet);
//            AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orbToSet);
//
//            orbToSet.applyFocus();
//        } else {
//            AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
//            AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
//            AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
//        }
//    }
//}
