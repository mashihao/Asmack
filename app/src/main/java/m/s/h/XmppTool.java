package m.s.h;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.OfflineMessageManager;

/**
 * asmack加载的静态方法
 * 
 */
public class XmppTool {
	private static ConnectionConfiguration connConfig;
	private static XMPPConnection con;
	private static OfflineMessageManager offlineManager;

	// 静态加载ReconnectionManager ,重连后正常工作
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static OfflineMessageManager getOffLineMessageManager() {
		if (offlineManager == null) {
			offlineManager = new OfflineMessageManager(con);
		}
		return offlineManager;
	}

	private static void openConnection() {
		try {
			connConfig = new ConnectionConfiguration("192.168.0.104", 5222,"svooo");

			// 设置登录状态为离线
			connConfig.setSendPresence(false);
			// 断网重连
			connConfig.setReconnectionAllowed(true);
			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null || !con.isConnected()) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
	}
}
