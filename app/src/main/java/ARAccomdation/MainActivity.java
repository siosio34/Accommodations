package ARAccomdation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.youngje.tgwing.accommodations.R;

import ARAccomdation.mixare.MixView;
import ARAccomdation.mixare.MixareActivity;

public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent arIntent = new Intent(MainActivity.this,MixView.class);
        startActivity(arIntent);
    }
}
