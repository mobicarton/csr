package mobi.carton.csr;


import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


public class ContinuousSpeechRecognition {


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

}