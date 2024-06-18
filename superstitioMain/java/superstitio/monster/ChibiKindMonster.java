package superstitio.monster;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import superstitio.DataManager;
import superstitioapi.pet.MinionMonster;
import superstitioapi.utils.CardUtility;
import superstitioapi.utils.CreatureUtility;
import superstitioapi.utils.ListUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

public class ChibiKindMonster extends CustomMonster {
    public static final String ID = DataManager.MakeTextID(ChibiKindMonster.class);
    public static final String[] CHIBI_CHARACTER = new String[]{
            DataManager.makeImgFilesPath_Character("chibiCharacter0"),
            DataManager.makeImgFilesPath_Character("chibiCharacter1"),
            DataManager.makeImgFilesPath_Character("chibiCharacter2")
    };
    private static final List<AbstractCard> willPlayCards = new ArrayList<>();
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String STRIKE = MOVES[0];
    public static final String DEFEND = MOVES[1];
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public ChibiKindMonster() {
        super(NAME, ID, AbstractDungeon.player.maxHealth, 0, 0, 250.0f, 250.0f, ListUtility.getRandomFromList(CHIBI_CHARACTER, new Random()));
        this.setHp(AbstractDungeon.player.maxHealth);
        this.animX = 0.0f;
        this.animY = 0.0f;
        this.type = EnemyType.ELITE;
        this.dialogX = -70.0F * Settings.scale;
        this.dialogY = 50.0F * Settings.scale;
        this.damage.add(new DamageInfo(this, 6));

        if (img == null) return;
        this.hb_w = img.getWidth() * Settings.scale;
        this.hb_h = img.getHeight() * Settings.xScale;
//        this.hb_x = hb_x * Settings.scale;
//        this.hb_y = hb_y * Settings.scale;

//        this.intentHb = new Hitbox(INTENT_HB_W, INTENT_HB_W);
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.healthHb = new Hitbox(this.hb_w, 72.0F * Settings.scale);
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
    }

    @Override
    public void takeTurn() {
        this.flashIntent();
        switch (this.intent) {
            case ATTACK:
//                addToBotAbstract(() -> {
//                if (!playStrike())
//                    playDefend();
//                });
                playStrike();
                break;
            case DEFEND:
                playDefend();
//                addToBotAbstract(() -> {
//                if (!playDefend())
//                    playStrike();
//                });
                break;
        }
        addToBot(new RollMoveAction(this));
//        addToBotAbstract(() ->
        addToBotAbstract(willPlayCards::clear, 2);
//        );
    }

    private boolean playStrike() {
        Optional<AbstractCard> strike = CardUtility.AllCardInBattle_ButWithoutCardInUse().stream()
                .filter(card -> !willPlayCards.contains(card)).filter(card -> card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)).findAny();
        strike.ifPresent(card -> {
            willPlayCards.add(card);
//            addToBot(new UseCardAction(card));
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, CreatureUtility.getRandomMonsterSafe(), 0, true, true));
        });
        return strike.isPresent();
    }

    private boolean playDefend() {
        Optional<AbstractCard> defend = CardUtility.AllCardInBattle_ButWithoutCardInUse().stream()
                .filter(card -> !willPlayCards.contains(card)).filter(card -> card.hasTag(AbstractCard.CardTags.STARTER_DEFEND)).findAny();
        defend.ifPresent(card -> {
            willPlayCards.add(card);
//            card.dontTriggerOnUseCard
//            addToBot(new UseCardAction(card));
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, CreatureUtility.getRandomMonsterSafe(), 0, true, true));
        });
        return defend.isPresent();
    }

    @Override
    public void update() {
        super.update();
    }

    @SpireOverride
    protected void updateIntentTip() {
        PowerTip intentTip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
        switch (this.nextMove) {
            case 1:
                intentTip.header = DIALOG[0];
                intentTip.body = DIALOG[1];
                intentTip.img = ImageMaster.INTENT_ATK_TIP_1;
                break;
            case 2:
                intentTip.header = DIALOG[2];
                intentTip.body = DIALOG[3];
                intentTip.img = ImageMaster.INTENT_DEFEND;
                break;
            default:
                intentTip.header = "NOT SET";
                intentTip.body = "NOT SET";
                intentTip.img = ImageMaster.INTENT_UNKNOWN;
        }
    }

    @Override
    protected void getMove(int num) {
        if (num < 50) {
            this.setMove(STRIKE, (byte) 1, Intent.ATTACK, 6);
        } else {
            this.setMove(DEFEND, (byte) 2, Intent.DEFEND);
        }
    }

    public static class MinionChibi extends MinionMonster {

        private static final float CHIBI_DRAW_SCALE = 2.0f;
        private final ChibiKindMonster petCoreChibi;

        public MinionChibi(ChibiKindMonster petCoreChibi) {
            super(petCoreChibi, CHIBI_DRAW_SCALE);
            this.petCoreChibi = petCoreChibi;
            this.flipHorizontal = false;
        }

        @Override
        public void takeTurn() {
            petCoreChibi.takeTurn();
        }

        @Override
        protected void getMove(int num) {
            super.getMove(num);
//            petCoreChibi.getMove(num);
        }
    }
}
