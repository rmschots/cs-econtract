package be.cipalschaubroeck.econtract.repository;

import be.cipalschaubroeck.econtract.domain.SignSessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignSessionDataRepository extends JpaRepository<SignSessionData, String> {
}
