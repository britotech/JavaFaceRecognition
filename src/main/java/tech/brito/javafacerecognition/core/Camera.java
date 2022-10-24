package tech.brito.javafacerecognition.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static java.util.Objects.nonNull;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.bytedeco.javacpp.BytePointer;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imencode;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public abstract class Camera {

    private javax.swing.JLabel lblExibicaoCamera;
    private Camera.DaemonThread myThread = null;
    private VideoCapture webSource = null;
    protected Mat cameraImage = new Mat();
    private Mat imageGray;
    private CascadeClassifier cascade;
    private RectVector detectedFaces;
    private BytePointer mem = new BytePointer();

    protected Mat faceCapturada;
    protected String caminhoArquivos;

    public Camera(JLabel lblExibicaoCamera, String caminhoArquivos) {
        this.lblExibicaoCamera = lblExibicaoCamera;
        this.caminhoArquivos = caminhoArquivos;
        this.cascade = new CascadeClassifier(caminhoArquivos + "/haarcascade_frontalface_alt.xml");
    }

    public abstract void executarOperacao(Rect dadosFace);

    public void redimensionarDemarcar(Rect dadosFace) {
       // rectangle(cameraImage, dadosFace, new Scalar(0, 255, 0, 2), 3, 0, 0);
        rectangle(cameraImage, dadosFace, new Scalar(0, 255, 0, 3), 3, 0, 0);
        faceCapturada = new Mat(imageGray, dadosFace);
        opencv_imgproc.resize(faceCapturada, faceCapturada, new Size(160, 160));
    }

    public void processar() {
        try {

            if (!webSource.grab()) {
                return;
            }

            webSource.retrieve(cameraImage);
            var graphics = lblExibicaoCamera.getGraphics();
            gerarImagemEscalaCinza();
            detectarFaces();

            for (int i = 0; i < detectedFaces.size(); i++) {
                executarOperacao(detectedFaces.get(i));
            }

            atualizarVisualizacaoImagem(graphics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gerarImagemEscalaCinza() {
        imageGray = new Mat();
        opencv_imgproc.cvtColor(cameraImage, imageGray, opencv_imgproc.COLOR_BGRA2GRAY);
    }

    public void detectarFaces() {
        detectedFaces = new RectVector();
        cascade.detectMultiScale(cameraImage, detectedFaces, 1.1, 1, 1, new Size(150, 150), new Size(500, 500));
    }

    public void atualizarVisualizacaoImagem(Graphics graphics) throws IOException {
        var buff = obterBufferedImage();
        if (graphics.drawImage(buff, 0, 0, 360, 390, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
            validarThreadExecucao();
        }
    }

    public BufferedImage obterBufferedImage() throws IOException {
        imencode(".bmp", cameraImage, mem);
        var im = ImageIO.read(new ByteArrayInputStream(mem.getStringBytes()));
        return (BufferedImage) im;
    }

    public void validarThreadExecucao() {

        try {
            if (!isRunnable()) {
                myThread.wait();
            }
        } catch (InterruptedException ex) {
        }
    }

    public boolean isRunnable() {
        return nonNull(myThread) && myThread.runnable;
    }

    public void startCamera() {
        new Thread() {
            @Override
            public void run() {
                webSource = new VideoCapture(0);
                myThread = new Camera.DaemonThread();
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                myThread.runnable = true;
                t.start();
            }
        }.start();
    }

    public void stopCamera() {
        try {
            myThread.runnable = false;
            webSource.release();
        } catch (Exception e) {
        }
    }

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    processar();
                }
            }
        }
    }
}
