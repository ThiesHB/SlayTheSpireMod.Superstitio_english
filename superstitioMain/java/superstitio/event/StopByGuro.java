//package superstitio.event;
//
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.events.AbstractImageEvent;
//import com.megacrit.cardcrawl.localization.EventStrings;
//import superstitio.DataManager;
//
//public class StopByGuro extends AbstractImageEvent {
//    public static final String ID = DataManager.MakeTextID(StopByGuro.class.getSimpleName());
//    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
//    //This text should be set up through loading an event localization json file
//    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
//    private static final String[] OPTIONS = eventStrings.OPTIONS;
//    private static final String NAME = eventStrings.NAME;
//
//    public StopByGuro() {
//        super(NAME, DESCRIPTIONS[0], "img/events/winding.png");
//
//        //This is where you would create your dialog options
//        this.imageEventText.setDialogOption(OPTIONS[0]); //This adds the option to a list of options
//    }
//
//    @Override
//    protected void buttonEffect(int buttonPressed) {
//        //It is best to look at examples of what to do here in the basegame event classes, but essentially when you click on a dialog option the
//        index of the pressed button is passed here as buttonPressed.
//    }
//}