package tech.brito.javafacerecognition.core;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import org.bytedeco.opencv.opencv_core.Rect;
import tech.brito.javafacerecognition.model.Usuario;
import tech.brito.javafacerecognition.repository.UserRepository;

public class CameraRegistro extends Camera {

    private final int NUMERO_CAPTURAS = 25;

    private JButton btnRegister;
    private JTextField txfNome;
    private JLabel lblContadorCapturas;
    private JDialog formulario;
    private Usuario usuario;
    private int capturasRealizadas = 1;

    public CameraRegistro(JLabel lblExibicaoCamera, JDialog formulario, JButton btnRegister, JTextField txfNome, JLabel lblContadorCapturas) {
        super(lblExibicaoCamera);

        this.formulario = formulario;
        this.btnRegister = btnRegister;
        this.txfNome = txfNome;
        this.lblContadorCapturas = lblContadorCapturas;
        this.usuario = new Usuario(UserRepository.getNextId());
    }

    @Override
    public void executarOperacao(Rect dadosFace) {
        redimensionarDemarcar(dadosFace);

        if (!btnRegister.getModel().isPressed()) {
            return;
        }

        usuario.setNome(txfNome.getText().trim().toUpperCase());
        if (!usuario.validarNomeInformado()) {
            JOptionPane.showMessageDialog(formulario, "Nome não informado!");
            return;
        }

        if (capturasRealizadas < NUMERO_CAPTURAS) {
            capturasRealizadas++;
            var cropped = String.format("%s/%d.%s.%d.jpg", caminhoDiretorioImagens, usuario.getId(), usuario.getNome(), capturasRealizadas);
            imwrite(cropped, faceCapturada);
            lblContadorCapturas.setText(String.valueOf(capturasRealizadas) + "/" + NUMERO_CAPTURAS);
        }

        validarTotalCapturas();
    }

    private void validarTotalCapturas() {
        if (capturasRealizadas > 24) {
            new TrainLBPH().trainPhotos();
            UserRepository.create(usuario);
            stopCamera();
            formulario.dispose();
        }
    }
}
