package mobi.carton.csr;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;


public class ContinuousSpeechRecognition
        implements
        RecognitionListener {


    /*
    INTERFACES
    */


    public interface OnTextListener {
        void onTextMatched(ArrayList<String> matchedText);

        void onError(int error);
    }


    public interface OnRmsListener {
        void onRmsChanged(float rms);
    }


    private static final String LANGUAGE_PREFERENCE_DEFAULT = "en";


    private OnTextListener mOnTextListener;
    private OnRmsListener mOnRmsListener;


    private SpeechRecognizer mSpeechRecognizer;
    private Intent mRecognizerIntent;


    private AudioManager mAudioManager;
    private int mStreamVolume = 0;

    private boolean mJustStarted;

    private boolean mContinuousListening;


    public ContinuousSpeechRecognition(Context context) {
        this(context, LANGUAGE_PREFERENCE_DEFAULT);
    }


    public ContinuousSpeechRecognition(Context context, String languagePreference) {

        mContinuousListening = false;

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(this);


        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languagePreference);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5); // doesn't really work ?
    }


    public void start() {
        mJustStarted = true;
        mContinuousListening = true;
        mSpeechRecognizer.startListening(mRecognizerIntent);
    }


    public void stop() {
        mContinuousListening = false;
        mSpeechRecognizer.stopListening();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
    }


    public void destroy() {
        stop();
        mSpeechRecognizer.destroy();
    }


    /*
    SETTERS
     */


    public void setOnTextListener(OnTextListener l) {
        mOnTextListener = l;
    }


    public void setOnRmsListener(OnRmsListener l) {
        mOnRmsListener = l;
    }


    /*
    IMPLEMENTS
     */


    @Override
    public void onReadyForSpeech(Bundle params) {
        if (mJustStarted) {
            mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            mJustStarted = false;
        }
    }


    @Override
    public void onBeginningOfSpeech() {

    }


    @Override
    public void onRmsChanged(float rmsdB) {
        if (mOnRmsListener != null) {
            mOnRmsListener.onRmsChanged(rmsdB);
        }
    }


    @Override
    public void onBufferReceived(byte[] buffer) {

    }


    @Override
    public void onEndOfSpeech() {

    }


    @Override
    public void onError(int error) {
        if (mOnTextListener != null) {
            mOnTextListener.onError(error);
        }

        switch (error) {
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
            case SpeechRecognizer.ERROR_AUDIO:
            case SpeechRecognizer.ERROR_NETWORK:
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
            case SpeechRecognizer.ERROR_SERVER:
            case SpeechRecognizer.ERROR_CLIENT:
            case SpeechRecognizer.ERROR_NO_MATCH:
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

                if (mContinuousListening) {
                    mSpeechRecognizer.destroy();
                    mSpeechRecognizer.setRecognitionListener(this);
                    mSpeechRecognizer.startListening(mRecognizerIntent);
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onResults(Bundle results) {
        if (mOnTextListener != null) {
            ArrayList<String> matchedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            mOnTextListener.onTextMatched(matchedText);
        }

        if (mContinuousListening)
            mSpeechRecognizer.startListening(mRecognizerIntent);
    }


    @Override
    public void onPartialResults(Bundle partialResults) {

    }


    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}