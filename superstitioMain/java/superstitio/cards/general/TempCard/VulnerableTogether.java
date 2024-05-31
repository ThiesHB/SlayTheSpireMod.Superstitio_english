package superstitio.cards.general.TempCard;

import basemod.cardmods.ExhaustMod;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;
import superstitioapi.utils.ActionUtility;

import java.util.Arrays;


public class VulnerableTogether extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(VulnerableTogether.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.ALL;
    private static final int COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public VulnerableTogether() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
        CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new VulnerablePower(AbstractDungeon.player, 1, false));
        Arrays.stream(ActionUtility.getAllAliveMonsters()).forEach(creature -> {
            addToBot_applyPower(new VulnerablePower(creature, 1, false));
        });
    }

    @Override
    public void upgradeAuto() {
    }
}
