package superstitioapi.utils;


import java.util.function.Consumer;

/**
 * 使用方法：在updateDescriptionArgs中放入setDescriptionArgs函数即可使用
 * 使用前需要覆盖自身的updateDescription以接入，否则不会生效
 */
public interface UpdateDescriptionAdvanced {
    void updateDescriptionArgs();

    String getDescriptionStrings();

    Object[] getDescriptionArgs();

    void setDescriptionArgs(Object... args);

    default void setDescriptionToArgs(Consumer<Object[]> descriptionSetter, Object[] args) {
        if (args == null || args.length == 0)
            descriptionSetter.accept(new Object[0]);
        else if (args[0] instanceof Object[])
            descriptionSetter.accept((Object[]) args[0]);
        else
            descriptionSetter.accept(args);
    }

    default String getFormattedDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        if (getDescriptionArgs() != null) {
            if (getDescriptionArgs().length != 0) {
                string = String.format(string, getDescriptionArgs());
            } else {
                string = String.format(string);
            }
        }

        return string;
    }


}
