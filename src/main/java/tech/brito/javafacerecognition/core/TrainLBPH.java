package tech.brito.javafacerecognition.core;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

public class TrainLBPH {

    public void trainPhotos() {
        var diretorioArquivos = new java.io.File("").getAbsolutePath() + "\\src\\main\\resources\\imagens\\";
        var directory = new File(diretorioArquivos);
        FilenameFilter filter = (File dir, String name1) -> name1.endsWith(".jpg") || name1.endsWith(".png");

        var files = directory.listFiles(filter);
        var photos = new MatVector(files.length);
        var labels = new Mat(files.length, 1, CV_32SC1);
        IntBuffer labelsBuffer = labels.createBuffer();

        int counter = 0;
        for (File image : files) {
            var photo = imread(image.getAbsolutePath(), COLOR_BGRA2GRAY);
            var personId = Integer.parseInt(image.getName().split("\\.")[0]);
            opencv_imgproc.resize(photo, photo, new Size(160, 160));

            photos.put(counter, photo);
            labelsBuffer.put(counter, personId);
            counter++;
        }

        var lbph = LBPHFaceRecognizer.create(1, 8, 8, 8, 12);
        lbph.train(photos, labels);
        lbph.save(diretorioArquivos + "classifierLBPH.yml");
    }
}
