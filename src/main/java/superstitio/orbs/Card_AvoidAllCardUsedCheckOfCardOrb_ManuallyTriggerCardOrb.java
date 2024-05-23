package superstitio.orbs;

import superstitio.hangUpCard.CardOrb_CardTrigger;

public interface Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb {
    boolean forceFilterCardOrbToHoveredMode(CardOrb_CardTrigger orb);

    int forceChangeOrbCounterShown(CardOrb_CardTrigger orb);
}
