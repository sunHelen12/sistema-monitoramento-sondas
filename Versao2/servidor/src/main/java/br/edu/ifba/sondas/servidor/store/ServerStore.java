package br.edu.ifba.sondas.servidor.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.LinkedList;

/*
 Simple per-sonda circular buffer to keep only recent K messages.
 Insert O(1); storage bounded.
*/
public class ServerStore {
    private static final int MAX = 50;
    private final Map<String, LinkedList<String>> data = new ConcurrentHashMap<>();
    private static final ServerStore INSTANCE = new ServerStore();
    private ServerStore() {}
    public static ServerStore getInstance(){ return INSTANCE; }

    public synchronized void add(String sondaId, String json) {
        data.putIfAbsent(sondaId, new LinkedList<>());
        LinkedList<String> q = data.get(sondaId);
        q.addFirst(json);
        if (q.size() > MAX) q.removeLast();
        System.out.printf("Armazenadas %d entradas para %s\n", q.size(), sondaId);
    }

    public synchronized LinkedList<String> getRecent(String sondaId) {
        return data.getOrDefault(sondaId, new LinkedList<>());
    }
}
