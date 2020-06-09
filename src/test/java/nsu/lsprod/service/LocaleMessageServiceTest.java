package nsu.lsprod.service;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class LocaleMessageServiceTest extends TestCase {
    @Mock
    Locale locale;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    LocaleMessageService localeMessageService = new LocaleMessageService("localeTag", messageSource);

    public void testGetMessage() {
    }

    public void testTestGetMessage() {
    }
}