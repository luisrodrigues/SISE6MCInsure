package pt.ulisboa.tecnico.sise.seproject.insure.wscalltasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.seproject.insure.InternalProtocol;
import pt.ulisboa.tecnico.sise.seproject.insure.TaskCallBack;
import pt.ulisboa.tecnico.sise.seproject.insure.WSHelper;

public class NewClaimTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "Insure";
    private String res;
    private Context _context;
    private int _sessionID = 0;
    private String _title;
    private String _occurDate;
    private String _plateNumber;
    private String _description;

    public NewClaimTask(Context context, int sessionID, String title, String occurDate, String plateNumber, String description) {
        this._context = context;
        this._sessionID = sessionID;
        this._title = title;
        this._occurDate = occurDate;
        this._plateNumber = plateNumber;
        this._description = description;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            boolean r = WSHelper.submitNewClaim(_sessionID, _title, _occurDate, _plateNumber, _description);
            Log.d(TAG, "Submit new claim result => " + r);
            res = "" + r;
        } catch (Exception e) {
            Log.d(TAG, "new claim failed!");
        }
        return res;
    }

    @Override
    protected void onProgressUpdate(Void... params) {

    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("true")) {
            if (_title.equals("")) {
                Toast.makeText(_context, "Write a claim title", Toast.LENGTH_LONG).show();
                return;
            }

            //return an intent containing the title and body of the new note
            Intent resultIntent = new Intent();

            resultIntent.putExtra(InternalProtocol.KEY_NEW_CLAIM_TITLE, _title);
            resultIntent.putExtra(InternalProtocol.KEY_NEW_CLAIM_PLATE_NUMBER, _plateNumber);
            resultIntent.putExtra(InternalProtocol.KEY_NEW_CLAIM_OCCUR_DATE, _occurDate);
            resultIntent.putExtra(InternalProtocol.KEY_NEW_CLAIM_DESCRIPTION, _description);

            ((Activity) _context).setResult(Activity.RESULT_OK, resultIntent);

            // write a toast message
            Toast.makeText(_context, "Claim submitted", Toast.LENGTH_LONG).show();

            ((TaskCallBack) _context).done();
        } else {
            // do something
        }
    }
}
