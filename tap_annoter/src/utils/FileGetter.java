package utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileGetter {

	private String fileName;
	
	public FileGetter(String name) {
		this.fileName = name;
	}
	
	public File GetFile() throws URISyntaxException {
		
		System.out.println("getting file");
		URL resource = getClass().getClassLoader().getResource(fileName);
		System.out.println("we got the file");
		return(new File(resource.toURI()));
			
		
	}
}
