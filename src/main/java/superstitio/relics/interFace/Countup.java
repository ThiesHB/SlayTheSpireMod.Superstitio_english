package superstitio.relics.interFace;

public interface Countup {
    int Zero = 0;
    int DefaultCounterOfRelic = -1;

    int getCounter();

    void setCounter(int CounterNum);

    default void CountAdd() {
        setCounter(tryGetCounter() + 1);
        CountCheck();
    }

    default void CountCheck() {
        if (tryGetCounter() >= getMaxNum()) {
            onCountMax();
            flash();
            stopPulse();
            if (ShouldRepeat()) {
                setCounter(Integer.max(Zero, tryGetCounter() - getMaxNum()));
            }
        }
        else if (tryGetCounter() == getMaxNum() - 1) {
            beginLongPulse();
        }
    }

    void onCountMax();

    default int tryGetCounter() {
        if (getCounter() == DefaultCounterOfRelic) {
            setCounter(Zero);
        }
        return getCounter();
    }

    default boolean ShouldRepeat() {
        return true;
    }

    int getMaxNum();

    void beginLongPulse();

    void stopPulse();

    void flash();
}
