package superstitio.cards.general.PowerCard.monsterGirl;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.general.AbstractTempCard;

public class KakaaGirlMode extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(KakaaGirlMode.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -2;
    private static final int MAGIC = 2;

    public KakaaGirlMode() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        addToBot_applyPower(new RitualPower(AbstractDungeon.player, this.magicNumber, true));
        AutoDoneInstantAction.addToBotAbstract(this::KakaaSound);
    }

    private void KakaaSound() {
        final int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_CULTIST_1A");
        }
        else if (roll == 1) {
            CardCrawlGame.sound.play("VO_CULTIST_1B");
        }
        else {
            CardCrawlGame.sound.play("VO_CULTIST_1C");
        }
        AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + AbstractDungeon.player.dialogX, this.hb.cY + AbstractDungeon.player.dialogY, 2.5f, Cultist.DIALOG[2], false));
    }

    @Override
    public void upgradeAuto() {
    }
}
