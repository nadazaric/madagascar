package ftn.rbs.madagascar_hub.security.jwt;

public interface IJWTTokenService {

	public JWTToken findByToken(String token);

	public void invalidateToken(String token);

	public boolean isValid(String token);

	public void createToken(String token);
	
	public void createNoMFAToken(String token);

	public void verifyToken(String token);
	
}
