//package SuperstitioMod.orbs;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
//import com.megacrit.cardcrawl.actions.defect.ChannelAction;
//import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.Hitbox;
//import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
//import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.orbs.AbstractOrb;
//import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//import com.megacrit.cardcrawl.rooms.MonsterRoom;
//import com.megacrit.cardcrawl.vfx.ThoughtBubble;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//public class OrbGroup {
//    public ArrayList<AbstractOrb> orbs = new ArrayList<>();
//    private int maxOrbs;
//    private Hitbox inspectHb;
//    private Hitbox hb;
//    private boolean inspectMode;
//    private ArrayList<AbstractPower> powers;
//    private float dialogX;
//    private float dialogY;
//    private boolean viewingRelics;
//    private AbstractCard hoveredCard;
//
//    void combatUpdate() {
//        for (AbstractOrb o : orbs) {
//            o.update();
//        }
//    }
//
//    public void update() {
//        this.updateControllerInput();
//        this.hb.update();
//        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.EVENT) {
//            for (AbstractOrb o : orbs) {
//                o.updateAnimation();
//            }
//        }
//    }
//
//    private void updateControllerInput() {
//        if (!Settings.isControllerMode || AbstractDungeon.topPanel.selectPotionMode || !AbstractDungeon.topPanel.potionUi.isHidden) return;
//        boolean anyHovered = false;
//        boolean orbHovered = false;
//        int orbIndex = 0;
//
//        for (AbstractOrb o : this.orbs) {
//            if (o.hb.hovered) {
//                orbHovered = true;
//                break;
//            }
//            orbIndex++;
//        }
//
//        if (orbHovered && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed())) {
//            CInputActionSet.up.unpress();
//            CInputActionSet.altUp.unpress();
//            this.inspectMode = false;
//            this.inspectHb = null;
//            this.viewingRelics = true;
//            return;
//        }
//        if (orbHovered && (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed())) {
//            orbIndex++;
//            if (orbIndex > this.orbs.size() - 1) {
//                orbIndex = 0;
//            }
//
//            this.inspectHb = this.orbs.get(orbIndex).hb;
//            Gdx.input.setCursorPosition((int) this.orbs.get(orbIndex).hb.cX, Settings.HEIGHT - (int) this.orbs.get(orbIndex).hb.cY);
//            return;
//        }
//        if (orbHovered && (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())) {
//            orbIndex--;
//            if (orbIndex < 0) {
//                orbIndex = this.orbs.size() - 1;
//            }
//
//            this.inspectHb = this.orbs.get(orbIndex).hb;
//            Gdx.input.setCursorPosition((int) this.orbs.get(orbIndex).hb.cX, Settings.HEIGHT - (int) this.orbs.get(orbIndex).hb.cY);
//            return;
//        }
//        if (this.inspectMode && (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed())) {
//            if (orbHovered) {
//                this.inspectHb = this.hb;
//                CInputHelper.setCursor(this.inspectHb);
//            } else {
//                this.inspectMode = false;
//                this.inspectHb = null;
//            }
//            return;
//        }
//        if (!this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
//            if ((float) Gdx.input.getX() < (float) Settings.WIDTH / 2.0F) {
//                this.inspectHb = this.hb;
//            } else if (AbstractDungeon.getMonsters().monsters.isEmpty()) {
//                this.inspectHb = this.hb;
//            } else {
//                ArrayList<Hitbox> hbs = new ArrayList<>();
//
//                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//                    if (!m.isDying && !m.isEscaping) {
//                        hbs.add(m.hb);
//                    }
//                }
//
//                if (!hbs.isEmpty()) {
//                    this.inspectHb = hbs.get(0);
//                } else {
//                    this.inspectHb = this.hb;
//                }
//            }
//
//            CInputHelper.setCursor(this.inspectHb);
//            this.inspectMode = true;
//            this.releaseCard();
//            return;
//        }
//        if (this.inspectMode && (CInputActionSet.right.isJustPressed()
//                || CInputActionSet.altRight.isJustPressed()) &&
//                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
//            int index;
//            ArrayList<Hitbox> hbs = new ArrayList<>();
//            hbs.add(this.hb);
//
//            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//                if (!m.isDying && !m.isEscaping) {
//                    hbs.add(m.hb);
//                }
//            }
//
//            index = 0;
//
//            for (Hitbox hitbox : hbs) {
//                hitbox.update();
//                if (hitbox.hovered) {
//                    anyHovered = true;
//                    break;
//                }
//                ++index;
//            }
//
//            if (!anyHovered) {
//                CInputHelper.setCursor(hbs.get(0));
//                this.inspectHb = hbs.get(0);
//            } else {
//                ++index;
//                if (index > hbs.size() - 1) {
//                    index = 0;
//                }
//
//                CInputHelper.setCursor(hbs.get(index));
//                this.inspectHb = hbs.get(index);
//            }
//
//            this.inspectMode = true;
//            this.releaseCard();
//            return;
//        }
//        if (this.inspectMode && (CInputActionSet.left.isJustPressed()
//                || CInputActionSet.altLeft.isJustPressed()) &&
//                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
//            int index;
//            ArrayList<Hitbox> hbs = new ArrayList<>();
//            hbs.add(this.hb);
//
//            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//                if (!m.isDying && !m.isEscaping) {
//                    hbs.add(m.hb);
//                }
//            }
//
//            index = 0;
//
//            for (Hitbox hitbox : hbs) {
//                hitbox.update();
//                if (hitbox.hovered) {
//                    anyHovered = true;
//                    break;
//                }
//                ++index;
//            }
//
//            if (!anyHovered) {
//                CInputHelper.setCursor(hbs.get(hbs.size() - 1));
//                this.inspectHb = hbs.get(hbs.size() - 1);
//            } else {
//                --index;
//                if (index < 0) {
//                    index = hbs.size() - 1;
//                }
//
//                CInputHelper.setCursor(hbs.get(index));
//                this.inspectHb = hbs.get(index);
//            }
//
//            this.inspectMode = true;
//            this.releaseCard();
//            return;
//        }
//        if (this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
//            CInputActionSet.up.unpress();
//            CInputActionSet.altUp.unpress();
//            if (!orbHovered && !this.orbs.isEmpty()) {
//                CInputHelper.setCursor(this.orbs.get(0).hb);
//                this.inspectHb = this.orbs.get(0).hb;
//            } else {
//                this.inspectMode = false;
//                this.inspectHb = null;
//                this.viewingRelics = true;
//            }
//        }
//    }
//
//
//    public void updateInput() {
//        showEvokeOrbByCard();
//    }
//
//    private void showEvokeOrbByCard() {
//        this.hoveredCard.flash(Color.SKY.cpy());
//        if (!this.hoveredCard.showEvokeValue) {
//            return;
//        }
//        if (this.hoveredCard.showEvokeOrbCount == 0) {
//            for (AbstractOrb o : this.orbs) {
//                o.showEvokeValue();
//            }
//            return;
//        }
//        for (AbstractOrb o : GetFirstOfNumOrb(this.hoveredCard.showEvokeOrbCount)) {
//            o.showEvokeValue();
//        }
//    }
//
//    private ArrayList<AbstractOrb> GetFirstOfNumOrb(int amount) {
//        return IntStream.range(0, amount).mapToObj(i -> orbs.get(i)).collect(Collectors.toCollection(ArrayList::new));
//    }
//
//    private int GetEmptyOrbNum() {
//        return (int) orbs.stream().filter(abstractOrb -> abstractOrb instanceof EmptyOrbSlot).count();
//    }
//
//
//    private void manuallySelectCard(AbstractCard card) {
//        this.hoveredCard = card;
//        this.hoveredCard.setAngle(0.0F, false);
//        showEvokeOrbByCard();
//    }
//
//    public void releaseCard() {
//        for (AbstractOrb o : this.orbs) {
//            o.hideEvokeValues();
//        }
//    }
//
//    public void preBattlePrep() {
//        this.maxOrbs = 0;
//        this.orbs.clear();
//    }
//
//    public void render(SpriteBatch sb) {
//        if ((AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoom))) return;
//        if (this.orbs.isEmpty()) return;
//        for (AbstractOrb o : this.orbs) {
//            o.render(sb);
//        }
//    }
//
//    public void triggerEvokeAnimation(int slot) {
//        if (this.maxOrbs > 0) {
//            this.orbs.get(slot).triggerEvokeAnimation();
//        }
//    }
//
//    public void evokeOrb() {
//        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
//            this.orbs.get(0).onEvoke();
//            AbstractOrb orbSlot = new EmptyOrbSlot();
//
//            int i;
//            for (i = 1; i < this.orbs.size(); ++i) {
//                Collections.swap(this.orbs, i, i - 1);
//            }
//
//            this.orbs.set(this.orbs.size() - 1, orbSlot);
//
//            for (i = 0; i < this.orbs.size(); ++i) {
//                this.orbs.get(i).setSlot(i, this.maxOrbs);
//            }
//        }
//
//    }
//
//    public void removeNextOrb() {
//        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
//            AbstractOrb orbSlot = new EmptyOrbSlot(this.orbs.get(0).cX, this.orbs.get(0).cY);
//
//            int i;
//            for (i = 1; i < this.orbs.size(); ++i) {
//                Collections.swap(this.orbs, i, i - 1);
//            }
//
//            this.orbs.set(this.orbs.size() - 1, orbSlot);
//
//            for (i = 0; i < this.orbs.size(); ++i) {
//                this.orbs.get(i).setSlot(i, this.maxOrbs);
//            }
//        }
//
//    }
//
//    public void evokeNewestOrb() {
//        if (!this.orbs.isEmpty() && !(this.orbs.get(this.orbs.size() - 1) instanceof EmptyOrbSlot)) {
//            this.orbs.get(this.orbs.size() - 1).onEvoke();
//        }
//
//    }
//
//    public void evokeWithoutLosingOrb() {
//        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
//            this.orbs.get(0).onEvoke();
//        }
//
//    }
//
//
//
//    public boolean hasEmptyOrb() {
//        return this.orbs.stream().anyMatch(abstractOrb -> abstractOrb instanceof EmptyOrbSlot);
//    }
//
//    public boolean hasOrb() {
//        if (this.orbs.isEmpty()) {
//            return false;
//        } else {
//            return !(this.orbs.get(0) instanceof EmptyOrbSlot);
//        }
//    }
//
//
//    public void channelOrb(AbstractOrb orbToSet) {
//        if (this.maxOrbs <= 0) {
//            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, AbstractPlayer.MSG[4], true));
//        } else {
//            int index = -1;
//
//            int plasmaCount;
//            for (plasmaCount = 0; plasmaCount < this.orbs.size(); ++plasmaCount) {
//                if (this.orbs.get(plasmaCount) instanceof EmptyOrbSlot) {
//                    index = plasmaCount;
//                    break;
//                }
//            }
//
//            if (index != -1) {
//                orbToSet.cX = this.orbs.get(index).cX;
//                orbToSet.cY = this.orbs.get(index).cY;
//                this.orbs.set(index, orbToSet);
//                this.orbs.get(index).setSlot(index, this.maxOrbs);
//                orbToSet.playChannelSFX();
//
//                for (AbstractPower p : this.powers) {
//                    p.onChannel(orbToSet);
//                }
//
//                orbToSet.applyFocus();
//            } else {
//                AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
//                AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
//                AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
//            }
//
//        }
//    }
//
//    public void increaseMaxOrbSlots(int amount, boolean playSfx) {
//        if (this.maxOrbs == 10) {
//            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, AbstractPlayer.MSG[3], true));
//        } else {
//            if (playSfx) {
//                CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
//            }
//
//            this.maxOrbs += amount;
//
//            int i;
//            for (i = 0; i < amount; ++i) {
//                this.orbs.add(new EmptyOrbSlot());
//            }
//
//            for (i = 0; i < this.orbs.size(); ++i) {
//                this.orbs.get(i).setSlot(i, this.maxOrbs);
//            }
//
//        }
//    }
//
//    public void decreaseMaxOrbSlots(int amount) {
//        if (this.maxOrbs > 0) {
//            this.maxOrbs -= amount;
//            if (this.maxOrbs < 0) {
//                this.maxOrbs = 0;
//            }
//            if (!this.orbs.isEmpty()) {
//                this.orbs.remove(this.orbs.size() - 1);
//            }
//
//            IntStream.range(0, this.orbs.size()).forEach(i -> this.orbs.get(i).setSlot(i, this.maxOrbs));
//        }
//    }
//
//    public void applyStartOfTurnOrbs() {
//        if (!this.orbs.isEmpty()) {
//            for (AbstractOrb o : this.orbs) {
//                o.onStartOfTurn();
//            }
//        }
//
//    }
//}
