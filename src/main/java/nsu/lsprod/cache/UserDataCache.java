package nsu.lsprod.cache;

import nsu.lsprod.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
//    private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.SHOW_MAIN_MENU;
        }

        return botState;
    }

//    @Override
//    public UserProfileData getUserProfileData(int userId) {
//        UserProfileData userProfileData = usersProfileData.get(userId);
//        if (userProfileData == null) {
//            userProfileData = new UserProfileData();
//        }
//        return userProfileData;
//    }
//
//    @Override
//    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
//        usersProfileData.put(userId, userProfileData);
//    }
}
