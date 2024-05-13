package superstitio.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.SuperstitioModSetup;
import superstitio.cards.lupa.BaseCard.Invite;
import superstitio.cards.lupa.AttackCard.hand.Job_Hand_BaseCard;
import superstitio.cards.lupa.BaseCard.Kiss;
import superstitio.cards.lupa.BaseCard.Masturbate;
import superstitio.relics.a_starter.*;

import java.util.ArrayList;

import static superstitio.SuperstitioModSetup.LupaEnums.LUPA_CARD;
import static superstitio.SuperstitioModSetup.LupaEnums.LUPA_Character;
import static superstitio.utils.CardUtility.isNotInBattle;

// 继承CustomPlayer类
public class Lupa extends CustomPlayer {
    public static final String ID = DataManager.MakeTextID("Lupa");

    // 人物立绘
    private static final String LUPA_CHARACTER = DataManager.makeImgFilesPath_Character_Lupa("character");
    // 火堆的人物立绘（行动前）
    private static final String LUPA_CHARACTER_SHOULDER_1 = DataManager.makeImgFilesPath_Character_Lupa("shoulder1");
    // 火堆的人物立绘（行动后）
    private static final String LUPA_CHARACTER_SHOULDER_2 = DataManager.makeImgFilesPath_Character_Lupa("shoulder2");
    // 人物死亡图像
    private static final String LUPA_CORPSE_IMAGE = DataManager.makeImgFilesPath_Character_Lupa("corpse");
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
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private float offsetX;
    private float offsetY;

    public Lupa(String name) {
        super(name, LUPA_Character, EnergyBall_TEXTURES, EnergyBall_VFX_Path, LAYER_SPEED, null, null);


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

        if (!SuperstitioModSetup.getEnableSFW()) {
            this.setMoveOffset(0, -hb.height / 3.0f);
        }

        // 如果你的人物没有动画，那么这些不需要写
        // this.loadAnimation(SuperstitioModSetup.getImgFilesPath()+"char/character.atlas", SuperstitioModSetup.getImgFilesPath()+"char/character
        // .json", 1.8F);
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
        // e.setTimeScale(1.2F);
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
    }


    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if ((isNotInBattle()) || this.isDead) return;
        this.renderHealth(sb);
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

    @Override
    // 初始卡组的ID，可直接写或引用变量
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        Logger.run("Begin loading starter Deck Strings");
        for (int x = 0; x < 1; x++) {
            retVal.add(Job_Hand_BaseCard.ID);
        }
        for (int x = 0; x < 5; x++) {
            retVal.add(Kiss.ID);
        }
        for (int x = 0; x < 4; x++) {
            retVal.add(Invite.ID);
        }
        retVal.add(Masturbate.ID);
        return retVal;
    }

    @Override
    // 初始遗物
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(StartWithSexToy.ID);
        retVal.add(JokeDescription.ID);
        retVal.add(DevaBody.ID);
        retVal.add(Sensitive.ID);
//        retVal.add(SorM.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                60, // 当前血量
                60, // 最大血量
                0, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[1];
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return LUPA_CARD;
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Job_Hand_BaseCard();
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return DataManager.LUPA_DATA.LUPA_COLOR;
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
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character_Lupa("Victory1"), "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character_Lupa("Victory2")));
        panels.add(new CutscenePanel(DataManager.makeImgFilesPath_Character_Lupa("Victory3")));
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

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new Lupa(this.name);
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return DataManager.LUPA_DATA.LUPA_COLOR;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return DataManager.LUPA_DATA.LUPA_COLOR;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

}