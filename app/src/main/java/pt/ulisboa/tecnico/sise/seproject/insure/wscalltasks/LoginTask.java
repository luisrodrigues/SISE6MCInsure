package pt.ulisboa.tecnico.sise.seproject.insure.wscalltasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.sise.seproject.insure.GlobalState;
import pt.ulisboa.tecnico.sise.seproject.insure.activities.MainPageActivity;
import pt.ulisboa.tecnico.sise.seproject.insure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.seproject.insure.datamodel.Customer;
import pt.ulisboa.tecnico.sise.seproject.insure.helpers.WSHelper;

public class LoginTask extends AsyncTask<Void, Void, Integer> {
    public final static String TAG = "LoginCallTask";
    GlobalState _globalState;
    private String _username;
    private String _password;
    private Context context;
    private int _sessionId = -1;
    private Customer _customer;
    private List<ClaimItem> _claimItemList;

    public LoginTask(String username, String password, GlobalState globalState, Context context) {
        this._username = username;
        this._password = password;
        this._globalState = globalState;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            _sessionId = WSHelper.login(_username, _password);        // username doesn't exist
            Log.d(TAG, "Login result => " + _sessionId);
            //_customer = WSHelper.getCustomerInfo(_sessionId);
            //_claimItemList = WSHelper.listClaims(_sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            _sessionId = -2;
        }
        return _sessionId;
    }

    @Override
    protected void onProgressUpdate(Void... params) {
    }

    protected void onPostExecute(Integer result) {
        Log.d(TAG, "result => " + result);
        _globalState.setSessionId(result);
        //_globalState.set_customer(_customer);

        if (result == -1) {
            Toast.makeText(context, "Login failed! Username or password incorrect.", Toast.LENGTH_SHORT).show();
        } else if (result == -2) {
            Toast.makeText(context, "Login failed! Sorry, we are having server problems..", Toast.LENGTH_SHORT).show();
        } else {
            _globalState.set_customer(new Customer(_sessionId, _username));
            String customerFileName = "customer.json";
            try {
//                String customerJson = JsonCodec.encodeCustomerInfo(_globalState.getCustomer());
//                Log.d(TAG, "customerInfo: customerJson - " + customerJson);
//                JsonFileManager.jsonWriteToFile(context, customerFileName, customerJson);
//                Log.d(TAG, "customerInfo: written to - " + customerFileName);

                Intent intent = new Intent(this.context, MainPageActivity.class);
                intent.putExtra("USERNAME", _globalState.getCustomer().getUsername());
                context.startActivity(intent);
                ((TaskCallBack) context).done();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

