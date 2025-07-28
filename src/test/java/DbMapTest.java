import org.example.DbMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DbMapTest {
    private static DbMap<String, String> dbMap;

    @BeforeEach
    void setup() {
        // You can also point this to a dedicated test database
        dbMap = new DbMap<>();
        System.out.println("In setup");
        dbMap.put("SheetalTest", "sheetaltest@gmail.com");
        dbMap.put("SheetalTest1", "sheetaltest1@gmail.com");
        dbMap.put("SheetalTest2", "sheetaltest2@gmail.com");
        dbMap.put("SheetalTest3", "sheetaltest3@gmail.com");

    }
    @AfterEach
    void tearDown(){
        System.out.println("In tear down");
        dbMap.clear(); // Clear table before tests
    }

//    @Test
//    void testPutAndGet(){
//        dbMap.put("SheetalTest", "sheetaltest@gmail.com");
//        String result = dbMap.get("SheetalTest");
//        assertEquals("sheetaltest@gmail.com", result);
//    }

    @Test
    void testSize(){
        int result = dbMap.size();
        assertEquals(4, result);
    }

    @Test
    void testIsEmpty(){
        Boolean result = dbMap.isEmpty();
        assertEquals(false, result);
    }

    @Test
    void testContainsKeyExistent(){
        boolean result = dbMap.containsKey("SheetalTest");
        assertEquals(true, result);
    }

    @Test
    void testContainsKeyNonExistent(){
        boolean result = dbMap.containsKey("AkhilTest");
        assertEquals(false, result);
    }

    @Test
    void testContainsValueExistent(){
        boolean result = dbMap.containsValue("sheetaltest@gmail.com");
        assertEquals(true, result);
    }

    @Test
    void testContainsValueNonExistent(){
        boolean result = dbMap.containsKey("akhil@gmail.com");
        assertEquals(false, result);
    }

    @Test
    void testGetNonExistentKey(){
        String result = dbMap.get("SheetalTest11");
        assertNull(result);
    }

    @Test
    void testGetExistentKey(){
        String result = dbMap.get("SheetalTest");
        assertEquals("sheetaltest@gmail.com", result);
    }

    @Test
    void testRemoveExistentKey(){
        String result = dbMap.remove("SheetalTest");
        assertEquals("sheetaltest@gmail.com", result);
    }

    @Test
    void testClear(){
        dbMap.clear();
        boolean result = dbMap.isEmpty();
        assertEquals(true, result);
    }


}
