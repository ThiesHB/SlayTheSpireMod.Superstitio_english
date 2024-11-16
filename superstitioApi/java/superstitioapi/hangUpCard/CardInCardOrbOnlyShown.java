//package superstitioapi.hangUpCard;
//
//import basemod.abstracts.CustomCard;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import superstitioapi.cards.HasMultiCardsToPreview;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CardInCardOrbOnlyShown extends CustomCard implements HasMultiCardsToPreview {
//    private final AbstractCard copyCard;
//    private final List<AbstractCard> multiCardsToPreview = new ArrayList<>();
//    private float cardsToPreviewTimer = 0.0f;
//
//    public CardInCardOrbOnlyShown(AbstractCard copyCard, String rawDescription) {
//        super("SuperstitioApi:CardInCardOrb",
//                copyCard.name,
//                copyCard.assetUrl,
//                copyCard.cost,
//                rawDescription.isEmpty() ? copyCard.rawDescription : rawDescription,
//                copyCard.type,
//                copyCard.color,
//                copyCard.rarity,
//                copyCard.target);
//        this.copyCard = copyCard;
//        this.multiCardsToPreview.add(copyCard);
//        if (this.copyCard instanceof HasMultiCardsToPreview)
//            this.multiCardsToPreview.addAll(((HasMultiCardsToPreview) this.copyCard).getMultiCardsToPreview());
//        else if (this.copyCard.cardsToPreview != null) {
//            this.multiCardsToPreview.add(this.copyCard.cardsToPreview);
//        }
//    }
//
//    public void synchronous(){
//        this.magicNumber = this.copyCard.magicNumber;
//        this.baseMagicNumber = this.copyCard.baseMagicNumber;
//
//    }
//
//    @Override
//    public void upgrade() {
//        copyCard.upgrade();
//    }
//
//    @Override
//    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
//    }
//
//    @Override
//    public List<AbstractCard> getMultiCardsToPreview() {
//        return this.multiCardsToPreview;
//    }
//
//    @Override
//    public float getCardsToPreviewTimer() {
//        return this.cardsToPreviewTimer;
//    }
//
//    @Override
//    public void setCardsToPreviewTimer(float cardsToPreviewTimer) {
//        this.cardsToPreviewTimer = cardsToPreviewTimer;
//    }
//
//    @Override
//    public void setCardsToPreview(AbstractCard cardsToPreview) {
//        this.cardsToPreview = cardsToPreview;
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        HasMultiCardsToPreview.super.update();
//    }
//
//
//}
