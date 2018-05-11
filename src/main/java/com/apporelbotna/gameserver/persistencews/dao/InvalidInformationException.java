package com.apporelbotna.gameserver.persistencews.dao;

public class InvalidInformationException extends Exception
{

    private static final long serialVersionUID = 1L;

    public enum Reason
    {
	USER_IS_STORED("El usuario ya ha sido guardado"),
	USER_IS_NOT_STORED("El usuario no existe"),
	GAME_IS_STORED("El juego ya existe"),
	GAME_IS_NOT_STORED("El juego no existe");

	private String msg;

	private Reason(
		       String msg)
	{
	    this.msg = msg;
	}

	public String getMsg()
	{
	    return msg;
	}

    }

    public InvalidInformationException(
				       Reason reason)
    {
	super( reason.msg );
    }
}
