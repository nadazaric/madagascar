package ftn.rbs.madagascar_hub.security.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Long> {
	
	public Optional<JWTToken> findByToken(String token);
}
