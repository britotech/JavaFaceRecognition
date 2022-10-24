package tech.brito.javafacerecognition.core;

import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import tech.brito.javafacerecognition.model.Usuario;
import tech.brito.javafacerecognition.repository.UserRepository;

public class CameraReconhecimento extends Camera {

    private final LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
    private final JTextField txfId;
    private final JTextField txfNome;
    private Usuario usuario;

    public CameraReconhecimento(JLabel lblExibicaoCamera, String caminhoArquivos, JTextField txfId, JTextField txfNome) {
        super(lblExibicaoCamera, caminhoArquivos);
        this.txfId = txfId;
        this.txfNome = txfNome;
        recognizer.read(caminhoArquivos + "\\classifierLBPH.yml");
        recognizer.setThreshold(80);        
        usuario = new Usuario();
    }

    @Override
    public void executarOperacao(Rect dadosFace) {
        redimensionarDemarcar(dadosFace);
        int prediction = obterPredicaoFaceBuscada();

        if (prediction == -1) {
            rectangle(cameraImage, dadosFace, new Scalar(0, 0, 255, 3), 3, 0, 0);
            usuario.setId(null);
            usuario.setNome("Desconhecido");
            updateForm();
        } else {
            rectangle(cameraImage, dadosFace, new Scalar(0, 255, 0, 3), 3, 0, 0);
            usuario = UserRepository.findById(prediction);
            updateForm();
        }
    }

    private void updateForm() {

        if (Objects.isNull(usuario.getId())) {
            txfId.setText("");
        } else {
            txfId.setText(usuario.getId().toString());
        }

        txfNome.setText(usuario.getNome());
    }

    private int obterPredicaoFaceBuscada() {
        var rotulo = new IntPointer(1);
        var confidence = new DoublePointer(1);
        recognizer.predict(faceCapturada, rotulo, confidence);
        return rotulo.get(0);
    }
}
