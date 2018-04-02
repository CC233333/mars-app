package u.master.protocol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mRequestView;
    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestView = (Button) findViewById(R.id.request_view);
        mResultView = (TextView) findViewById(R.id.result_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.start(v.getContext());
            }
        });


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MarsServiceProxy.setForeground(true);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MarsServiceProxy.setForeground(false);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private RequestTask mRequestTask;

//    private void request() {
//        if (mRequestTask != null) {
//            MarsServiceProxy.cancel(mRequestTask);
//        }
//
////        mRequestTask = new RequestTask(new MainRequest(), new MainResponse());
//        mRequestTask = new RequestTask(new Main.MainRequest(), new Main.MainResponse());
//        MarsServiceProxy.send(mRequestTask.setHostPath(UApp.BASE_URL, "/mars/getconvlist"));
//    }

//    private class RequestTask extends NanoTaskStub<Main.MainRequest, Main.MainResponse> {
//
//        public RequestTask(Main.MainRequest request, Main.MainResponse response) {
//            super(request, response);
//        }
//
//        @Override
//        public void onPreEncode(Main.MainRequest request) {
//            request.type = Main.MainRequest.DEFAULT;
//            request.token = "";
//        }
//
//        @Override
//        public void onPostDecode(Main.MainResponse response) {
//
//        }
//
//        @Override
//        public void onTaskEnd(int type, int code) throws RemoteException {
//            super.onTaskEnd(type, code);
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mResponse != null) {
//                        mResultView.setText(mResponse.toString());
//                    }
//                }
//            });
//        }
//    }
}
