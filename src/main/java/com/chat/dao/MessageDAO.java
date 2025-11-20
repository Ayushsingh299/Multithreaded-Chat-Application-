package com.chat.dao;

import com.chat.model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public boolean saveMessage(Message message) {
        String sql = "INSERT INTO messages (sender_id, recipient_id, content) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, message.getSenderId());
            if (message.getRecipientId() != null) {
                stmt.setInt(2, message.getRecipientId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, message.getContent());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Message> getRecentMessages(int limit) {
        String sql = "SELECT * FROM messages WHERE recipient_id IS NULL ORDER BY sent_at DESC LIMIT ?";
        List<Message> messages = new ArrayList<>();
        
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message msg = new Message();
                msg.setMessageId(rs.getInt("message_id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setRecipientId(rs.getObject("recipient_id", Integer.class));
                msg.setContent(rs.getString("content"));
                msg.setSentAt(rs.getTimestamp("sent_at"));
                messages.add(msg);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
    
    public List<Message> getPrivateMessages(int user1Id, int user2Id) {
        String sql = "SELECT * FROM messages " +
                     "WHERE (sender_id = ? AND recipient_id = ?) " +
                     "   OR (sender_id = ? AND recipient_id = ?) " +
                     "ORDER BY sent_at ASC";
        List<Message> messages = new ArrayList<>();
        
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.setInt(3, user2Id);
            stmt.setInt(4, user1Id);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message msg = new Message();
                msg.setMessageId(rs.getInt("message_id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setRecipientId(rs.getInt("recipient_id"));
                msg.setContent(rs.getString("content"));
                msg.setSentAt(rs.getTimestamp("sent_at"));
                messages.add(msg);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
}
