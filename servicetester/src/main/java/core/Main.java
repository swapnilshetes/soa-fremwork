package core;

import java.io.IOException;

import config.Constants;
import config.Constants.*;
import utilities.Logger;

public class Main extends base {

	public static void main(String[] args) throws IOException, InterruptedException {
	
		Logger.logInfo(Main.class, "MAIN START POINT");
		new Main();

		OptType operationType = prop.getProperty("PROCESS_READ_WRITE").equals("read") ? OptType.read : OptType.write;
		OptFileType operationFileType = prop.getProperty("PARAMETER_CONFIGURATION").equals("xlsx")? OptFileType.xlsx : OptFileType.json;
		new setProcess(operationType, operationFileType);
	}

}
