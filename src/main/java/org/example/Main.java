package org.example;

import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Map<String, String> dbmap = new DbMap<>();
        dbmap.put("Akhil", "dadakhil@gmail.com");
        dbmap.put("Akhil1", "dadakhil1@gmail.com");
        dbmap.put("Sheetal", "sheetalkabra4@gmail.com");
        System.out.println(dbmap.get("Sheetal"));
        System.out.println(dbmap.containsValue("sheetalkabra4@mgial.com"));
        System.out.println(dbmap.containsValue("sheetalkabra4@gmail.com"));
        System.out.println(dbmap.containsKey("Sheetal"));
        System.out.println(dbmap.containsKey("Akhil11"));
        System.out.println("==== IsEmpty ====");
        System.out.println(dbmap.isEmpty());
        System.out.println(dbmap.size());


//        System.out.println(dbmap.remove("Sheetal"));
        System.out.println("==== Size ====");
        System.out.println(dbmap.size());
        System.out.println("==== ContainsKey ====");
        System.out.println(dbmap.containsKey("Sheetal"));

//        dbmap.clear();
        System.out.println("==== Size ====");
        System.out.println(dbmap.size());
        System.out.println("==== KeySet ====");
        System.out.println(dbmap.keySet());
        System.out.println("==== Values ====");
        System.out.println(dbmap.values());


        System.out.println("==== PutAll ====");
        Map<String, String> userMap = new HashMap<>();
        userMap.put("Alice", "aliceq@example.com");
        userMap.put("Bob", "bob@example.com");

        dbmap.putAll(userMap);

        System.out.println("==== KeySet ====");
        System.out.println(dbmap.keySet());
        System.out.println("==== Values ====");
        System.out.println(dbmap.values());


        System.out.println("==== entrySet ====");
        System.out.println(dbmap.entrySet());
    }
}