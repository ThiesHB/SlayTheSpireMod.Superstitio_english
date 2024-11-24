package superstitioapi.cardPool;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import superstitioapi.DataUtility;
import superstitioapi.Logger;
import superstitioapi.player.PlayerInitPostDungeonInitialize;
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 绑定在角色身上
 */
public interface CharacterWithCardPool extends PlayerInitPostDungeonInitialize, RenderInCharacterSelect {
    List<BaseCardPool> getCardPools();
    String getCharacterUUID();
//
//    public static class CharacterWithCardPool_Save implements java.io.Serializable {
//        private static final String CharacterWithCardPool_Save_STRING = "CharacterWithCardPool_Save";
//        public static SpireConfig config = null;
//        public static Properties theDefaultSettings = new Properties();
//        public static Gson saveFileGson = new Gson();
//        public HashMap<String, Boolean> cardPoolId_IsSelect;
//
//        public CharacterWithCardPool_Save(HashMap<String, Boolean> cardPoolId_IsSelect) {
//            this.cardPoolId_IsSelect = cardPoolId_IsSelect;
//        }
//
//        public static void loadConfig() {
//            theDefaultSettings.setProperty(CharacterWithCardPool_Save_STRING, "");
//            try {
//                config = new SpireConfig(DataUtility.getModID() + CharacterWithCardPool_Save.class.getSimpleName(),
//                        DataUtility.getModID() + CharacterWithCardPool_Save.class.getSimpleName() + "Config",
//                        theDefaultSettings);
//                config.load();
//                String tzeentchString = config.getString(CharacterWithCardPool_Save_STRING);
//                CharacterWithCardPool_Save CharacterWithCardPool_Save = saveFileGson.fromJson(tzeentchString, CharacterWithCardPool_Save.class);
//                onLoad(CharacterWithCardPool_Save);
//            } catch (Exception e) {
//                Logger.error(e);
//            }
//        }
//
//        public static void saveConfig() {
//            String tzeentchString = saveFileGson.toJsonTree(onSave(), CharacterWithCardPool_Save.class).toString();
//            config.setString(CharacterWithCardPool_Save_STRING, tzeentchString);
//            try {
//                config.save();
//            } catch (IOException e) {
//                Logger.error(e);
//            }
//        }
//
//        private static JsonElement onSaveRaw() {
//            return saveFileGson.toJsonTree(onSave());
//        }
//
//        private static void onLoadRaw(JsonElement value) {
//            if (value != null) {
//                CharacterWithCardPool_Save parsed = saveFileGson.fromJson(value, CharacterWithCardPool_Save.class);
//                onLoad(parsed);
//            } else {
//                onLoad(null);
//            }
//
//        }
//
//        private static CharacterWithCardPool_Save onSave() {
//            HashMap<String, Boolean> cardPoolData = new HashMap<>();
//            CardPoolManager.instance.cardPools.forEach(cardPool -> cardPoolData.put(cardPool.getId(), cardPool.getIsSelect()));
//            return new CharacterWithCardPool_Save(cardPoolData);
//        }
//
//        private static void onLoad(CharacterWithCardPool_Save CharacterWithCardPool_Save) {
//            if (CharacterWithCardPool_Save == null) return;
//            CharacterWithCardPool_Save.cardPoolId_IsSelect.forEach((cardPoolId, isSelect) -> {
//                CardPoolManager.instance.cardPools.forEach(cardPool -> {
//                    if (Objects.equals(cardPool.getId(), cardPoolId))
//                        cardPool.setIsSelect(isSelect);
//                });
//            });
//        }
//    }


}
