package org.tensorflow.demo;


import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ClassifierActivity extends Activity {

  Button confirmButton;
  Button clearButton;
  DrawingView drawingView;
  TextView textView;

  Classifier classifier;

  private static final int INPUT_SIZE = 299;
  private static final int IMAGE_MEAN = 128;
  private static final float IMAGE_STD = 128.0f;
  private static final String INPUT_NAME = "Mul:0";
  private static final String OUTPUT_NAME = "final_result";

  private static final String MODEL_FILE = "file:///android_asset/graph.pb";
  private static final String LABEL_FILE = "file:///android_asset/labels.txt";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    confirmButton = (Button)this.findViewById(R.id.recognizeButton);
    clearButton = (Button)this.findViewById(R.id.clearButton);
    drawingView = (DrawingView)this.findViewById(R.id.drawingView);
    textView = (TextView)this.findViewById(R.id.textView);

    classifier = TensorFlowImageClassifier.create(
            getAssets(),
            MODEL_FILE,
            LABEL_FILE,
            INPUT_SIZE,
            IMAGE_MEAN,
            IMAGE_STD,
            INPUT_NAME,
            OUTPUT_NAME);

    clearButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Paint clearPaint = new Paint();
        Canvas canvas = drawingView.getCanvas();
        clearPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), clearPaint);
        textView.setText("");
      }
    });

    confirmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bitmap bitmap = drawingView.getmBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(drawingView.getmBitmap(), INPUT_SIZE, INPUT_SIZE, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(scaledBitmap);
        Classifier.Recognition firstRecognition = results.get(0);
        String recognitionString = firstRecognition.getTitle() + " " + firstRecognition.getConfidence();
        textView.setText(recognitionString);
      }
    });
  }
}
