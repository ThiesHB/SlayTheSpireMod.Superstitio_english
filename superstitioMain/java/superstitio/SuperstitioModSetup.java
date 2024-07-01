package superstitio;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import superstitio.cards.SuperstitioCard;
import superstitio.characters.Lupa;
import superstitio.characters.Maso;
import superstitio.characters.Tzeentch;
import superstitio.customStrings.StringsSetManager;
import superstitio.customStrings.interFace.WordReplace;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

import java.util.List;
import java.util.Map;

import static com.megacrit.cardcrawl.unlock.UnlockTracker.relicSeenPref;
import static superstitio.DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD;
import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_CARD;
import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_Character;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;
import static superstitio.DataManager.SPTT_DATA.TempCardEnums.TempCard_CARD;
import static superstitio.DataManager.SPTT_DATA.TzeentchEnums.TZEENTCH_Character;
import static superstitio.customStrings.StringsSetManager.makeWordReplaceRule;

@SpireInitializer
public class SuperstitioModSetup implements
        EditStringsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
        EditCharactersSubscriber, AddAudioSubscriber, PostInitializeSubscriber {

    public static final String MOD_NAME = "Superstitio";
    public DataManager data;

    public SuperstitioModSetup() {
        BaseMod.subscribe(this);
        SuperstitioConfig.loadConfig();

        // 这里注册颜色
        data = new DataManager();
        BaseMod.addColor(LUPA_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(TempCard_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(GENERAL_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(MASO_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        Logger.run("Done " + this + " subscribing");
        Logger.run("Adding mod settings");
    }

    public static void initialize() {
        new SuperstitioModSetup();
    }

    private static void makeSFWWordForOriginStrings() {
        Map<String, RelicStrings> relicsStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");

        List<WordReplace> wordReplaces = makeWordReplaceRule();
        relicsStrings.forEach((s, Strings) -> {
            if (s.contains(DataManager.getModID().toLowerCase()) || s.contains(DataManager.getModID())) {
                Strings.FLAVOR = "";
                wordReplaces.forEach(wordReplace -> DataManager.replaceStringsInObj(Strings, wordReplace));
            }
        });

        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "relics", relicsStrings);
    }

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Maso(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.MASO_CHARACTER_PORTRAIT, MASO_Character);
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, LUPA_Character);
        BaseMod.addCharacter(new Tzeentch(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, TZEENTCH_Character);
    }

    @Override
    public void receiveEditCards() {
        //将卡牌添加
        new AutoAdd(MOD_NAME.toLowerCase())
                .packageFilter(SuperstitioCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveAddAudio() {
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_NAME.toLowerCase())
                .packageFilter(SuperstitioRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    if (relic instanceof InfoBlight.BecomeInfoBlight) {
                        InfoBlight.initInfoBlight(relic);
                        relicSeenPref.putInteger(relic.relicId, 1);
                        relicSeenPref.flush();
                        relic.isSeen = true;
                        return;
                    }
                    BaseMod.addRelic(relic, RelicType.SHARED);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        Logger.run("Beginning to edit strings for mod with ID: " + DataManager.getModID());
        StringsSetManager.loadAllStrings();
        BaseMod.loadCustomStringsFile(EventStrings.class, DataManager.makeLocalizationPath(Settings.language, "event_general"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, SuperstitioConfig.isEnableSFW() ? "character_LupaSFW" : "character_Lupa"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, "character_General"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, DataManager.makeLocalizationPath(Settings.language, "monsters"));
        if (SuperstitioConfig.isEnableSFW()) {
            makeSFWWordForOriginStrings();
        }
        Logger.run("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {
        StringsSetManager.makeKeyWords();
    }

    @Override
    public void receivePostInitialize() {
        SuperstitioConfig.setUpModOptions();
    }

}
