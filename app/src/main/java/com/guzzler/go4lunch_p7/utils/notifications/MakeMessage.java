package com.guzzler.go4lunch_p7.utils.notifications;

import java.util.List;

public class MakeMessage {

    public static StringBuilder makeMessage(List<String> workmatesList) {
        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < workmatesList.size(); i++) {
            mStringBuilder.append(workmatesList.get(i));
            // tant qu'on est pas au dernier membre de la liste on rajoute la virgule
            if (!(i == workmatesList.size() - 1)) {
                mStringBuilder.append(", ");
            }
        }
        return mStringBuilder;
    }
}