package tij.annotation.useCase;

public class PasswordUtils {
	@UseCase
	public boolean validatePassword(String password) {return true;}
	
	@UseCase(id = 2, description = "encrypt password")
	public String encryptPassword(String password) {return null;}
	
	@UseCase(id = 3, description = "check for new password")
	public boolean checkForNewPassword(String password) {return true;}
	
}
