package shiwentaowifi.tjut.edu.cn.wifipwd;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;
import DBHepler.DBHelper;
import application.Init;
import application.WifiInfo;
import utils.GetRoot;
import utils.WifiManage;


public class MainActivity extends AppCompatActivity {

    private WifiManage wifiManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //友盟更新
        UmengUpdateAgent.update(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //开始获取root权限
        String apkRoot="chmod 777 "+getPackageCodePath();
        GetRoot.RootCommand(apkRoot);
        //获取root权限结束
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //数据库初始化
        DBHelper dbHelper=new DBHelper(Init.getContext(),"wifiinfos.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //
        wifiManage = new WifiManage();
        try {
            Init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public void Init() throws Exception {
        List wifiInfos = wifiManage.Read();
        ListView wifiInfosView=(ListView)findViewById(R.id.WifiInfosView);
        WifiAdapter ad = new WifiAdapter(wifiInfos,MainActivity.this);
        wifiInfosView.setAdapter(ad);
    }

    public class WifiAdapter extends BaseAdapter {

        List wifiInfos =null;
        Context con;

        public WifiAdapter(List wifiInfos,Context con){
            this.wifiInfos =wifiInfos;
            this.con = con;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return wifiInfos.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return wifiInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = LayoutInflater.from(con).inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
            WifiInfo wi= (WifiInfo) wifiInfos.get(position);
            tv.setText("Wifi:"+wi.Ssid+"\n密码:"+wi.Password);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv1= (TextView) findViewById(v.getId());
                    String msg =tv1 .getText().toString();
                    //为了使用剪贴板，需要通过调用getSystemService()方法来实例化ClipboardManager的对象
                    ClipboardManager myClipboard;
                    myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    //复制数据
                    ClipData myClip;
                    myClip = ClipData.newPlainText("wifi信息", msg);
                    myClipboard.setPrimaryClip(myClip);
                    Log.v(Init.getTAG(), msg);
                    Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

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
}
