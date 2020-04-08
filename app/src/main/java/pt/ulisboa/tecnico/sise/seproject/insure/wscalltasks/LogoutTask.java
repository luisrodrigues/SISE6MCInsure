package pt.ulisboa.tecnico.sise.seproject.insure.wscalltasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.seproject.insure.GlobalState;
import pt.ulisboa.tecnico.sise.seproject.insure.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.seproject.insure.helpers.WSHelper;

public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "LogoutCallTask";

    private GlobalState _globalState;
    private Context _context;

    public LogoutTask(GlobalState globalState, Context context) {
        this._globalState = globalState;
        this._context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Integer sessionId = _globalState.getCustomer().getSessionId();
        try {
            return WSHelper.logout(sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (b != null && b) {
            //reset session id
            _globalState.getCustomer().setSessionId(-1);

            _context.deleteFile("customer.json");
            _context.deleteFile("claimList.json");
            _globalState.clearCustomer();

            //open login activity
            Intent intent = new Intent(this._context, LoginActivity.class);
            _context.startActivity(intent);

            ((TaskCallBack) _context).done();

        } else {
            Toast.makeText(_context, "Unable to logout...", Toast.LENGTH_SHORT).show();
        }
    }
}
