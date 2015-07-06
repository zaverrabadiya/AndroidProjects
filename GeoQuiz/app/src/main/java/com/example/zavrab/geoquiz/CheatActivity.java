package com.example.zavrab.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends ActionBarActivity {

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.zavrab.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.zavrab.geoquiz.answer_shown";
    private static final String KEY_ANSWERSHOWN = "answerShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsAnswerShown = true;
                updateAnswer();
                setAnswerShownResult();
            }
        });

        if(savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_ANSWERSHOWN);
        }
        updateAnswer();
        setAnswerShownResult();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWERSHOWN, mIsAnswerShown);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return  i;
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void updateAnswer() {
        if(mIsAnswerShown) {
            if (mAnswerIsTrue)
                mAnswerTextView.setText(R.string.true_button);
            else
                mAnswerTextView.setText(R.string.false_button);
        }
    }
}
