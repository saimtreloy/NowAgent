package saim.com.nowagent;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {

    TextInputEditText inputEmailLogin, inputPasswordLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("TOKEN ID", FirebaseInstanceId.getInstance().getToken());

        init();
    }

    private void init() {
        inputEmailLogin = (TextInputEditText) findViewById(R.id.inputEmailLogin);
        inputPasswordLogin = (TextInputEditText) findViewById(R.id.inputPasswordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
