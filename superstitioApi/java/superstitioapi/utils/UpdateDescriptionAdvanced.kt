package superstitioapi.utils


/**
 * 使用方法：在updateDescriptionArgs中放入setDescriptionArgs函数即可使用
 * 使用前需要覆盖自身的updateDescription以接入，否则不会生效
 */
interface UpdateDescriptionAdvanced
{
    fun updateDescriptionArgs()

    fun getDescriptionStrings(): String

    var descriptionArgs: Array<out Any>?

//    fun updateDescription()
}

fun UpdateDescriptionAdvanced.getFormattedDescription(): String
{
    this.updateDescriptionArgs()
    var string = getDescriptionStrings()
    if (descriptionArgs != null)
    {
        string = if (descriptionArgs!!.isNotEmpty())
        {
            String.format(string, *descriptionArgs!!)
        }
        else
        {
            String.format(string)
        }
    }

    return string
}

fun UpdateDescriptionAdvanced.setDescriptionArgs(vararg args: Any)
{
    descriptionArgs = args
}
