package m.s.h;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 连接
 *
 * @author jin
 */
public class ConnecMethod {
    /**
     * 登录
     *
     * @param account  登录帐号
     * @param password 登录密码
     * @return
     */
    public static boolean login(String account, String password) {
        try {
            if (XmppTool.getConnection() == null)
                return false;
            /** 登录 */
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            XmppTool.getConnection().login(account, password);
            // 设置登录状态：在线
             Presence presence = new Presence(Presence.Type.available);
             XmppTool.getConnection().sendPacket(presence);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册
     *
     * @param account  注册帐号
     * @param password 注册密码
     */
    public static String regist(String account, String password) {
        if (XmppTool.getConnection() == null)
            return "0";
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(XmppTool.getConnection().getServiceName());
        reg.setUsername(account);// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
        reg.setPassword(password);
        reg.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
        PacketFilter filter = new AndFilter(new PacketIDFilter(
                reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = XmppTool.getConnection()
                .createPacketCollector(filter);
        XmppTool.getConnection().sendPacket(reg);
        IQ result = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        // Stop queuing results
        collector.cancel();// 停止请求results（是否成功的结果）
        if (result == null) {
            Log.e("RegistActivity", "No response from server.");
            return "服务器没有结果";
        } else if (result.getType() == IQ.Type.RESULT) {
            return "注册成功";
        } else { // if (result.getType() == IQ.Type.ERROR)
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                Log.e("RegistActivity", "IQ.Type.ERROR: "
                        + result.getError().toString());
                return "账号已经存在";
            } else {
                Log.e("RegistActivity", "IQ.Type.ERROR: "
                        + result.getError().toString());
                return "注册失败";
            }
        }
    }

    /**
     * 获取所有好友
     *
     * @return
     */
    public static List<RosterEntry> searchAllFriends() {
        List<RosterEntry> entries = new ArrayList<RosterEntry>();
        Collection<RosterEntry> roscol = XmppTool.getConnection().getRoster()
                .getEntries();
        Iterator<RosterEntry> iter = roscol.iterator();

        while (iter.hasNext()) {
            entries.add(iter.next());
        }
        return entries;
    }

    /**
     * 获取所有组
     *
     * @return
     */
    public static List<RosterGroup> searchAllGroup() {
        List<RosterGroup> groups = new ArrayList<RosterGroup>();
        Collection<RosterGroup> roscol = XmppTool.getConnection().getRoster()
                .getGroups();
        Iterator<RosterGroup> iter = roscol.iterator();
        while (iter.hasNext()) {
            groups.add(iter.next());
        }
        return groups;
    }

    /**
     * 获取某组的所有好友
     *
     * @param group
     * @return
     */
    public static List<RosterEntry> getGroupFriends(String group) {
        List<RosterEntry> entries = new ArrayList<RosterEntry>();
        RosterGroup rosgrou = XmppTool.getConnection().getRoster()
                .getGroup(group);
        Collection<RosterEntry> roscol = rosgrou.getEntries();
        Iterator<RosterEntry> iter = roscol.iterator();
        while (iter.hasNext()) {
            entries.add(iter.next());
        }
        return entries;
    }

    /**
     * 创建新组
     *
     * @param group
     * @return a new group, or null if the group already exists
     */
    public static boolean addNewGroup(String group) {
        try {
            XmppTool.getConnection().getRoster().createGroup(group);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加组好友
     *
     * @param group
     * @param friend
     */
    public static void addGroupFriend(String group, String friend) {
        Roster roster = XmppTool.getConnection().getRoster();
        try {
            roster.createEntry(friend + "@coboqo", null, new String[]{group});
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 搜索用户
     */
    public static ArrayList<String> searchUsers(String user) {
        ArrayList<String> users = new ArrayList<String>();
        UserSearchManager usm = new UserSearchManager(XmppTool.getConnection());
        Form searchForm = null;
        try {
            searchForm = usm.getSearchForm("search."
                    + XmppTool.getConnection().getServiceName());
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", user);
            ReportedData data = usm.getSearchResults(answerForm, "search."
                    + XmppTool.getConnection().getServiceName());
            // column:jid,Username,Name,Email
            Iterator<Row> it = data.getRows();
            Row row = null;
            while (it.hasNext()) {
                row = it.next();
                // Log.d("UserName",
                // row.getValues("Username").next().toString());
                // Log.d("Name", row.getValues("Name").next().toString());
                // Log.d("Email", row.getValues("Email").next().toString());
                // 若存在，则有返回,UserName一定非空，其他两个若是有设，一定非空
                users.add(row.getValues("Username").next().toString());
            }
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 获取离线的消息
     *
     * @return
     */
    public static List<Message> getOffLine() {
        List<Message> msglist = new ArrayList<Message>();
        // 获取离线消息,线程阻塞 不能Toast
        try {
            Iterator<Message> it = XmppTool
                    .getOffLineMessageManager().getMessages();
            while (it.hasNext()) {
                Message message = it.next();
                msglist.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 设置在线
//                Presence presence = new Presence(Presence.Type.available);
//                XmppTool.getConnection().sendPacket(presence);
                XmppTool.getOffLineMessageManager().deleteMessages();
            } catch (XMPPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return msglist;
    }
    /**
     * 获取在线消息
     *
     * @return
     */
    public static void getOnLine(final Context context) {
        ChatManager cm = XmppTool.getConnection().getChatManager();
        cm.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean able) {
                chat.addMessageListener(new MessageListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void processMessage(Chat chat2, Message message) {
                        Log.d("connectMethod:getOnLine()", message.getBody()+ message.getFrom());
                        sendBroadcastMsg(context, message.getBody());
                    }
                });
            }
        });
    }

    /**
     * VCard
     */
    public static VCard getVCard(String user) {
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
                new org.jivesoftware.smackx.provider.VCardProvider());
        VCard card = new VCard();
        try {
            card.load(XmppTool.getConnection(), user);
            Log.d("*****", card.getFirstName() + card.getNickName());
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return card;
    }

    /**
     * 添加好友
     *
     * @param user
     */
    public static void addFriend(String user) {
        try {
            // 添加好友
            Roster roster = XmppTool.getConnection().getRoster();
            roster.createEntry(user + "@coboqo", null,
                    new String[]{"friends"});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 文本广播
     *
     * @param context
     */
    private static void sendBroadcastMsg(Context context, String txt) {
        Intent intent = new Intent();
        intent.setAction("msg_receiver");
        intent.putExtra("msg", txt);
        context.sendBroadcast(intent);
    }

    /**
     * 获取在线文件
     *
     * @param context
     */
    public static void getOnLineFile(Context context) {
        FileTransferManager fileTransferManagernew = new FileTransferManager(
                XmppTool.getConnection());
        FileTransferListener filter = new ChatFileTransferListener(context);
        fileTransferManagernew.addFileTransferListener(filter);
    }

    /**
     * 文件广播
     *
     * @param context
     */
    private static void sendBroadcastFile(Context context, String filepath) {
        Intent intent = new Intent();
        intent.setAction("file_receiver");
        intent.putExtra("path", filepath);
        context.sendBroadcast(intent);
    }

    /**
     * 文件接受监听器
     */
    static class ChatFileTransferListener implements FileTransferListener {
        Context context;

        public ChatFileTransferListener(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        public void fileTransferRequest(FileTransferRequest request) {
            // TODO Auto-generated method stub
            try {
                File insFile = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + request.getFileName());
                IncomingFileTransfer infiletransfer = request.accept();
                infiletransfer.recieveFile(insFile);

                sendBroadcastFile(context, insFile.getAbsolutePath());
            } catch (XMPPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param to
     * @param msg
     */
    public static void sendTalkMsg(String to, String msg) {
        Chat chat = XmppTool.getConnection().getChatManager()
                .createChat(to, null);
        try {
            chat.sendMessage(msg);
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 发送文件
     *
     * @param to
     * @param filepath
     */
    public static void sendTalkFile(String to, String filepath) {
        FileTransferManager fileTransferManager = new FileTransferManager(
                XmppTool.getConnection());
        OutgoingFileTransfer outgoingFileTransfer = fileTransferManager
                .createOutgoingFileTransfer(to + "/Spark 2.6.3");
        File insfile = new File(filepath);
        try {
            outgoingFileTransfer.sendFile(insfile, "descr");
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
