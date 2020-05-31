package nsu.lsprod.cache;

import nsu.lsprod.botapi.BotState;
import nsu.lsprod.botapi.handlers.watchingschedule.UserInputData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserInputData getUserInputData(int userId);

    void saveUserInputData(int userId, UserInputData userProfileData);
}