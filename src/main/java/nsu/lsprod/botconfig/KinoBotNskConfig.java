package nsu.lsprod.botconfig;


import nsu.lsprod.botapi.KinoBotNsk;
import lombok.Getter;
import lombok.Setter;
import nsu.lsprod.botapi.TelegramInterface;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class KinoBotNskConfig {
    private String webHookPath;
    private String userName;
    private String botToken;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    @Bean
    public KinoBotNsk myTelegramBot(TelegramInterface telegramInterface) {
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

        options.setProxyHost(proxyHost);
        options.setProxyPort(proxyPort);
        options.setProxyType(proxyType);

        KinoBotNsk mySuperTelegramBot = new KinoBotNsk(options, telegramInterface);
        mySuperTelegramBot.setBotUserName(userName);
        mySuperTelegramBot.setBotToken(botToken);
        mySuperTelegramBot.setWebHookPath(webHookPath);

        return mySuperTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}