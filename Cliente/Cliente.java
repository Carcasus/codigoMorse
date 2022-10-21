package Cliente;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.sound.sampled.LineUnavailableException;

public class Cliente { //Quinta y ultima clase a tratar
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		//Hace la conexion TCP hacia el puerto 25000
        try {
			Socket socket = new Socket("localhost",25000);
			//Pregunta el texto al usuario
			System.out.println("Introduce un texto a pasar a codigo morse");
			String textoLegible = sc.nextLine();
			
			System.out.println("Introduce la duracion en milisegundos del punto");
			int duracion = sc.nextInt();

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
			bw.write(textoLegible);
			bw.newLine();
			bw.flush(); //Enviar el mensaje
			
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			mostrarRespuesta(ois, duracion);
			
	
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private static void mostrarRespuesta(ObjectInputStream ois, int duracion) {
		Object mensajeMorse;
		try {
			mensajeMorse = ois.readObject();
			ArrayList<String> letraMorse = (ArrayList<String>) mensajeMorse;
			Iterator it = letraMorse.iterator();                          
			
			for (int i = 0; i < letraMorse.size(); ++i) {
				for (int j = 0; j < letraMorse.get(i).length(); ++j) {
					if (letraMorse.get(i).charAt(j) == '·') { //actuacion del punto
						System.out.print("· ");
						SoundPlayer.play(200, duracion);
						if (j+1 != letraMorse.get(i).length()) {//Actuacion del espacio entre simbolos morse (cuando se llegue al ultimo hueco de J, dejara de pitar)
							SoundPlayer.play(0, duracion);
						}
					} else if (letraMorse.get(i).charAt(j) == '-') { //Actuacion de la barra
						System.out.print("- ");
						SoundPlayer.play(300, duracion*3);
						if (j+1 != letraMorse.get(i).length()) {//Actuacion del espacio entre simbolos morse (cuando se llegue al ultimo hueco de J, dejara de pitar)
							SoundPlayer.play(0, duracion);
						}
					}
				}
				if (letraMorse.get(i).contains(" ")) { //Actuacion del espacio entre palabras
					SoundPlayer.play(0, duracion*7);
				}
				if (it.hasNext() && !(letraMorse.get(i).contains(" "))){ //espacio entre caracteres y evitando espacios(usando un iterador, podemos saltar entre campo y campo del array)
					SoundPlayer.play(0, duracion*3);
				}
			}
			
	    }catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String obtenerTexto(String textoLegible) {
		return textoLegible;
	}
}