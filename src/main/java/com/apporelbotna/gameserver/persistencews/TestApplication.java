package com.apporelbotna.gameserver.persistencews;

import java.sql.SQLException;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.persistencews.service.PostgreService;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;

@SpringBootApplication
public class Application
{

	public static void main(String[] args)
	{
		// SpringApplication.run(Application.class, args);
		PostgreDAO postgreDao = PostgreDAO.getInstance();
		PostgreService postgreService = PostgreService.getInstance();
		postgreDao.connect();

		// Testing
		try
		{
			User jan = postgreDao.getUserBasicInformation("jan@jan.com");
			System.out.println(jan);
			User testStore = new User("testStore@teststore.com", "testStore");
			postgreDao.storeNewUserInBBDD(testStore, "1234");

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("------------------------------------------");
		// Logearse
		// recibe una contraseña y un email
		// mira si existe en la bbdd
		// si existe generar token asignar al usuario y debolver true
		// WS tendra que contestar este token


		String email = "testStore@teststore.com";
		String password = "1234";

		try
		{
			if (postgreDao.getUserBasicInformation(email) == null) // usuario no existe
			{
				System.out.println("el usuario no existe");
			} else
			{

				// el usuario existe
				// Mirar contraseña
				String requiredPassword = postgreDao.getUserPassword(email);
				if (!requiredPassword.equals(password))
				{
					System.out.println("Las contraseñas no son iguales");
				}

				// generar token insertar y devolver
				User userTestToken = postgreService.getAllInformationUser(email);
				Token token = new Token();
				try
				{
					postgreDao.storeTokenToUser(userTestToken, token);
					System.out.println(token);
				} catch (InvalidInformationException e)
				{
					e.printStackTrace();
				}

			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		}


		System.out.println("------------------------------------------");
		//TBA
		//para la primera version del programa le daremos los juegos asignados ya al usuario, en este caso: El pong
		//en proximas versiones se podra crear juegos desde el panel de administrador.

		//Mirar juegos del usuario

		try
		{
			List<Game> games = postgreDao.getGamesUser("jan1@jan.com");
			for (Game game : games)
			{
				System.out.println(game);
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Check user with all information
		try
		{
			User userAllInformationTest = postgreService.getAllInformationUser("jan@jan.com");
			System.out.println(userAllInformationTest);

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------------------------------------------");

		//horas jugadas a un juego
		try
		{
			System.out.println(postgreDao.getHourPlayedInGame("jan@jan.com", 1));
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
