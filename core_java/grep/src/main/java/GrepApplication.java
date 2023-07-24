import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrepApplication {
	public static void main(String[] args) throws IllegalAccessException {
		Logger logger = LoggerFactory.getLogger(GrepApplication.class);

		if(args.length != 3){
			throw new IllegalAccessException("USAGE: grep regex rootPath outfile");
		}

		Grep grep = new GrepStreamImpl(args[0], args[1], args[2]);

		try {
			grep.process();
		}
		catch (Exception exception){
			exception.printStackTrace();
			logger.error("Error: Unable to process", exception);
		}
	}
}
