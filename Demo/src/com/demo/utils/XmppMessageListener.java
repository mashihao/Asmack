package com.demo.utils;

import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by sv-004 on 2016/7/1.
 */
public class XmppMessageListener implements PacketListener{
    private static final String Tag = "MSH";
    @Override
    public void processPacket(Packet packet) {
        final Message nowMessage = (Message) packet;
        Log.d(Tag,nowMessage.getBody()+"------msh");
    }
}
