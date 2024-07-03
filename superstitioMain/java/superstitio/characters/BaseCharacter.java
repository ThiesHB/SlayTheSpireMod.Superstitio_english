package superstitio.characters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.SuperstitioConfig;
import superstitio.cards.general.BaseCard.Kiss;
import superstitioapi.utils.TipsUtility;

import java.util.ArrayList;

import static superstitio.DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD;
import static superstitioapi.utils.ActionUtility.isNotInBattle;

// 继承CustomPlayer类
public abstract class BaseCharacter extends CustomPlayer {
    // 人物立绘
    public static final String LUPA_CHARACTER = DataManager.makeImgFilesPath_Character("character");
    // 火堆的人物立绘（行动前）
    public static final String LUPA_CHARACTER_SHOULDER_1 = DataManager.makeImgFilesPath_Character("shoulder1");
    // 火堆的人物立绘（行动后）
    public static final String LUPA_CHARACTER_SHOULDER_2 = DataManager.makeImgFilesPath_Character("shoulder2");
    // 人物死亡图像
    public static final String LUPA_CORPSE_IMAGE = DataManager.makeImgFilesPath_Character("corpse");
    public static final UIStrings GuroText = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("GuroText"));
    public static final PowerTip GuroTip = new PowerTip(GuroText.TEXT[0], GuroText.TEXT[1]);
    private static final String EnergyBall_Path = "EnergyBall_Lupa/";
    private static final String EnergyBall_VFX_Path = DataManager.makeImgFilesPath_UI(EnergyBall_Path + "vfx");
    // 战斗界面左下角能量图标的每个图层
    private static final String[] EnergyBall_TEXTURES = new String[]{
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer5"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer4"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer3"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer2"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer1"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer6"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer5d"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer4d"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer3d"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer2d"),
            DataManager.makeImgFilesPath_UI(EnergyBall_Path + "layer1d")};
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};
    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    protected final CharacterStrings characterStrings;
    private float offsetX;
    private float offsetY;
    private float simpleAnim = 0.0f;

    public BaseCharacter(String ID, String name, PlayerClass playerClass) {
        super(name, playerClass, EnergyBall_TEXTURES, EnergyBall_VFX_Path, LAYER_SPEED, null, null);
        characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
        this.title = getTitle(playerClass);


        // 人物对话气泡的大小，如果游戏中尺寸不对在这里修改（libgdx的坐标轴左下为原点）
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 150.0F * Settings.scale);


        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                LUPA_CHARACTER, // 人物图片
                LUPA_CHARACTER_SHOULDER_2, LUPA_CHARACTER_SHOULDER_1, LUPA_CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(), 0.0F, 0.0F, 250.0F, 375.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );

        if (!SuperstitioConfig.isEnableSFW()) {
            this.setMoveOffset(0, -hb.height / 3.0f);
        }

        // 如果你的人物没有动画，那么这些不需要写
        // this.loadAnimation(SuperstitioModSetup.getImgFilesPath()+"char/character.atlas", SuperstitioModSetup.getImgFilesPath()+"char/character
        // .json", 1.8F);
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
        // e.setTimeScale(1.2F);
    }

    protected static void unableByGuroSetting() {
        if (!SuperstitioConfig.isEnableGuroCharacter()) {
            try {
                CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isDisabled = true;
                if (CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isHovered) {
                    TipsUtility.renderTipsWithMouse(GuroTip);
                }
            } catch (Exception e) {
                Logger.warning("no confirmButton found");
            }
        }
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
        ArrayList<AbstractCard> originCardPool = super.getCardPool(tmpPool);
        addCardByCardFilter(originCardPool);
        return originCardPool;
    }

    protected void addCardByCardFilter(ArrayList<AbstractCard> originCardPool) {
        CardLibrary.cards.forEach(((string, card) -> {
            if (UnlockTracker.isCardLocked(string) && !Settings.treatEverythingAsUnlocked())
                return;
            if (card.rarity == AbstractCard.CardRarity.BASIC)
                return;
            if (originCardPool.contains(card))
                return;
            if (isCardCanAdd(card)) {
                originCardPool.add(card);
            }
        }));
    }

    protected abstract boolean isCardCanAdd(AbstractCard card);

    @Override
    public void update() {
        super.update();
        simpleAnim += Gdx.graphics.getDeltaTime();
        if (simpleAnim >= 1.0f)
            simpleAnim = 0;
    }

    @Override
    public void render(SpriteBatch sb) {
        this.stance.render(sb);

        if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
            sb.setColor(Color.WHITE);
            this.renderShoulderImg(sb);
            return;
        }
        if (this.atlas != null && !(boolean) ReflectionHacks.getPrivate(this, AbstractCreature.class, "renderCorpse")) {
            this.renderPlayerImage(sb);
        } else {
            sb.setColor(Color.WHITE);
            drawImg(sb);
        }

        this.hb.render(sb);
        this.healthHb.render(sb);
        if ((isNotInBattle()) || this.isDead) return;
        this.renderHealth(sb);
        if (!this.orbs.isEmpty()) {
            for (AbstractOrb o : this.orbs) {
                o.render(sb);
            }
        }
    }

    private void drawImg(SpriteBatch sb) {
        float scaleX = 1.0f;
        float v = 0.005f * MathUtils.sinDeg(MathUtils.sinDeg(simpleAnim * 360) * 15);
        float scaleY = 1.0f + v;
        float rotation = 0;
        sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX,
                this.drawY + this.hb.height * v,
                0, 0,
                (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale,
                scaleX, scaleY, rotation,
                0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
    }

    public void setMoveOffset(float x, float y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    @Override
    public void movePosition(float x, float y) {
        super.movePosition(x + offsetX, y + offsetY);
    }

    @Override
    protected void refreshHitboxLocation() {
        super.refreshHitboxLocation();
        this.hb.move(this.hb.cX - this.offsetX, this.hb.cY - this.offsetY);
        this.healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        if (characterStrings == null)
            return "[MISSING_Title]";
        return characterStrings.NAMES[1];
    }

    public ArrayList<AbstractCard.CardColor> getOtherAddedCardColors() {
        ArrayList<AbstractCard.CardColor> cardColors = new ArrayList<>();
        cardColors.add(GENERAL_CARD);
        return cardColors;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Kiss();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return DataManager.SPTT_DATA.SEX_COLOR;
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("SELECT_WATCHER", MathUtils.random(-0.15F, 0.15F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character("Victory1"), "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character("Victory2")));
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character("Victory3")));
        return panels;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return DataManager.SPTT_DATA.SEX_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return DataManager.SPTT_DATA.SEX_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    public static class CharacterSelectInfo {
        public int currentHp;
        public int maxHp;
        public int gold;

        public CharacterSelectInfo(int currentHp, int maxHp, int gold) {
            this.currentHp = currentHp;
            this.maxHp = maxHp;
            this.gold = gold;
        }

        public String makeHpString() {
            return currentHp + "/" + maxHp;
        }

//        public void refreshCharacterSelectScreen(CharacterOption characterOption) {
//
//        }
    }

}