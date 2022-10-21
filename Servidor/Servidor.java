package Servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor { //cuarta clase
	
	public static void main(String[] args) throws IOException {
		ServerSocket ssocket = new ServerSocket(25000);
		while (true) {//Vuelve a esperar conexion
			
			try {
				//Espera conexion por parte de un cliente en el puerto 25000
				
				System.out.println("Escuchando por el puerto 25000...");
				Socket socket = ssocket.accept();
				System.out.println("Mensaje entrante...");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				
				String mensaje = "";
				
				if ((mensaje = in.readLine()) != null) { //Este if se activara solo al recibir un mensaje desde cliente
					//Crea un hilo_GestorConexion para tratarla
					Hilo_GestorConexion hilo_gestorConexion = new Hilo_GestorConexion(mensaje, socket);
					Thread thread = new Thread(hilo_gestorConexion);
					thread.start();
				}			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}