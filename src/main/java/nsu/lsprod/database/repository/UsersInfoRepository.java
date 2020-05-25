package nsu.lsprod.database.repository;

import nsu.lsprod.database.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersInfoRepository extends JpaRepository<UsersInfo, Long> {
}
