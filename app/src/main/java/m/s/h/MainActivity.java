package m.s.h;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PacketListener{
    public static final String Tag = "MSH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        accountLogin();
    }
    public void get(View view){

    }


    /**
     * 离线消息
     */
    private void getOffLine() {
        new Thread() {
            public void run() {
                List<Message> msglist = ConnecMethod
                        .getOffLine();
                for (org.jivesoftware.smack.packet.Message msg : msglist) {
                    Log.d(Tag, msg.getTo() + msg.getBody() + msg.getFrom());
                }
            };
        }.start();
    }

    private Handler insHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // 登录成功
                case 0:
                    Log.d(Tag, "login suc");
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    break;
                // 登录失败
                case 1:
                    Log.d(Tag, "login fail");
                    break;
                // 注册成功
                case 2:
                    Log.d(Tag, "reg suc");
                    break;
                // 注册成功
                case 3:
                    Log.d(Tag, "reg fail");
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            Message message = (Message) msg.obj;
            String body = message.getBody();
            Log.d(Tag, "-------"+body+"-------");
        }
    };

    private void accountLogin() {
        new Thread() {
            public void run() {
                String account = ((EditText) findViewById(R.id.username))
                        .getText().toString();
                String pwd = ((EditText) findViewById(R.id.password)).getText()
                        .toString();
                boolean is = ConnecMethod.login(account, pwd);
                if (is) {
                    insHandler.sendEmptyMessage(0);
                    // 将用户名保存
                } else {
                    insHandler.sendEmptyMessage(1);
                }
            }

            ;
        }.start();
    }

    @Override
    public void processPacket(Packet packet) {
        android.os.Message msg = new android.os.Message();
        msg.obj = packet;
        mHandler.sendMessage(msg);
    }
}
