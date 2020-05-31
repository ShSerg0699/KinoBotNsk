package nsu.lsprod.cache;

import nsu.lsprod.botapi.BotState;
import nsu.lsprod.botapi.handlers.watchingschedule.UserInputData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserInputData> usersInputData = new HashMap<>();


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

    @Override
    public UserInputData getUserInputData(int userId) {
        UserInputData userInputData = usersInputData.get(userId);
        if (userInputData == null) {
            userInputData = new UserInputData();
        }
        return userInputData;
    }

    @Override
    public void saveUserInputData(int userId, UserInputData userInputData) {
        usersInputData.put(userId, userInputData);
    }
}
