/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanrabad.survey.presenter.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import th.or.nectec.tanrabad.survey.R;

public class AdvanceStepperDialog extends Dialog implements View.OnClickListener {

    private final TextView callerView;
    private boolean safeMode = true;
    private TextView textOperator;
    private TextView textOperand2nd;
    private int operand1st = 0;
    private int operand2nd = 0;
    private Operator operator = Operator.ADDITION;

    public AdvanceStepperDialog(Context context, TextView callerView) {
        super(context);
        this.callerView = callerView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_advance_stepper);
        setupFirstOperand();

        textOperator = (TextView) findViewById(R.id.operator);
        textOperand2nd = (TextView) findViewById(R.id.operand2nd);

        updateOperator(Operator.ADDITION);
        setOnClickListener();
    }

    private void setupFirstOperand() {
        TextView textOperand1st = (TextView) findViewById(R.id.operand1st);
        String text = callerView.getText().toString();
        if (TextUtils.isEmpty(text)) {
            text = "0";
        }
        operand1st = Integer.parseInt(text);
        textOperand1st.setText(text);
    }

    private void setOnClickListener() {
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.zero).setOnClickListener(this);

        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    private void updateOperator(Operator operator) {
        this.operator = operator;
        textOperator.setText(operator.sign);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                concatSecondOperand(1);
                break;
            case R.id.two:
                concatSecondOperand(2);
                break;
            case R.id.three:
                concatSecondOperand(3);
                break;
            case R.id.four:
                concatSecondOperand(4);
                break;
            case R.id.five:
                concatSecondOperand(5);
                break;
            case R.id.six:
                concatSecondOperand(6);
                break;
            case R.id.seven:
                concatSecondOperand(7);
                break;
            case R.id.eight:
                concatSecondOperand(8);
                break;
            case R.id.nine:
                concatSecondOperand(9);
                break;
            case R.id.zero:
                concatSecondOperand(0);
                break;
            case R.id.plus:
                updateOperator(Operator.ADDITION);
                break;
            case R.id.minus:
                updateOperator(Operator.SUBTRACTION);
                break;
            case R.id.delete:
                deleteSecondOperand();
                break;
            case R.id.enter:
                setResultToCallerView();
                dismiss();
                break;
        }
    }

    private void concatSecondOperand(int newDigit) {
        String current = String.valueOf(operand2nd);
        setSecondOperand(current + newDigit);
    }

    private void setSecondOperand(String secondOperand) {
        try {
            operand2nd = Integer.parseInt(secondOperand);
            textOperand2nd.setText(String.valueOf(operand2nd));
        } catch (NumberFormatException nfe) {
            operand2nd = 0;
            textOperand2nd.setText(String.valueOf(0));
        }
    }

    private void deleteSecondOperand() {
        String current = String.valueOf(operand2nd);
        String newOperand = "0";
        if (current.length() > 1)
            newOperand = current.substring(0, current.length() - 1);
        setSecondOperand(newOperand);
    }

    private void setResultToCallerView() {
        int result = 0;
        switch (operator) {
            case ADDITION:
                result = operand1st + operand2nd;
                break;
            case SUBTRACTION:
                result = operand1st - operand2nd;
                if (safeMode && result < 0)
                    result = 0;
                break;
        }
        callerView.setText(String.valueOf(result));
    }

    private enum Operator {
        ADDITION("+"), SUBTRACTION("-");
        String sign;

        Operator(String sign) {
            this.sign = sign;
        }
    }
}
