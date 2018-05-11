package com.apporelbotna.gameserver.persistencews.properties;

import java.util.ResourceBundle;

public final class ApplicationProperties
{
    private static String version;
    private static String name;

    private static String serverUrl;

    private static String databaseUrl;
    private static String databaseUser;
    private static String databasePassword;

    private ApplicationProperties()
    {
	throw new UnsupportedOperationException( "ApplicationProperties must not be instantiated!" );
    }

    static
    {
	ResourceBundle bundle =
			      ResourceBundle.getBundle( "com.apporelbotna.gameserver.persistencews.properties.application" );

	version = bundle.getString( "version" );
	name = bundle.getString( "name" );

	serverUrl = bundle.getString( "server.url" );

	databaseUrl = bundle.getString( "db.url" );
	databaseUser = bundle.getString( "db.user" );
	databasePassword = bundle.getString( "db.password" );
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

    public static String getDatabaseUrl()
    {
	return databaseUrl;
    }

    public static String getDatabaseUser()
    {
	return databaseUser;
    }

    public static String getDatabasePassword()
    {
	return databasePassword;
    }
}
