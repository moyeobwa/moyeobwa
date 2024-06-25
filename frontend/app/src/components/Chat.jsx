import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import { Box, TextField, Button, Card, CardContent, List, ListItem, Grid } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';
import './Chat.css';

const Chat = ({ gatheringId }) => {
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const senderId = 1; // 현재 사용자의 아이디

  useEffect(() => {
    axios.get(`${apiUrl}/api/v1/chat-messages/gatherings/${gatheringId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(response => {
        setMessages(response.data.values || []);
        console.log("Fetched messages successfully:", response.data);
      })
      .catch(error => {
        console.error('Failed to fetch messages:', error);
        setMessages([]); // API 호출 실패 시 빈 배열 설정
      });

    const stompClient = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      debug: function (str) {
        console.log(str);
      },
      onConnect: () => {
        console.log('Connected!');
        stompClient.subscribe(`/topic/chat-rooms/${gatheringId}`, function (message) {
          const receivedMessage = JSON.parse(message.body);
          setMessages(prevMessages => [...prevMessages, receivedMessage]);
        });
      },
      onStompError: function (frame) {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
      onWebSocketError: function (event) {
        console.error('WebSocket error: ' + event);
      },
      onWebSocketClose: function (event) {
        console.error('WebSocket close: ' + event);
      },
      onError: function (error) {
        console.log('Error:', error);
      },
    });

    stompClient.activate();
    setClient(stompClient);
    return () => {
      stompClient.deactivate();
    };
  }, [gatheringId]);

  const handleSendMessage = () => {
    if (client && newMessage) {
      const message = {
        chatRoomId: gatheringId,
        content: newMessage,
        senderId: senderId,
      };
      client.publish({
        destination: "/app/message",
        body: JSON.stringify(message)
      });
      setNewMessage('');
    }
  };

  return (
    <Box className="chat-section">
      <Card sx={{ flex: 1, display: 'flex', flexDirection: 'column', margin: 2 }}>
        <CardContent sx={{ flex: 1, overflowY: 'auto', padding: 0 }}>
          <List className="chat-messages">
            {messages.map((msg, index) => (
              <ListItem key={index} className={`chat-message ${msg.senderId === senderId ? 'chat-message-sent' : 'chat-message-received'}`}>
                <Box className="text">
                  {msg.content}
                  <p className="time">{msg.senderName || "Unknown"}</p>
                </Box>
              </ListItem>
            ))}
          </List>
        </CardContent>
        <Box className="chat-input" sx={{ padding: 2 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={10}>
              <TextField
                fullWidth
                variant="outlined"
                placeholder="메시지를 입력하세요..."
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                sx={{ backgroundColor: 'white' }}
              />
            </Grid>
            <Grid item xs={2}>
              <Button
                className="send-button"
                fullWidth
                variant="contained"
                endIcon={<SendIcon />}
                onClick={handleSendMessage}
                sx={{ 
                  height: '100%', 
                  backgroundColor: 'rgb(246, 113, 120)', 
                  color: 'white', 
                  whiteSpace: 'nowrap',
                }}
              >
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Card>
    </Box>
  );
};

export default Chat;
