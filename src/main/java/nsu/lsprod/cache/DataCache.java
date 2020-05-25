package nsu.lsprod.cache;

import nsu.lsprod.botapi.BotState;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

//    UserProfileData getUserProfileData(int userId);
//
//    void saveUserProfileData(int userId, UserProfileData userProfileData);
}