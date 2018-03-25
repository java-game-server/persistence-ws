package com.apporelbotna.gameserver.persistencews;

import java.sql.SQLException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;

@SpringBootApplication
public class Application
{

	public static void main(String[] args)
	{
		// SpringApplication.run(Application.class, args);
		PostgreDAO dao = PostgreDAO.getInstance();
		dao.connect();

		// Testing
		try
		{
			User jan = dao.getUserInformation("jan@jan.com");
			System.out.println(jan);
			User testStore = new User("testStore@teststore.com", "testStore");
			dao.StoreNewUserInBBDD(testStore, "1234");

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
			if (dao.getUserInformation(email) == null) // usuario no existe
			{
				System.out.println("el usuario no existe");
			} else
			{

				// el usuario existe
				// Mirar contraseña
				String requiredPassword = dao.getUserPassword(email);
				if (!requiredPassword.equals(password))
				{
					System.out.println("Las contraseñas no son iguales");
				}

				// generar token y devolver
				Token token = new Token();
				System.out.println(token);
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		System.out.println("------------------------------------------");
		//TBA
		//para la primera version del programa le daremos los juegos asignados ya al usuario, en este caso: El pong
		//en proximas versiones se podra crear juegos desde el panel de administrador.

		//Mirar juegos del usuario


	}
}
