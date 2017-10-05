package cabral.com.br.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import cabral.com.br.whatsapp.R;
import cabral.com.br.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText codPais;
    private EditText codArea;
    private EditText telefone;
    private Button cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome     = (EditText)findViewById(R.id.edit_nome);
        codPais  = (EditText)findViewById(R.id.edit_cod_pais);
        codArea  = (EditText)findViewById(R.id.edit_cod_area);
        telefone = (EditText)findViewById(R.id.edit_telefone);
        cadastrar = (Button)findViewById(R.id.btCadastrar);

        SimpleMaskFormatter simpleMaskCodPais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskCodArea = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskCodPais = new MaskTextWatcher(codPais, simpleMaskCodPais);
        MaskTextWatcher maskCodArea = new MaskTextWatcher(codArea, simpleMaskCodArea);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        codPais.addTextChangedListener(maskCodPais);
        codArea.addTextChangedListener(maskCodArea);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        codPais.getText().toString()+
                        codArea.getText().toString()+
                       telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-","");

                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(numeroRandomico);

                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                HashMap<String, String> usuario = preferencias.getDadosUsuario();

            }
        });

    }
}
