package SuperstitioMod;

import SuperstitioMod.cards.Lupa.Skill.CalmDown;
import SuperstitioMod.cards.Lupa.base.BaseSkill_Lupa;
import SuperstitioMod.cards.Lupa.base.Defend_Lupa;
import SuperstitioMod.cards.Lupa.base.Strike_Lupa;
import SuperstitioMod.characters.Lupa;
import SuperstitioMod.relics.Sensitive;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



@SpireInitializer
public class SuperstitioModSetup implements EditStringsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
        EditCharactersSubscriber, AddAudioSubscriber, PostInitializeSubscriber, PostExhaustSubscriber, StartGameSubscriber, PostUpdateSubscriber,
        RelicGetSubscriber, PostPowerApplySubscriber, PostBattleSubscriber, PostDungeonInitializeSubscriber,

        OnCardUseSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber, PostEnergyRechargeSubscriber {

    private static final String MOD_ID = "SuperstitioMod";
    public static final Logger logger = LogManager.getLogger(SuperstitioModSetup.class.getName());
    public static final Color MY_COLOR = new Color(79.0F / 255.0F, 185.0F / 255.0F, 9.0F / 255.0F, 1.0F);
    //选英雄界面的角色图标、选英雄时的背景图片
    private static final String MY_CHARACTER_BUTTON = getImgFilesPath() + "char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = getImgFilesPath() + "char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = getImgFilesPath() + "512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = getImgFilesPath() + "512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = getImgFilesPath() + "512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = getImgFilesPath() + "char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = getImgFilesPath() + "1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = getImgFilesPath() + "1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = getImgFilesPath() + "1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = getImgFilesPath() + "char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = getImgFilesPath() + "char/cost_orb.png";

    public SuperstitioModSetup() {
        BaseMod.subscribe(this);
        // 这里注册颜色
        BaseMod.addColor(Lupa.Enums.LUPA_CARD, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512, BG_SKILL_512,
                BG_POWER_512, ENEYGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }

    public static void initialize() {
        new SuperstitioModSetup();
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename) {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret = ret + "zhs/";
                break;
            case KOR:
                ret = ret + "kor/";
                break;
            default:
                ret = ret + "eng/";
        }

        return getModID() + "Resources/" + ret + filename + ".json";
    }

    private static String getModID() {
        return MOD_ID;
    }

//    public static String getCardPath(String resourcePath) {
//        return getImgFilesPath()+"cards/" + resourcePath + ".png";
//    }

    public static String getImgFilesPath() {
        return getModID() + "Resources/img/";
    }

    public static String getRelicPath(String resourcePath) {
        return getImgFilesPath() + "relics/" + resourcePath + ".png";
    }

    public static String getRelicOutlinePath(String resourcePath) {
        return getImgFilesPath() + "relics/outline/" + resourcePath + ".png";
    }

    public static String getOrbPath(String resourcePath) {
        return getImgFilesPath() + "orbs/" + resourcePath;
    }

    public static String getPowerPath(String resourcePath) {
        return getImgFilesPath() + "powers/" + resourcePath;
    }

    public static String getEventPath(String resourcePath) {
        return getImgFilesPath() + "events/" + resourcePath;
    }

    public static String MakeTextID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, Lupa.Enums.LUPA_Character);
    }

    @Override
    public void receiveEditCards() {
        //将卡牌添加
        BaseMod.addCard(new Strike_Lupa());
        BaseMod.addCard(new Defend_Lupa());
        BaseMod.addCard(new BaseSkill_Lupa());
        BaseMod.addCard(new CalmDown());
    }

    @Override
    public void receiveAddAudio() {
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new Sensitive(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(Settings.language, "card_Lupa"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocPath(Settings.language, "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(Settings.language, "relic_Lupa"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(Settings.language, "power"));
//        logger.info("card strings path: " + makeLocPath(Settings.language, "DefaultMod-Card-Strings"));
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language, "DefaultMod-Event-Strings"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language, "DefaultMod-Potion-Strings"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language, "DefaultMod-Orb-Strings"));
//        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(Settings.language, "UIStrings"));
        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {
//        Gson gson = new Gson();
//        String json = Gdx.files.internal(makeLocPath(Settings.language, "DefaultMod-Keyword-Strings")).readString(String.valueOf(StandardCharsets
//        .UTF_8));
//        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
//        if (keywords != null) {
//
//            for (Keyword keyword : keywords) {
//                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
//            }
//        }

    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
    }

    @Override
    public void receivePostInitialize() {
    }

    @Override
    public void receivePostUpdate() {
    }

    @Override
    public void receiveStartGame() {
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {

    }

    @Override
    public void receivePowersModified() {

    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {

    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {

    }

    @Override
    public void receivePostDungeonInitialize() {

    }

    @Override
    public void receivePostEnergyRecharge() {

    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {

    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {

    }

    /**
     * 只输出后面的id，不携带模组信息
     * @param complexId 带有模组信息的ID，如“xxxMod:xxx”
     * @return 只输出冒号后面的部分
     */
    public static String getIdOnly(String complexId){
        Matcher matcher =Pattern.compile(":(.*)").matcher(complexId);
        if (matcher.find())
            return matcher.group(1).trim();
       return complexId;
    }

}
