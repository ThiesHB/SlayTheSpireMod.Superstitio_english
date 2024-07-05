package superstitioapi.utils;


/**
 * 使用方法：在updateDescriptionArgs中放入setDescriptionArgs函数即可使用
 * 使用前需要覆盖自身的updateDescription以接入，否则不会生效
 */
public interface UpdateDescriptionAdvanced {
    void updateDescriptionArgs();

    String getDescriptionStrings();

    Object[] getDescriptionArgs();

    void setDescriptionArgs(Object... args);

    default String getFormattedDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        if (getDescriptionArgs() != null && getDescriptionArgs().length != 0)
            string = String.format(string, getDescriptionArgs());
        return string;
    }
}
