package fileSystemSSP;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import  java.io.*;
public class Main {
	public static void main(String[] args) throws IOException {
		//read create execute delete
		Scanner in = new Scanner(System.in);
		System.out.print("Input a login: ");
        String userName = writeStr(in);
        System.out.println("hello " +userName);
       
		AccessManager manager = new AccessManager(userName);
		String myLocation = "root";
		File fileSystem = new File("E:\\Study2.0\\SSP\\fileSystem");
		String path = fileSystem.getAbsolutePath();

		while(true) {
			System.out.println();
			System.out.print(myLocation + ">");
			fileSystem = takeMethod(fileSystem,writeStr(in),manager);
			if(!Objects.equals(path,fileSystem.getAbsolutePath())){
				
				myLocation = "root";
				String[] words = (fileSystem.getAbsolutePath()).split(Pattern.quote("\\"));
				path = fileSystem.getAbsolutePath();
				boolean flag = false;
				
				for(int i = 0; i < words.length;i++) {
					if(Objects.equals(words[i],"fileSystem")) {
						flag = true;
						continue;
					}
					if(flag) {
						myLocation += "\\" +words[i];
					}
				}
			}
		}
	}
	
	public static File takeMethod(File fileSystem,String str,AccessManager manager) throws IOException {
		String[] words = str.split("\\s+");
		if(words.length == 1) {
			switch(words[0]) {
				case "ls": 
					return ls(fileSystem);
				default: 
					System.out.printf("System dont have command '%s' \n",words[0]);
			}
		}
		else if(words.length == 2) {
			switch(words[0]) {
				case "cd": 
					return cd(fileSystem,words[1]);
				case "mkdir":
					return mkdir(fileSystem,words[1]);
				case "create":
					return create(fileSystem,words[1],manager);
				case "rm":
					return rm(fileSystem,words[1],manager);
				case "exec":
					return exec(fileSystem,words[1],manager);
				case "read":
					return read(fileSystem,words[1], manager);
				default: 
					System.out.printf("System dont have command '%s' \n",words[0]);	
			}	
		}else if(words.length == 3) {
			switch(words[0]) {
			case "rename": 
				return rename(fileSystem,words[1],words[2]);
			default: 
				System.out.printf("System dont have command '%s' \n",words[0]);	
		}	
	}
		else {
			System.out.printf("System dont have command '%s' with %d argument \n",words[0], words.length-1);
		}
		return fileSystem;
	}
	
	private static File rename(File file, String string, String newName) {
		String newPath = file.getAbsolutePath() + "//" + string;
		String newPathRename = file.getAbsolutePath() + "//" + newName;
		File newFile = new File(newPath);
		File newFileRename = new File(newPathRename);
		newFile.renameTo(newFileRename);
		if(newFileRename.exists() && newFile.exists()) {
			System.out.print("File will be renamed from " + string + " to " + newName + "\n");
		} else {
			System.out.print("File cannot be renamed" + "\n");
		}
		return file;
	}

	public static String writeStr(Scanner in) {
			String str = in.nextLine();
			return str;
		
	}
	
	
	public static File ls(File folder) {
		for (File file : folder.listFiles())
		{
		 System.out.println(file.getName());
		}
		return folder;
	}
	
	public static File cd(File folder , String str) {
		
		String rootPath = "E:\\Study2.0\\SSP\\fileSystem";
		if(Objects.equals(str, "..")) {
			if(!Objects.equals(rootPath, folder.getAbsolutePath())) {
				String newPath = "";
				String[] words = (folder.getAbsolutePath()).split(Pattern.quote("\\"));
				
				for(int i = 0; i < words.length-1; i++) {
					if(i != words.length-2) {
						newPath += words[i]+ "\\";
					} else {
						newPath += words[i];
					}
					
				}
				folder = new File(newPath);
			} else {
				System.out.println("this is root");
			}
		}
		else {
			String newPath = folder.getAbsolutePath() + "//" + str;
			File newFolder = new File(newPath);
			if(newFolder.isDirectory()) {
				folder = newFolder;
			}
			else{
				System.out.println(str + " This isn't folder");
			}
		}
		return folder;
	}
	
	public static File mkdir(File folder , String str) {
		String newPath = folder.getAbsolutePath() + "//" + str;
		File newFolder = new File(newPath);
		boolean created = newFolder.mkdir();
		if(created) {
			System.out.printf("Folder %s has been created \n", str);
		} else {
			System.out.printf("Folder with name %s  can't be created \n", str);
		}
		return folder;
	}
	
	public static File create(File file, String str, AccessManager manager) {
		if(manager.cheackAccess(1)) {
			String newPath = file.getAbsolutePath() + "//" + str;
			File newFile = new File(newPath);
	        try
	        {
	            boolean created = newFile.createNewFile();
	            if(created)
	                System.out.println("File has been created");
	        }
	        catch(IOException ex){
	             
	            System.out.println(ex.getMessage());
	        }  
        }
        return file;
	}
	
	public static File rm(File file, String str, AccessManager manager) {
		if(manager.cheackAccess(3)) {
			String newPath = file.getAbsolutePath() + "//" + str;
			
			File newFile = new File(newPath);
			if(newFile.isFile()) {
				 boolean deleted = newFile.delete();
			        if(deleted)
			            System.out.println("file " + str + " has been deleted");
			} else if(newFile.isDirectory()) {
				recursiveDelete(newFile);
				System.out.println("folder " + str + " has been deleted");
			} else {
				System.out.println(str + " cannot be deleted");
			}
		}
		return file;
	}
	
	public static void recursiveDelete(File file) {
		if(file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
		file.delete();
	}
	public static File exec(File file, String str,AccessManager manager) throws IOException {
		if(manager.cheackAccess(2)) {
			String path = file.getAbsolutePath() + "//" + str;
			if((new File(path)).isFile()) {
				Runtime.getRuntime().exec("notepad " + path);
			} else {
				System.out.println(str + " cannot be execute");
			}
		}
		return file;
	}
	public static File read(File file, String str, AccessManager manager) {
		if(manager.cheackAccess(0)) {
			try {
				String path = file.getAbsolutePath() + "//" + str;
				if((new File(path)).isFile()) {
					BufferedReader fin = new BufferedReader(new FileReader(new File(path)));
					String line;
					while ((line = fin.readLine()) != null) System.out.println(line);
					fin.close();
				}
				else {
					System.out.println(str + " this is not file");
					return file;
				}
			} catch (FileNotFoundException e) {
				System.out.println(str + " this is not file");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
}


