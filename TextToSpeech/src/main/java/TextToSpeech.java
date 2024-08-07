import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextToSpeech {
    private static final String VOICE_NAME = "kevin"; // Default voice

    public static void main(String[] args) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(VOICE_NAME);

        if (voice != null) {
            voice.allocate();
            try (InputStream inputStream = TextToSpeech.class.getResourceAsStream("/script.txt");
                 BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("PAUSE")) {
                        String[] parts = line.split(" ");
                        int pauseDuration = Integer.parseInt(parts[1]);
                        Thread.sleep(pauseDuration);
                    } else {
                        voice.speak(line);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            voice.deallocate();
        } else {
            System.out.println("Voice not found!");
        }
    }
}
