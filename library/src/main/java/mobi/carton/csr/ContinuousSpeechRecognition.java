package mobi.carton.csr;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


public class ContinuousSpeechRecognition
        implements
        RecognitionListener {


    private SpeechRecognizer mSpeechRecognizer;
    private Intent mRecognizerIntent;


    private boolean continuousListening;


    public ContinuousSpeechRecognition(Context context) {

        continuousListening = false;

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);


        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en"); // TODO : make it as option (parameter)
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5); // doesn't really work ?
    }


    @Override
    public void onReadyForSpeech(Bundle params) {

    }


    @Override
    public void onBeginningOfSpeech() {

    }


    @Override
    public void onRmsChanged(float rmsdB) {

    }


    @Override
    public void onBufferReceived(byte[] buffer) {

    }


    @Override
    public void onEndOfSpeech() {

    }


    @Override
    public void onError(int error) {

        switch (error) {
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: // Insufficient permissions
            case SpeechRecognizer.ERROR_AUDIO: // Audio recording error
            case SpeechRecognizer.ERROR_CLIENT: // Client side error
            case SpeechRecognizer.ERROR_NETWORK: // Network error
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: // Network timeout
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: // RecognitionService busy
            case SpeechRecognizer.ERROR_SERVER: // Error from server
                break;


            case SpeechRecognizer.ERROR_NO_MATCH: // No match
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: // No speech input

                if (continuousListening) {
                    mSpeechRecognizer.startListening(mRecognizerIntent);
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onResults(Bundle results) {

        if (continuousListening)
            mSpeechRecognizer.startListening(mRecognizerIntent);
    }


    @Override
    public void onPartialResults(Bundle partialResults) {

    }


    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}