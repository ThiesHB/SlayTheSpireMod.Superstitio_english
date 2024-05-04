package SuperstitioMod.cards.BlockMod;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.DamageMod.SexDamage;
import SuperstitioMod.customStrings.BlockModifierWithSFW;
import SuperstitioMod.customStrings.HasSFWVersion;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class SexBlock extends AbstractBlockModifier {

    public static final String ID = DataManager.MakeTextID(SexDamage.class.getSimpleName());
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SexBlock() {
        this(true);
    }

    public SexBlock(boolean autoBind) {
        this.automaticBindingForCards = autoBind;
    }

    public static BlockModifierWithSFW getBlockModifierWithSFW(String cardName) {
        try {
            return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.block_modifiers, BlockModifierWithSFW.class);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int amountLostAtStartOfTurn() {
        return 5;
    }

    @Override
    public void onStartOfTurnBlockLoss(int blockLost) {
        this.addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, blockLost)));
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new SexBlock();
    }
}