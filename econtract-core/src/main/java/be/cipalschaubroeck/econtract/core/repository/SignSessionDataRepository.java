package be.cipalschaubroeck.econtract.core.repository;

import be.cipalschaubroeck.econtract.core.domain.SignSessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignSessionDataRepository extends JpaRepository<SignSessionData, String> {
}
