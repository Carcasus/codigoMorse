package Cliente;

import javax.sound.sampled.*;

public class SoundPlayer {
	
	private static float SAMPLE_RATE = 8000f;
	private static int SAMPLE_SIZE = 8;
	private static double AMPLITUDE = 2.0 * Math.PI;
	
	
	private static AudioFormat audioFormat= new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE,1, true, false);
	
	public static void play(int hz, int msecs) throws LineUnavailableException {
		int bufferSize = msecs * SAMPLE_SIZE;
		byte[] buf = new byte[bufferSize];
		double sample = AMPLITUDE * hz / SAMPLE_RATE;
		double angle;
		
		SourceDataLine sdl = AudioSystem.getSourceDataLine(audioFormat);
		sdl.open(audioFormat);
		sdl.start();
		
		for (int i = 0; i < bufferSize; i++) {
			buf[i] = (byte) (Math.sin(i * sample) * 127.0);
		}
		
		sdl.write(buf, 0, bufferSize);
		sdl.drain();
		sdl.stop();
		sdl.close();
	}
}