package com.football.utilities;

import android.content.Context;

import com.football.fantasy.R;

import static com.football.models.responses.PlayerResponse.POSITION_ATTACKER;
import static com.football.models.responses.PlayerResponse.POSITION_DEFENDER;
import static com.football.models.responses.PlayerResponse.POSITION_GOALKEEPER;
import static com.football.models.responses.PlayerResponse.POSITION_MIDFIELDER;

public class PlayerUtils {

    public static String getPositionText(Context context, int position, boolean fullText) {
        String result = "";
        switch (position) {
            case POSITION_GOALKEEPER:
                result = fullText ? context.getString(R.string.goalkeeper) : "G";
                break;
            case POSITION_DEFENDER:
                result = fullText ? context.getString(R.string.defender) : "D";
                break;
            case POSITION_MIDFIELDER:
                result = fullText ? context.getString(R.string.midfielder) : "M";
                break;
            case POSITION_ATTACKER:
                result = fullText ? context.getString(R.string.attacker) : "A";
                break;
        }

        return result;
    }
}
