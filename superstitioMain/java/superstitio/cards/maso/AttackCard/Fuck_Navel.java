package superstitio.cards.maso.AttackCard;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.actions.DamageAllEnemiesAction;
import superstitioapi.cards.DamageActionMaker;

import java.util.Map;

import static superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard;


public class Fuck_Navel extends MasoCard implements FuckJob_Card, OnOrgasm_onOrgasm, IsNotLupaCard {
    public static final String ID = DataManager.MakeTextID(Fuck_Navel.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL;

    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5;

    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
//    private int orgasmTimes;

    public Fuck_Navel() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.isMultiDamage = true;
        CardModifierManager.addModifier(this, new RetainMod());
//        this.orgasmTimes = 0;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
//        addToBot_dealDamageToAllEnemies(SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
//        addToBot_dealDamage(AbstractDungeon.player, SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
        Map<AbstractCreature, Integer> damageMap = DamageAllEnemiesAction.Builder.GetDamageMapFromMultiDamagesOfCard(this.multiDamage);
        this.calculateSingleDamage(AbstractDungeon.player, AbstractDungeon.player);
        damageMap.put(AbstractDungeon.player, this.damage);
        DamageActionMaker.makeDamages(damageMap)
                .setSource(AbstractDungeon.player)
                .setDamageType(DamageInfo.DamageType.NORMAL)
                .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                .setExampleCard(this)
                .addToBot();

        addToBot_applyPower(new VulnerablePower(AbstractDungeon.player, 1, false));
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        if (AbstractDungeon.player.hand.contains(this)) {
            this.flash();
            this.setupDamage(this.baseDamage + this.magicNumber);
        }
    }
}
