package com.caragiz_studios.classifiedgallery.helpers;

import android.app.Activity;

import org.tensorflow.lite.Interpreter;

import java.io.IOError;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Classifier {
    private static final String TAG = "TfLite";

    /** Name of the model file stored in Assets. */
    private static final String MODEL_PATH = "optimized_graph.lite";

    /** Name of the label file stored in Assets. */
    private static final String LABEL_PATH = "retrained_labels.txt";

    /** Number of results to show in the UI. */
    private static final int RESULTS_TO_SHOW = 3;

    /** Dimensions of inputs. */
    private static final int BATCH_SIZE = 1;

    private static final int PIXEL_SIZE = 3;

    static final int IMG_SIZE_X = 42;
    static final int IMG_SIZE_Y = 42;

    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

//    for storing image data
    private int[] intValues = new int[IMG_SIZE_X*IMG_SIZE_Y];


    private Interpreter tflite;
    private List<String> labelList;
    private ByteBuffer imgData = null;
    private float[][] labelProbArray = null;
    /** multi-stage low pass filter **/
    private float[][] filterLabelProbArray = null;
    private static final int FILTER_STAGES = 3;
    private static final float FILTER_FACTOR = 0.4f;
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels=
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    }
            );

    Classifier(Activity activity) throws IOException{
        tflite = new Interpreter(loadModelFile(activity));
        labelList = loadLabelList(activity);
        imgData = ByteBuffer.allocateDirect(4 * BATCH_SIZE * IMG_SIZE_X * IMG_SIZE_Y * PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        labelProbArray=new float[1][labelList.size()];
        filterLabelProbArray = new float[FILTER_STAGES][labelList.size()];


    }

    private List<String> loadLabelList(Activity activity) {
        return null;
    }

    private ByteBuffer loadModelFile(Activity activity) {
        return null;
    }
}
