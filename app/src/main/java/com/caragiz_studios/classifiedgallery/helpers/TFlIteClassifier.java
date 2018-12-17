package com.caragiz_studios.classifiedgallery.helpers;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TFlIteClassifier implements Classifier {

    public static final int MAX_RESULTS = 3;
    public static final int BATCH_SIZE = 1;
    public static final int PIXEL_SIZE = 3;
    public static final float THRESHOLD = 0.1f;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    private Interpreter interpreter;
    private int inputSize;
    private List<String> labelList;

    private TFlIteClassifier() {
    }

    public static Classifier create(AssetManager assetManager,
                             String modelPath,
                             String labelPath,
                             int inputSize) throws IOException {
        TFlIteClassifier classifier = new TFlIteClassifier();
        classifier.interpreter = new Interpreter(classifier.loadModelFile(assetManager, modelPath));
        classifier.labelList = classifier.loadLabelFile(assetManager, labelPath);
        classifier.inputSize = inputSize;
        return classifier;
    }

    @Override
    public List<Recognition> recognizeImage(Bitmap bmp) {
        ByteBuffer byteBuffer = convertBitmpatToByteBuffer(bmp);
        float[][] result = new float[1][labelList.size()];
        interpreter.run(byteBuffer, result);
        return getSortedResult(result);
    }

    @Override
    public void close() {
        interpreter.close();
        interpreter = null;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException{
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream fis = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fis.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaerdLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset , declaerdLength);

    }

    private List<String> loadLabelFile(AssetManager assetManager, String labelPath) throws IOException{
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        while ((line = reader.readLine())!=null){
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private ByteBuffer convertBitmpatToByteBuffer(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*BATCH_SIZE*inputSize*inputSize*PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[inputSize*inputSize];
        bitmap.getPixels(intValues , 0 , bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        int pixel = 0;
        for(int i = 0; i< inputSize ; ++i){
            for(int j =0 ; j<inputSize ; ++j){
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                byteBuffer.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                byteBuffer.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
        return byteBuffer;
    }

    private List<Recognition>getSortedResult(float[][]labelProbArray){
        PriorityQueue<Recognition> pq = new PriorityQueue<>(
                MAX_RESULTS,
                new Comparator<Recognition>() {
                    @Override
                    public int compare(Recognition o1, Recognition o2) {
                        return Float.compare(o2.getConfidence(), o1.getConfidence());
                    }
                });
        for(int i=0; i<labelList.size() ; ++i){
            float confidence = (labelProbArray[0][i] *100)/127f;
            if(confidence > THRESHOLD){
                pq.add(new Recognition(""+i, labelList.size()>i?labelList.get(i):"unknown",confidence));
            }
        }
        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for(int i = 0; i<recognitionsSize; ++i){
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

}
