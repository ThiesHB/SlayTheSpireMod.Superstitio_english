package superstitio.cards.maso.SkillCard;

import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.DamageActionMaker;
import superstitio.cards.maso.MasoCard;
import superstitio.utils.ActionUtility;

//拔指甲/趾甲
public class NailExtraction extends MasoCard {
    public static final String ID = DataManager.MakeTextID(NailExtraction.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int DAMAGE = 1;
    private static final int MAGIC = 2;
    //    private static final int UPGRADE_MAGIC = 1;
    private static final int DRAW_CARD = 2;
    private static final int COPY_SELF = 2;
    private static final int MAX_IN_TURN = 20;


    public NailExtraction() {
        this(false);
    }

    public NailExtraction(boolean isPreview) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        if (!isPreview)
            this.cardsToPreview = new NailExtraction(true);
        this.setupMagicNumber(MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
        CardModifierManager.addModifier(this, new EtherealMod());
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(DRAW_CARD, COPY_SELF, MAX_IN_TURN);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        DamageActionMaker damageActionMaker = DamageActionMaker.maker(this.magicNumber, AbstractDungeon.player)
                .setEffect(AbstractGameAction.AttackEffect.NONE);
        if (!upgraded)
            damageActionMaker.setDamageType(DamageInfo.DamageType.HP_LOSS).addToBot();
        else
            damageActionMaker.setDamageType(DamageInfo.DamageType.NORMAL).addToBot();
        addToBot_drawCards(DRAW_CARD);
        ActionUtility.addToBot_makeTempCardInBattle(new NailExtraction(), BattleCardPlace.DrawPile, COPY_SELF, this.upgraded);
        InBattleDataManager.NailExtractionPlayedInTurn++;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (InBattleDataManager.NailExtractionPlayedInTurn >= 20)
            return false;
        return super.canUse(p, m);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
