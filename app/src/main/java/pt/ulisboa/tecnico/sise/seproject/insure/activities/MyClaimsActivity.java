package pt.ulisboa.tecnico.sise.seproject.insure.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pt.ulisboa.tecnico.sise.seproject.insure.GlobalState;
import pt.ulisboa.tecnico.sise.seproject.insure.R;
import pt.ulisboa.tecnico.sise.seproject.insure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.seproject.insure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.seproject.insure.wscalltasks.MyClaimsTask;
import pt.ulisboa.tecnico.sise.seproject.insure.wscalltasks.ReadClaimTask;

public class MyClaimsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SISE - ListClaims";
    private GlobalState _globalState;
    private ListView _listView;
    private List<ClaimRecord> _claimList;
    private Button buttonBack;
    private Context _context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_claims);

        _globalState = (GlobalState) getApplicationContext();
        _listView = (ListView) findViewById(R.id.my_claims_list);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClaimItem claim = (ClaimItem) _listView.getItemAtPosition(position);
                new ReadClaimTask(_globalState, _globalState.getSessionId(), claim.getId(), _context).execute();
                Log.d("position", position + "");
            }
        });

        buttonBack = (Button) findViewById(R.id.my_claims_list_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "My Claims Buttons is working!");
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new MyClaimsTask(_globalState, _globalState.getSessionId(), _listView, _context).execute();
    }
}
