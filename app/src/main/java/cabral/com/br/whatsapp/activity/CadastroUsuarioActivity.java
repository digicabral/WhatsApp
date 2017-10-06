package cabral.com.br.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import cabral.com.br.whatsapp.R;
import cabral.com.br.whatsapp.config.ConfiguracaoFirebase;
import cabral.com.br.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button cadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome      = (EditText)findViewById(R.id.edit_cadastro_nome);
        email     = (EditText)findViewById(R.id.edit_cadastro_email);
        senha     = (EditText)findViewById(R.id.edit_cadastro_senha);
        cadastrar = (Button)findViewById(R.id.bt_cadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                cadastrarUsuario();
            }
        });

    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();
                    FirebaseUser userFirebase = task.getResult().getUser();
                    usuario.setId(userFirebase.getUid());
                    usuario.salvar();

                    autenticacao.signOut();
                    finish();

                }
                else{
                    String erroExcecao = "";
                    try{
                        throw task.getException();
                    }
                    catch(FirebaseAuthWeakPasswordException e)
                    {
                        erroExcecao = "Sua senha deve conter masi caracteres, além de letras e numeros!";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado não é válido!";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        erroExcecao = "Este e-mail já está em uso!";
                    }
                    catch(Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
