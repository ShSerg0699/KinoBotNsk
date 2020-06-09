package nsu.lsprod.database.service;

import junit.framework.TestCase;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.UsersInfo;
import nsu.lsprod.database.repository.UsersInfoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceTest extends TestCase {
    @Mock
    UsersInfoRepository usersInfoRepository;
    @Mock
    UsersInfo usersInfo;

    @InjectMocks
    UserInfoService userInfoService = new UserInfoService(usersInfoRepository);

    @Test
    public void test(){
        List<Cinema> expected = new ArrayList<>();
        Mockito.when(usersInfo.getUserId()).thenReturn(1L);
        Mockito.when(usersInfoRepository.findByUserId(1L)).thenReturn(usersInfo);
        userInfoService.addUsersInfo(usersInfo);
        Mockito.when(usersInfo.getSelectedCinemas()).thenReturn(expected);
        Assert.assertEquals(expected, userInfoService.getUserCinema(1L));
    }

}

