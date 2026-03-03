package com.tvassistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private SpeechRecognizer speechRecognizer;
    private TextView tvStatus;
    private TextView tvResult;
    private LinearLayout bottomBar;
    private ImageView micIcon;
    private Handler handler = new Handler();
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);
        tvResult = findViewById(R.id.tv_result);
        bottomBar = findViewById(R.id.bottom_bar);
        micIcon = findViewById(R.id.mic_icon);

        setupSpeechRecognizer();

        // Hide bottom bar initially
        bottomBar.setVisibility(View.GONE);
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                tvStatus.setText("🎤 বলুন...");
                micIcon.setImageResource(R.drawable.ic_mic_active);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                );
                if (matches != null && !matches.isEmpty()) {
                    String command = matches.get(0).toLowerCase();
                    tvResult.setText(matches.get(0));
                    processCommand(command);
                }
                isListening = false;
                hideBarAfterDelay(3000);
            }

            @Override
            public void onError(int error) {
                tvStatus.setText("আবার চেষ্টা করুন");
                isListening = false;
                hideBarAfterDelay(2000);
            }

            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() { tvStatus.setText("⏳ Processing..."); }
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void processCommand(String command) {
        // YouTube searches
        if (command.contains("বাংলা মুভি") || command.contains("bangla movie") || command.contains("নতুন মুভি")) {
            searchYouTube("নতুন বাংলা মুভি 2024");
        } else if (command.contains("নিউজ") || command.contains("news") || command.contains("খবর")) {
            searchYouTube("আজকের বাংলা খবর");
        } else if (command.contains("গান") || command.contains("song") || command.contains("music")) {
            searchYouTube("বাংলা গান");
        } else if (command.contains("নাটক") || command.contains("drama")) {
            searchYouTube("বাংলা নাটক 2024");
        } else if (command.contains("ইউটিউব") || command.contains("youtube")) {
            openYouTube();
        } else if (command.contains("সেটিং") || command.contains("setting")) {
            openSettings();
        } else {
            // General YouTube search with whatever was said
            searchYouTube(command);
        }
    }

    private void searchYouTube(String query) {
        tvStatus.setText("🔍 YouTube এ খুঁজছে...");
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", query);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            // Fallback to browser
            Uri uri = Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(query));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browserIntent);
        }
    }

    private void openYouTube() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    private void startListening() {
        if (!isListening) {
            isListening = true;
            bottomBar.setVisibility(View.VISIBLE);
            tvStatus.setText("🎤 বলুন...");
            tvResult.setText("");

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "bn-BD");
            intent.putExtra(RecognizerIntent.EXTRA_ALSO_RECOGNIZE_SPEECH_LANGUAGES, "en-US");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

            speechRecognizer.startListening(intent);
        }
    }

    private void hideBarAfterDelay(int ms) {
        handler.postDelayed(() -> {
            bottomBar.setVisibility(View.GONE);
        }, ms);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Mic button or search button on remote
        if (keyCode == KeyEvent.KEYCODE_SEARCH ||
            keyCode == KeyEvent.KEYCODE_VOICE_ASSIST ||
            keyCode == KeyEvent.KEYCODE_MICROPHONE) {
            startListening();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
