package fileSystemSSP;

import java.util.Objects;

public class AccessManager {
	//read create execute delete admin = [0] user [1] =  
	private final int[][] accessMatrix = {{1,1,1,1},
							{1,0,0,0}};
	private String str;
	
	public AccessManager(String userName) {
		str = userName; 
	}
	public boolean cheackAccess(int i) {
		if(Objects.equals(str,"admin")) {
			if(accessMatrix[0][i] != 0) {
				return true;
			} else {
				System.out.print("access denied" +"\n");
				return false;
			}
		} 
		if(Objects.equals(str,"user")) {
			if(accessMatrix[1][i] != 0) {
				return true;
			} else {
				System.out.print("access denied" +"\n");
				return false;
			}
		}
		System.out.print("access denied tut " + "\n");
		return false;
	}
}
