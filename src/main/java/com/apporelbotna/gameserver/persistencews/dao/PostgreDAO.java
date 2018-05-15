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
    public enum UserFilter
    {
	NAME("name"),
	EMAIL("email");

	private String columnName;

	private UserFilter(String columnName)
	{
	    this.columnName = columnName;
	}

	public String getColumnName()
	{
	    return columnName;
	}

	public static UserFilter fromString(String userFilter)
	{
	    for ( UserFilter actualUserFilter : UserFilter.values() )
	    {
		if ( actualUserFilter.getColumnName().equals( userFilter ) )
		{
		    return actualUserFilter;
		}
	    }
	    return null;
	}
    }

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
		    UserType userType = UserType.fromString( executeQuery.getString( "rol" ) );

		    return new User( sqlEmail, name, gold, userType );
		}
	    }
	}
	return null;
    }

    public List< User > findUsersLikeName(String userInput) throws SQLException
    {
	String select = "select * from public.user where name like ?";

	List< User > users = new ArrayList<>();
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, "%" + userInput + "%" );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    String sqlEmail = executeQuery.getString( "email" );
		    String name = executeQuery.getString( "name" );
		    float gold = executeQuery.getFloat( "gold" );
		    UserType userType = UserType.fromString( executeQuery.getString( "rol" ) );
		    List< Game > games = getAllGamesByUser( sqlEmail );
		    users.add( new User( sqlEmail, name, games, gold, userType ) );
		}
	    }
	}
	return users;
    }

    public List< User > findUsersLikeEmail(String userInput) throws SQLException
    {
	String select = "select * from public.user where email like ?";

	List< User > users = new ArrayList<>();
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, "%" + userInput + "%" );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    String sqlEmail = executeQuery.getString( "email" );
		    String name = executeQuery.getString( "name" );
		    float gold = executeQuery.getFloat( "gold" );
		    UserType userType = UserType.fromString( executeQuery.getString( "rol" ) );
		    List< Game > games = getAllGamesByUser( sqlEmail );
		    users.add( new User( sqlEmail, name, games, gold, userType ) );
		}
	    }
	}
	return users;
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
	String select =
		      "SELECT uhbg.email_user, uhbg.id_game, g.name, g.description, g.executable_name, g.img_uri, g.price"
			+ " FROM public.user_have_bought_game uhbg INNER Join game g on g.id = uhbg.id_game"
			+ " where email_user = ?";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, email );
	    List< Game > userGames = new ArrayList<>();
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    userGames.add(
			    new Game(
				    executeQuery.getInt( "id_game" ),
				    executeQuery.getString( "name" ),
				    executeQuery.getString( "description" ),
				    executeQuery.getString( "executable_name" ),
				    executeQuery.getString( "img_uri" ),
				    executeQuery.getFloat( "price" ) ) );
		}
	    }
	    return userGames;
	}
    }

    public List< Game > getAllGames() throws SQLException
    {
	String select = "SELECT id, name, description, executable_name, img_uri, price FROM public.game;";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    List< Game > userGames = new ArrayList<>();
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    userGames.add(
			    new Game(
				    executeQuery.getInt( "id" ),
				    executeQuery.getString( "name" ),
				    executeQuery.getString( "description" ),
				    executeQuery.getString( "executable_name" ),
				    executeQuery.getString( "img_uri" ),
				    executeQuery.getFloat( "price" ) ) );
		}
	    }
	    return userGames;
	}
    }

    public List< Game > findGamesLikeName(String userInput) throws SQLException
    {
	String select = "select * from public.game where name like ?";

	List< Game > games = new ArrayList<>();
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    prepareStatement.setString( 1, "%" + userInput + "%" );

	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    Integer id = executeQuery.getInt( "id" );
		    String name = executeQuery.getString( "name" );
		    String descripcion = executeQuery.getString( "description" );
		    String executable = executeQuery.getString( "executable_name" );
		    String image = executeQuery.getString( "img_uri" );
		    float price = executeQuery.getFloat( "price" );
		    games.add( new Game( id, name, descripcion, executable, image, price ) );
		}
	    }
	}
	return games;
    }

    public List< String > getAllGenre() throws SQLException
    {
	String select = "SELECT name FROM public.genre;";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( select ); )
	{
	    List< String > genres = new ArrayList<>();
	    try ( ResultSet executeQuery = prepareStatement.executeQuery(); )
	    {
		while ( executeQuery.next() )
		{
		    genres.add( executeQuery.getString( "name" ) );
		}
	    }
	    return genres;
	}
    }

    /**
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

	String select = "select email_user, id_game, game_lenght from public.user_historical_game "
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
		    game = new Game(
			    executeQuery.getInt( "id" ),
			    executeQuery.getString( "name" ),
			    executeQuery.getString( "description" ),
			    executeQuery.getString( "executable_name" ),
			    executeQuery.getString( "img_uri" ),
			    executeQuery.getFloat( "price" ) );
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
		    ranking.add(
			    new RankingPointsTO(
				    executeQuery.getString( "email_user" ),
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

    @Override
    public int storeNewGame(Game game) throws SQLException,
				       InvalidInformationException
    {
	String sql = "INSERT INTO public.game ( name, description, executable_name, img_uri, price ) VALUES"
		     + " ( ?, ?, ?, ?, ? )";

	try ( PreparedStatement preparedStatement = conn.prepareStatement( sql ) )
	{
	    preparedStatement.setString( 1, game.getName() );
	    preparedStatement.setString( 2, game.getDescription() );
	    preparedStatement.setString( 3, game.getExecutableName() );
	    preparedStatement.setString( 4, game.getImgUri() );
	    preparedStatement.setFloat( 5, game.getPrice() );
	    preparedStatement.executeUpdate();

	    try ( ResultSet rs = preparedStatement.getGeneratedKeys() )
	    {
		if ( rs.next() )
		{
		    return rs.getInt( 1 );
		}
	    }
	    return 0;
	}
    }

    public void storeGameToUser(User user, int idGame) throws SQLException,
						       InvalidInformationException
    {
	// Check if user exist
	if ( getUserBasicInformation( user.getEmail() ) == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}

	// Check if game exist
	if ( getGameById( idGame ) == null )
	{
	    throw new InvalidInformationException( Reason.GAME_IS_NOT_STORED );
	}

	String sql = "INSERT INTO public.user_have_bought_game( email_user, id_game ) VALUES ( ?, ?);";

	// quitar dineros
	float userMoney = user.getGold();
	Game game = getGameById( idGame );
	float gameCost = game.getPrice();

	if ( userMoney < gameCost )
	{
	    user.setGold( userMoney - gameCost);
	    updateUser( user );
	}

	try ( PreparedStatement preparedStatement = conn.prepareStatement( sql ) )
	{
	    preparedStatement.setString( 1, user.getEmail() );
	    preparedStatement.setInt( 2, idGame );
	    preparedStatement.executeUpdate();
	}
    }

    @Override
    public void updateUser(User user) throws SQLException,
				      InvalidInformationException
    {
	User userBasicInformation = getUserBasicInformation( user.getEmail() );
	if ( userBasicInformation == null )
	{
	    throw new InvalidInformationException( Reason.USER_IS_NOT_STORED );
	}

	// update user
	String query = "UPDATE public.\"user\" SET name=?, gold=?, rol=? WHERE email=?";
	try ( PreparedStatement prepareStatement = conn.prepareStatement( query ); )
	{
	    prepareStatement.setString( 1, user.getName() );
	    prepareStatement.setFloat( 2, user.getGold() );
	    prepareStatement.setString( 3, user.getRol().getType() );
	    prepareStatement.setString( 4, user.getEmail() );
	    prepareStatement.executeUpdate();
	}
    }

    public boolean isStoredGenre(String genre) throws SQLException
    {
	String sql = "SELECT name FROM public.genre where name=?";
	boolean existe = false;
	try ( PreparedStatement prepareStatement = conn.prepareStatement( sql ); )
	{
	    prepareStatement.setString( 1, genre );

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

    public void storeGenreToGame(Game game, String genre) throws SQLException,
							  InvalidInformationException
    {
	// Check if genre exist
	if ( !isStoredGenre( genre ) )
	{
	    throw new InvalidInformationException( Reason.GENRE_IS_NOT_STORED );
	}

	// Check if game exist
	if ( getGameById( game.getId() ) == null )
	{
	    throw new InvalidInformationException( Reason.GAME_IS_NOT_STORED );
	}

	String sql = "INSERT INTO public.game_belong_genre(id_game, genre_name) VALUES ( ?, ?);";

	try ( PreparedStatement preparedStatement = conn.prepareStatement( sql ) )
	{
	    preparedStatement.setInt( 1, game.getId() );
	    preparedStatement.setString( 2, genre );
	    preparedStatement.executeUpdate();
	}

    }
}
