package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApplicationProperties
{
	private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

	private static String version;
	private static String name;

	private static String serverUrl;

	private ApplicationProperties()
	{
		throw new UnsupportedOperationException("ApplicationProperties must not be instantiated!");
	}

	static
	{
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String applicationPropertiesPath = rootPath + "com\\apporelbotna\\gameserver\\persistencewsclient\\properties\\application.properties";

		Properties applicationProperties = new Properties();
		try
		{
			applicationProperties.load(new FileInputStream(applicationPropertiesPath));
			version = applicationProperties.getProperty("version");
			name = applicationProperties.getProperty("name");

			serverUrl = applicationProperties.getProperty("server.url");
		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
		}
	}

	public static String getVersion()
	{
		return version;
	}

	public static String getName()
	{
		return name;
	}

	public static String getServerUrl()
	{
		return serverUrl;
	}
}
