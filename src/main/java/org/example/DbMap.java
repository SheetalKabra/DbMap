package org.example;

import java.sql.*;
import java.util.*;

public class DbMap<K, V> implements Map<K, V> {
    private Connection conn;

    public DbMap() {
        try {
            this.conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            String createTable = """
                CREATE TABLE IF NOT EXISTS kv_store (
                    key_col VARCHAR(255),
                    value_col VARCHAR(255)
                )
                """;

            stmt.execute(createTable);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        String selectQuery = "SELECT count(*) FROM kv_store";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        String selectQuery = "SELECT count(*) FROM kv_store";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        String selectQuery = "SELECT value_col FROM kv_store WHERE key_col = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setString(1, (String) key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        String selectQuery = "SELECT key_col FROM kv_store WHERE value_col = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setString(1, (String) value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public V get(Object key) {
        String selectQuery = "SELECT value_col FROM kv_store WHERE key_col = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setString(1, (String) key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (V) rs.getString("value_col");
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        String insertSQL = "INSERT into kv_store (key_col, value_col) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ps.setString(1, (String) key);
            ps.setString(2, (String) value);
            int rowsInserted = ps.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error inserting user:");
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public V remove(Object key) {
        String selectQuery = "SELECT value_col FROM kv_store WHERE key_col = ?";
        String deleteQuery = "DELETE FROM kv_store WHERE key_col = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setString(1, (String) key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String value_col = rs.getString("value_col");
                    try (PreparedStatement deletePs = conn.prepareStatement(deleteQuery)) {
                        deletePs.setString(1, (String) key);
                        int rowsDeleted = deletePs.executeUpdate();

                        if (rowsDeleted > 0) {
                            return (V) value_col;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        String upsertQuery = "Insert INTO kv_store (key_col, value_col) VALUES (?, ?) ON DUPLICATE KEY Update value_col = VALUES(value_col)";

        try (PreparedStatement ps = conn.prepareStatement(upsertQuery)) {
            for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
                ps.setString(1, (String) entry.getKey());
                ps.setString(2, (String) entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            System.err.println("Error during putAll:");
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        String truncateQuery = "TRUNCATE TABLE kv_store";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(truncateQuery);
        } catch (Exception e) {
            System.err.println("Error truncating table:");
            e.printStackTrace();
        }
    }

    @Override
    public Set<K> keySet() {
        String selectQuery = "SELECT key_col FROM kv_store";
        Set<K> keys = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    K key = (K) rs.getString("key_col");
                    keys.add(key);
                }
            }
            return keys;
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<V> values() {
        String selectQuery = "SELECT value_col FROM kv_store";
        Set<V> keys = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    V value = (V) rs.getString("value_col");
                    keys.add(value);
                }
            }
            return keys;
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        String selectQuery = "SELECT key_col,value_col FROM kv_store";
        Set<Map.Entry<K,V>> entries = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    K key = (K) rs.getString("key_col");
                    V value = (V) rs.getString("value_col");
                    Map.Entry<K,V> entry = new AbstractMap.SimpleEntry<>(key,value);
                    entries.add(entry);
                }
            }
            return entries;
        } catch (Exception e) {
            System.err.println("Error fetching user:");
            e.printStackTrace();
        }
        return null;
    }
}

