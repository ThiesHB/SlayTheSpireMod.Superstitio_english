package superstitio.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

public class LingeringStance extends AbstractStance {
    public static final String STANCE_ID = "Lingering";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;

    public LingeringStance() {
        this.ID = STANCE_ID;
        this.name = LingeringStance.stanceString.NAME;
        this.updateDescription();
    }

    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0f) {
                this.particleTimer = 0.2f;
                AbstractDungeon.effectsQueue.add(new DivinityParticleEffect());
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0f) {
            this.particleTimer2 = MathUtils.random(0.45f, 0.55f);
            AbstractDungeon.effectsQueue.add(new StanceAuraEffect(STANCE_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
    }

    @Override
    public void updateDescription() {
        this.description = LingeringStance.stanceString.DESCRIPTION[0];
    }

    @Override
    public void onEnterStance() {
        if (LingeringStance.sfxId != -1L) {
            this.stopIdleSfx();
        }
        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        LingeringStance.sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, STANCE_ID));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(3));
    }

    @Override
    public void onPlayCard(AbstractCard card) {

    }

    //@Override


    @Override
    public void onExitStance() {
        this.stopIdleSfx();
    }

    @Override
    public void stopIdleSfx() {
        if (LingeringStance.sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", LingeringStance.sfxId);
            LingeringStance.sfxId = -1L;
        }
    }

}
