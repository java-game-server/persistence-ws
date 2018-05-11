package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;

public interface Connectivity
{
    public Connection connect();

    public void close();

}
