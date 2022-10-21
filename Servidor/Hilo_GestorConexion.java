package Servidor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Hilo_GestorConexion implements Runnable {  //tercera clase
	private final String[] caracter = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	private final String[] caracterMorse = {"·-", "-···", "-·-·", "-··", "·", "··-·", "--·", "····", "··", "·---", "-·-", "·-··", "--", "-·", "---","·--·", "--·-",
			 "·-·", "···", "-", "··-", "···-", "·--", "-··-", "-·--", "--··",
			 "-----", "·----", "··---", "···--", "····-", "·····", "-····", "--···", "---··", "----·"};
	
	String textoLegible;
	ArrayList<String> respuestaMorse;
	Socket ssocket;
	
	public Hilo_GestorConexion(String textoLegible, Socket ssocket) {
		this.textoLegible = textoLegible;
		this.ssocket = ssocket;
	}

	@Override
	public void run() {
		
		//Recibe el texto del cliente para ser tratado
		textoLegible = quitarTildes(textoLegible); // Retiramos las tildes
		textoLegible = textoLegible.replaceAll("[^\\dA-Za-z ]", ""); // retiramos todos los signos EXCEPTO ESPACIOS (todo lo que no sean caracteres alfanumericos)
		textoLegible = textoLegible.toLowerCase(); // ponemos en minusculas el texto


		//Se crean los Hilos Traductores
		respuestaMorse = traducir(textoLegible);
		System.out.println(textoLegible);
		
		//Devuelve la respuesta al cliente (objeto TextoMorse)
		OutputStream outputStream;
		try {
			outputStream = ssocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);
			oos.writeObject(respuestaMorse); //devuelve por el socket la respuestaMorse hasta el cliente
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String quitarTildes(String textoLegible) {
		String tildesYPuntos         = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű";
		String textoSinTildesYPuntos = "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";
		StringBuilder sb = new StringBuilder();
		
	    for (int i = 0; i < textoLegible.length(); ++i) {
	        char c = textoLegible.charAt(i);
	        int pos = tildesYPuntos.indexOf(c);
	        
	        //Si el caracter actual se encuentra dentro de la cadena, cambialo por su igual, de lo contrario, manten el caracter actual.
	        if (pos > -1)
	            sb.append(textoSinTildesYPuntos.charAt(pos));
	        else {
	            sb.append(c);
	        }
	    }
	    return  textoLegible = sb.toString();
	}
	
	private ArrayList<String> traducir(String textoLegible) {
		char[] arrayLetras = textoLegible.toCharArray();
		respuestaMorse = new ArrayList<String>();
		for (int i = 0; i < arrayLetras.length; ++i) {
			if (arrayLetras[i] == ' ') { //Añade el espacio en blanco
				respuestaMorse.add(" ");
			}else {
				for (int j = 0; j < caracter.length; ++j) {
					if (arrayLetras[i] == caracter[j].charAt(0)) { //charAt(0) pillara la primera letra de la cadena de String caracter
						respuestaMorse.add(caracterMorse[j]);
					}
				}	
			}
		}
		return respuestaMorse;
	}
}