package superstitio.cards.general.AttackCard;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexualHeat;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.utils.ActionUtility;

import java.util.List;

public class CountSign extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(CountSign.class);

    public static final AbstractCard.CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_DAMAGE = 5;

    private static final int MAGIC = 3;

    private static final int UPGRADE_MAGIC = 1;

    public CountSign() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    private int getOriginDamage() {
        if (this.upgraded)
            return DAMAGE + UPGRADE_DAMAGE;
        else
            return DAMAGE;
    }

    private void updateDamage() {
        this.baseDamage = getOriginDamage();
        if (ActionUtility.isNotInBattle()) return;
        if (!SexualHeat.Orgasm.isPlayerInOrgasm()) return;
        int damageUp;
        damageUp = this.magicNumber * InBattleDataManager.OrgasmTimesTotal;
//        if (damageUp >= 1)
//            this.isDamageModified = true;
        this.baseDamage = this.getOriginDamage() + damageUp;
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        List<TooltipInfo> customTooltipsTop = super.getCustomTooltipsTop();
        customTooltipsTop.add(new TooltipInfo(this.name, String.format(this.cardStrings.getEXTENDED_DESCRIPTION(0),
                InBattleDataManager.OrgasmTimesTotal)));
        return customTooltipsTop;
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        updateDamage();
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDamage();
    }

    @Override
    public void onMoveToDiscard() {
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(final AbstractMonster monster) {
        updateDamage();
        super.calculateCardDamage(monster);
        this.initializeDescription();
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        if (SexualHeat.Orgasm.isPlayerInOrgasm()) {
            this.glowColor = Color.PINK.cpy();
        }
    }
}
