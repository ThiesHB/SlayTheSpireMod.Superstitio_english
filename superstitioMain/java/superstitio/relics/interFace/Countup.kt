package superstitio.relics.interFace

interface Countup
{
    fun setCounter(value: Int)
    fun getCounter(): Int

    fun CountAdd()
    {
        setCounter(tryGetCounter() + 1)
        CountCheck()
    }

    fun CountCheck()
    {
        if (tryGetCounter() >= getMaxNum())
        {
            onCountMax()
            flash()
            stopPulse()
            if (ShouldRepeat())
            {
                setCounter(Integer.max(Zero, tryGetCounter() - getMaxNum()))
            }
        }
        else if (tryGetCounter() == getMaxNum() - 1)
        {
            beginLongPulse()
        }
    }

    fun onCountMax()

    fun tryGetCounter(): Int
    {
        if (getCounter() == DefaultCounterOfRelic)
        {
            setCounter(Zero)
        }
        return getCounter()
    }

    fun ShouldRepeat(): Boolean
    {
        return true
    }

    fun getMaxNum(): Int

    fun beginLongPulse()

    fun stopPulse()

    fun flash()

    companion object
    {
        const val Zero: Int = 0
        const val DefaultCounterOfRelic: Int = -1
    }
}
