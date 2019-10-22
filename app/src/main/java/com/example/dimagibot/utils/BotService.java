package com.example.dimagibot.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import com.example.dimagibot.R;
import com.thedeanda.lorem.LoremIpsum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * $ |-| ! V @ M
 * Created by Shivam Pokhriyal on 2019-10-22.
 */
public class BotService {

    private Map<String, String> dictionary;
    private Map<String, List<String>> employeeLocation;
    private static BotService botService;
    private List<String> history;

    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;

    private static final String OFFICE = "Office";
    private static final String HOME = "Home";

    // Commands
    private static final String HISTORY = "history";
    private static final String HELP = "help";
    private static final String HERE_I_AM = "hereiam";
    private static final String IN_OFFICE = "inoffice";
    private static final String PING = "ping";
    private static final String PLAY = "play";
    private static final String SAY = "say";
    private static final String SPRECHE = "spreche";
    private static final String STOP = "stop";
    private static final String VOL = "vol";
    private static final String WHERE_IS = "whereis";
    private static final String CALCULATE = "calculate";
    private static final String IPSUM = "ipsum";
    private static final String LINK = "link";

    private BotService(Context context) {
        dictionary = new HashMap<>();
        employeeLocation = new HashMap<>();
        history = new ArrayList<>();
        initTTS(context);
        prepareDictionary();
        addDummyEmployee();
    }

    private void initTTS(final Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    initTTS(context);
                }
            }
        });
    }

    public static BotService getInstance(Context context) {
        if (botService == null) {
            botService = new BotService(context);
        }
        return botService;
    }

    public String botAction(final String message, Context context) {
        if (message.toLowerCase().contains(HISTORY)) {
            return history.toString();
        }
        if (message.toLowerCase().contains(HELP)) {
            String[] command = message.split(" ");
            if (command.length == 2) {
                history.add(message);
                return dictionary.get(command[1].toLowerCase());
            } else {
                return "Invalid command! \nOnly 1 argument requied. \nSample: help play";
            }
        }
        if (message.toLowerCase().contains(HERE_I_AM)) {
            String[] command = message.split(" ");
            if (command.length != 2) {
                return "Invalid command! \nOnly 1 argument required. \nSample: hereiam Bangalore";
            }
            history.add(message);
            updateEmployeeLoc("current", command[1]);
            return "Updated your location successfully";
        }
        if (message.toLowerCase().contains(IN_OFFICE)) {
            history.add(message);
            return employeeLocation.get(OFFICE).toString();
        }
        if (message.toLowerCase().contains(PING)) {
            history.add(message);
            return "🥰 Bot is active 🥰 ";
        }
        if (message.toLowerCase().contains(PLAY)) {
            String[] command = message.split(" ");
            if (command.length != 2) {
                return "Invalid command! \nOnly 1 argument required. \nSample: play perfect";
            }
            stopAudio();
            history.add(message);
            if (playAudio(command[1], context)) {
                return "Playing your song 🎸";
            } else {
                return "Sorry!! Can't find this song for you. \n Available songs are nannaku and perfect";
            }
        }
        if (message.toLowerCase().contains(SAY)) {
            String[] command = message.split(" ");
            if (command.length < 1) {
                return "Invalid command! \nAtleast 1 argument required. \nSample: say hello";
            }
            history.add(message);
            stopAudio();
            String textToSay = message.substring(message.indexOf(" "));
            sayText(Locale.US, textToSay);
            return "Speaking your text out loud";
        }
        if (message.toLowerCase().contains(SPRECHE)) {
            String[] command = message.split(" ");
            if (command.length < 3) {
                return "Invalid command! \nAtleast 2 arguments required. \nSample: spreche us hello";
            }
            history.add(message);
            stopAudio();
            String locale = message.split(" ")[1];
            String textToSay = message.substring(message.indexOf(" ", message.indexOf(" ") + 1));
            if (locale.contains("us")) {
                sayText(Locale.US, textToSay);
            } else if (locale.contains("canada")) {
                sayText(Locale.CANADA, textToSay);
            } else if (locale.contains("chinese")) {
                sayText(Locale.CHINESE, textToSay);
            } else if (locale.contains("french")) {
                sayText(Locale.FRENCH, textToSay);
            } else {
                return "Invalid locale! Supported ones are us, canada, chinese, french";
            }
            return "Speaking your text";
        }
        if (message.toLowerCase().contains(STOP)) {
            history.add(STOP);
            if (stopAudio()) {
                return "Stopped your song!";
            } else {
                return "Nothing is playing right now!";
            }
        }
        if (message.toLowerCase().contains(VOL)) {
            String[] command = message.split(" ");
            if (command.length != 2) {
                return "Invalid command! \nSample:: vol 10";
            }
            try {
                int vol = Integer.parseInt(command[1]);
                if (vol >= 0 && vol <= 100) {
                    setVolume(context, vol);
                    return "Adjusted volume";
                } else {
                    return "Invalid volume! \nValid range is 0 to 100";
                }
            }catch (Exception e) {
                return "Enter Valid integer value";
            }
        }
        if (message.toLowerCase().contains(WHERE_IS)) {
            String[] command = message.split(" ");
            if (command.length != 2) {
                return "Invalid command! \nname required \nSample:: whereis shivam";
            }
            String loc = findEmployee(command[1]);
            if (loc != null) {
                return loc;
            } else {
                return "Cannot find " + command[1];
            }
        }
        if (message.toLowerCase().contains(CALCULATE)) {
            String[] command = message.split(" ");
            if (command.length < 2) {
                return "Invalid command! \nSample:: calculate 5 + 10";
            }
            return String.valueOf(eval(message.substring(message.indexOf(" "))));
        }
        if (message.toLowerCase().contains(IPSUM)) {
            String[] command = message.split(" ");
            if (command.length < 2) {
                return "Invalid command! \n Max Length required \nSample:: ipsum 500";
            }
            try {
                int max = Integer.parseInt(command[1]);
                return LoremIpsum.getInstance().getWords(0, max);
            }catch (Exception e) {
                return "Enter Valid integer value";
            }
        }
        if (message.toLowerCase().contains(LINK)) {
            String[] command = message.split(" ");
            if (command.length != 2) {
                return "Invalid command! \nURl should be there \nSample:: link www.google.com";
            }
            String url = command[1];
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
            return "Opening link";
        }
        return "Oops! Can't find this command. \nTry somthing out from " + dictionary.keySet().toString();
    }

    private void setVolume(Context context, int volume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int newVol = (volume / 100) * maxVolume;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0);
    }

    private void sayText(Locale locale, String message) {
        textToSpeech.setLanguage(locale);
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    private boolean playAudio(String audioName, Context context) {
        if (audioName.toLowerCase().equals("nannaku")) {
            mediaPlayer = MediaPlayer.create(context, R.raw.nannaku);
            mediaPlayer.start();
            return true;
        }
        if (audioName.toLowerCase().equals("perfect")) {
            mediaPlayer = MediaPlayer.create(context, R.raw.perfect);
            mediaPlayer.start();
            return true;
        }
        return false;
    }

    private boolean stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
            return true;
        }
        return false;
    }

    private String findEmployee(String name) {
        for (Map.Entry<String, List<String>> entry : employeeLocation.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private void updateEmployeeLoc(String name, String loc) {
        for (Map.Entry<String, List<String>> entry : employeeLocation.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).equals(name)) {
                    List<String> updated = entry.getValue();
                    updated.remove(i);
                    entry.setValue(updated);
                    break;
                }
            }
        }
        if (employeeLocation.containsKey(loc)) {
            employeeLocation.get(loc).add(name);
        } else {
            employeeLocation.put(loc, new ArrayList<String>(Arrays.asList(name)));
        }
    }

    private void addDummyEmployee() {
        employeeLocation.put(OFFICE, new ArrayList<String>(Arrays.asList("shivam", "raman")));
        employeeLocation.put(HOME, new ArrayList<String>(Arrays.asList("mayank", "neeraj")));
    }

    private void prepareDictionary() {
        dictionary.put(HELP, "display help; args: [command]");
        dictionary.put(HERE_I_AM, "update your whereis location; args: [current location]");
        dictionary.put(HISTORY, "display recent commands");
        dictionary.put(IN_OFFICE, "who is in the office");
        dictionary.put(PING, "test if the bot is active");
        dictionary.put(PLAY, "play an audio file; args: [search terms]");
        dictionary.put(SAY, "say text aloud; args: text");
        dictionary.put(SPRECHE, "say text aloud (multi-lingual); args: [iso-639-1 language code] text");
        dictionary.put(STOP, "stop all audio and video playback");
        dictionary.put(VOL, "adjust speaker volume; args: [0-100]");
        dictionary.put(WHERE_IS, "locate an employee; args: [first name]");
        dictionary.put(CALCULATE, "calculate a maths expression; args; [maths exp]");
        dictionary.put(IPSUM, "generates random lorem ipsum; args: [max words]");
        dictionary.put(LINK, "opens a link in the web browser; args: [valid link]");
    }

    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
