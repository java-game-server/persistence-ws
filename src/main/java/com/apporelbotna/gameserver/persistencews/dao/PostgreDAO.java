package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException.Reason;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;
import com.apporelbotna.gameserver.stubs.RegisterUser;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;
import com.apporelbotna.gameserver.stubs.UserType;
import com.apporelbotna.gameserver.stubs.UserWrapper;

/**
 * @author albert Spring repository, basic persistence module tho database
 *
 */
@Repository
public class PostgreDAO extends ConnectivityPostgreDAO implements DAO
{
    public PostgreDAO()
    {
	// empty constructor
    }

    // TODO check bechavior after refactor
    /**
     * @param email
     *            the primaryKey of the user.
     * @return user if exist or null.
     * @throws SQLException
     */
    @Override
    public User getUserBasicInformation(String email) throws SQLException
    {
	String select = "SELECT email, name, rol, gold FROM public.\"user\" where email = ? ";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, email );
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		if ( executeQuery.next() )
		{

		    String sqlEmail = executeQuery.getString( "email" );
		    String name = executeQuery.getString( "name" );
		    float gold = executeQuery.getFloat( "gold" );
		    UserType userType = UserType.convertStringToUserType( executeQuery.getString( "rol" ) );

		    return new User(sqlEmail, name, gold, userType);
		}
	    }
	}
	return null;
    }

    /**
     * metodo que te saca todos los juegos de un usuario.
     *
     * @param email
     *            the primary key of the User
     * @return
     * @throws SQLException
     */
    @Override
    public List< Game > getAllGamesByUser(String email) throws SQLException
    {
	String select = "SELECT uhbg.email_user, uhbg.id_game, g.name, g.description, g.executable_name, g.img_uri"
			+ " FROM public.user_have_bought_game uhbg" + " INNER Join game g on g.id = uhbg.id_game"
			+ " where email_user = ?";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, email );
	    List< Game > userGames = new ArrayList<>();
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    userGames.add( new Game( executeQuery.getInt( "id_game" ),
					     executeQuery.getString( "name" ),
					     executeQuery.getString( "description" ),
					     executeQuery.getString( "executable_name" ),
					     executeQuery.getString( "img_uri" ) ) );
		}
	    }
	    return userGames;
	}
    }

    /**
     * en el stub User la password
     *
     * @param email
     * @return the password of the user if exists or null
     * @throws SQLException
     */
    @Override
    public String getUserPassword(String email) throws SQLException
    {
	String select = "SELECT password FROM public.user where email = ?";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, email );
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		if ( executeQuery.next() )
		{
		    return executeQuery.getString( "password" );
		}
	    }
	}
	return null;
    }

    @Override
    public float getTimePlayedInGame(String email, int gameId) throws SQLException,
							       InvalidInformationException
    {
	if ( getUserBasicInformation( email ) == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}
	if ( getGameById( gameId ) == null )
	{
	    throw new InvalidInformationException( Reason.GAME_IS_NOT_STORED );
	}

	String select = "select email_user, id_game, game_lenght " + "from public.user_historical_game "
			+ "where email_user = ? and id_game = ?";
	float totalTime = 0.0f;
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, email );
	    prepareStatement.setInt( 2, gameId );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    totalTime += executeQuery.getFloat( "game_lenght" );
		}
	    }
	}
	return totalTime;
    }

    @Override
    public Game getGameById(int idGame) throws SQLException
    {
	String select = "select * from public.game where id = ?";
	Game game = null;

	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setInt( 1, idGame );
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		if ( executeQuery.next() )
		{
		    game = new Game( executeQuery.getInt( "id" ),
				     executeQuery.getString( "name" ),
				     executeQuery.getString( "description" ),
				     executeQuery.getString( "executable_name" ),
				     executeQuery.getString( "img_uri" ) );
		}
	    }
	}
	return game;
    }

    @Override
    public boolean isTokenValid(UserWrapper userWrapper) throws SQLException
    {
	String select = "SELECT token, user_email FROM public.token where token = ? and user_email = ?";

	boolean existe = false;
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, userWrapper.getToken().getTokenName() );
	    prepareStatement.setString( 2, userWrapper.getUser().getEmail() );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		if ( executeQuery.next() )
		{
		    existe = true;
		}
	    }
	}
	return existe;
    }

    @Override
    public List< RankingPointsTO > getRankingUsersGameByPoints(int idGame) throws SQLException,
									   InvalidInformationException
    {
	Game game = getGameById( idGame );
	if ( game == null )
	{
	    throw new InvalidInformationException( Reason.GAME_IS_NOT_STORED );
	}

	String select = "select email_user, sum(puntuation) as puntuation" + " from public.user_historical_game"
			+ " where id_game = ? group by email_user";

	List< RankingPointsTO > ranking = new ArrayList<>();
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setInt( 1, idGame );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    ranking.add( new RankingPointsTO( executeQuery.getString( "email_user" ),
						      executeQuery.getRow(),
						      executeQuery.getInt( "puntuation" ) ) );
		}
	    }
	}
	return ranking;
    }

    @Override
    public void storeNewUserInBBDD(RegisterUser userToRegister) throws InvalidInformationException,
								SQLException
    {
	User user = userToRegister.getUser();
	if ( getUserBasicInformation( user.getId() ) != null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_STORED );
	}

	String query = "INSERT INTO public.\"user\"(email, name, password, rol, gold) VALUES (?, ?, ?, ?, ?);";
	try ( PreparedStatement preparedStatement = conn.prepareStatement( query ); )
	{
	    preparedStatement.setString( 1, user.getEmail() );
	    preparedStatement.setString( 2, user.getName() );
	    preparedStatement.setString( 3, userToRegister.getPassword() );
	    preparedStatement.setString( 4, user.getRol().getType() );
	    preparedStatement.setFloat( 5, user.getGold() );
	    preparedStatement.executeUpdate();
	}
    }

    @Override
    public void storeTokenToUser(User user, Token token) throws InvalidInformationException,
							 SQLException
    {
	if ( getUserBasicInformation( user.getId() ) == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}
	String query = "INSERT INTO public.token (token, user_email) VALUES (?, ?);";
	try ( PreparedStatement preparedStatement = conn.prepareStatement( query ); )
	{
	    preparedStatement.setString( 1, token.getTokenName() );
	    preparedStatement.setString( 2, user.getId() );
	    preparedStatement.executeUpdate();
	}
    }

    /**
     * @param match
     * @throws SQLException
     * @throws InvalidInformationException
     */
    @Override
    public void storeNewMatch(Match match) throws InvalidInformationException,
					   SQLException
    {
	User user = getUserBasicInformation( match.getEmailUser() );
	if ( user == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}
	Game game = getGameById( match.getIdGame() );
	if ( game == null )
	{
	    throw new InvalidInformationException( Reason.GAME_IS_NOT_STORED );
	}
	String query = "INSERT INTO public.user_historical_game" + " (email_user, id_game, game_lenght, puntuation)"
		       + " VALUES (?, ?, ?, ?);";
	try ( PreparedStatement preparedStatement = conn.prepareStatement( query ) )
	{
	    preparedStatement.setString( 1, match.getEmailUser() );
	    preparedStatement.setInt( 2, match.getIdGame() );
	    preparedStatement.setLong( 3, ( long ) match.getGameTimeInSeconds() );
	    preparedStatement.setInt( 4, match.getScore() );
	    preparedStatement.executeUpdate();
	}
    }

    // TODO Store Game

    // TODO Cambiar password user

    // TODO Add game to user

    @Override
    public void updateUser(UserWrapper userWrapper) throws SQLException,
						    InvalidInformationException
    {
	User userBasicInformation = getUserBasicInformation( userWrapper.getUser().getEmail() );
	if ( userBasicInformation == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}

	if ( !isTokenValid( userWrapper ) )
	{
	    throw new InvalidInformationException( Reason.TOKEN_INVALID );
	}

	// TODO add update games

	String query = "UPDATE public.\"user\" SET name=?, gold=?, rol=? WHERE email=?";

	try ( PreparedStatement prepareStatement = conn.prepareStatement( query ); )
	{
	    prepareStatement.setString( 1, userWrapper.getUser().getName() );
	    prepareStatement.setFloat( 2, userWrapper.getUser().getGold() );
	    prepareStatement.setString( 3, userWrapper.getUser().getRol().getType() );
	    prepareStatement.setString( 4, userWrapper.getUser().getEmail() );
	}

    }
}
