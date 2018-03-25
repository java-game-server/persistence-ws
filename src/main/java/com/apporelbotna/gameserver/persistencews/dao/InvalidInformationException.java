package com.apporelbotna.gameserver.persistencews.dao;

public class InvalidInformationException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public enum Reason
	{
		USER_IS_STORED("El usuario ya ha sido guardado");

		private String msg;

		private Reason(String msg)
		{
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

	}

	public InvalidInformationException(Reason reason)
	{
		super(reason.msg);
	}
}
