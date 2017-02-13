/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.fayeclient;

import java.util.HashMap;
import java.util.Map;
//import org.cometd.bayeux.client.ClientSessionChannel;
//import org.cometd.client.BayeuxClient;
//import org.cometd.client.transport.LongPollingTransport;

/**
 *

 */
public class PushClientFaye {

    /*
    private BayeuxClient client;
    private final ClientSessionChannel channelSession;
    private String channel;
    public PushClientFaye(String channel) {
        String host = System.getProperty("FAYE_SERVER_HOST");
        String port = System.getProperty("FAYE_SERVER_PORT");
        this.client = new BayeuxClient(
                "http://" + host + ":" + port + "/faye", LongPollingTransport.create(null));
        client.handshake();
        client.waitFor(1000, BayeuxClient.State.CONNECTED);
        this.channelSession = client.getChannel("/"+channel);
        this.channel = channel;
    }

    public void publish(int count) {
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("channel", this.channel);
            json.put("count", count);
            channelSession.publish(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void publish(String key, Object value){
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("channel", this.channel);
            json.put(key, value);
            channelSession.publish(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnectChannel() {
        client.disconnect();
        client.waitFor(1000, BayeuxClient.State.DISCONNECTED);
        client = null;
    }
    */
}
