package superstitio.utils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataUtility {

    public static ArrayList<hasUuid> removeMatch(ArrayList<hasUuid> listReturn, ArrayList<hasUuid> listInfo) {
        listReturn.removeIf(a -> listInfo.stream()
                .map(hasUuid::getUuid)
                .collect(Collectors.toList())
                .contains(a.getUuid()));
        return listReturn;
    }

    public interface hasUuid {
        String getUuid();
    }
}
