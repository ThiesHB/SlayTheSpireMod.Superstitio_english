package superstitioapi.relicToBlight.customSave;

import basemod.abstracts.CustomSavableRaw;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import superstitioapi.relicToBlight.BlightWithRelic;

import static superstitioapi.relicToBlight.customSave.SuperstitioApiModSaves.ArrayListOfJsonElement;
import static superstitioapi.relicToBlight.customSave.SuperstitioApiModSaves.modBlightSaves;

@SpirePatch(clz = SaveFile.class, method = "<ctor>", paramtypez = {SaveFile.SaveType.class})
public class InfoBlightConstructSaveFilePatch {
    @SpirePostfixPatch
    public static void Prefix(final SaveFile __instance, final SaveFile.SaveType saveType) {
        final ArrayListOfJsonElement blightSaves = new ArrayListOfJsonElement();
        for (AbstractBlight blight : AbstractDungeon.player.blights) {
            if (blight instanceof BlightWithRelic
                    && ((BlightWithRelic) blight).relic instanceof CustomSavableRaw)
                blightSaves.add(((CustomSavableRaw) ((BlightWithRelic) blight).relic).onSaveRaw());
            else {
                blightSaves.add(null);
            }
        }
        modBlightSaves.set(__instance, blightSaves);
    }
}
