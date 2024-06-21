package superstitio.cards.maso.SkillCard;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_AtStartOfTurn;
import superstitioapi.utils.ActionUtility;

//炮烙
public class HotPillar extends MasoCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(HotPillar.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
//    private static final int BLOCK = 18;
//    private static final int UPGRADE_BLOCK = 4;

    public HotPillar() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.tags.add(DataManager.CardTagsType.CruelTorture);
        this.cardsToPreview = new Burn();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addTempDexterity(this);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        HotPillar self = this;
        new CardOrb_AtStartOfTurn(this, cardGroup, 99, cardOrbAtStartOfTurn -> {
            cardOrbAtStartOfTurn.StartHitCreature(AbstractDungeon.player);
            addTempDexterity(self);
        })
                .addToBot_HangCard();
    }

    private void addTempDexterity(HotPillar self) {
        self.addToBot_applyPower(new DexterityPower(AbstractDungeon.player, this.magicNumber));
        self.addToBot_applyPower(new LoseDexterityPower(AbstractDungeon.player, this.magicNumber));
        ActionUtility.addToBot_makeTempCardInBattle(new Burn(), ActionUtility.BattleCardPlace.Discard);
    }
}
