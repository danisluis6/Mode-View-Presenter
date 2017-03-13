package vn.enclave.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vn.enclave.app.R;

public class LoginActivity extends Activity {

    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView txRemmember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiComponents();
        setContentView(R.layout.activity_login);
        intiWidgets();
    }

    private void intiWidgets() {
        edtEmail = (EditText)this.findViewById(R.id.email);
        edtPass = (EditText)this.findViewById(R.id.pass);
        btnLogin = (Button)this.findViewById(R.id.login);
        txRemmember = (TextView)this.findViewById(R.id.rememberme);

        /**
         * Configurate some attributes
         */
        edtEmail.setMovementMethod(null);
        edtPass.setMovementMethod(null);
    }

    private void intiComponents() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /** this.requestWindowFeature(Window.FEATURE_NO_TITLE); **/
    }
}
