package nsu.lsprod.database.service;

import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.UsersInfo;
import nsu.lsprod.database.repository.CinemaRepository;
import nsu.lsprod.database.repository.UsersInfoRepository;

import java.util.ArrayList;
import java.util.List;

public class UserInfoService {
    private final UsersInfoRepository usersInfoRepository;

    public UserInfoService(UsersInfoRepository usersInfoRepository) {
        this.usersInfoRepository = usersInfoRepository;
    }

    public List<Cinema> getUserCinema(Long userId) {
        List<Cinema> favoriteCinemas = new ArrayList<>();
        UsersInfo userInfo = usersInfoRepository.findByUserId(userId);
        return  userInfo.getSelectedCinemas();
    }


    public void addUsersInfo(UsersInfo usersInfo){
        UsersInfo userInfo = usersInfoRepository.findByUserId(usersInfo.getUserId());
        usersInfoRepository.save(userInfo);
    }

}
