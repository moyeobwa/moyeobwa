// Messages.js
import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import { Input, Button, Card, Typography } from '@material-tailwind/react';

function Messages() {
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {
        login: "user",
        passcode: "password",
      },
      debug: function (str) {
        console.log(str);
      },
      onConnect: () => {
        console.log('Connected!');
        stompClient.subscribe('/topic/messages', function (message) {
          setMessages(prev => [...prev, JSON.parse(message.body).content]);
        });
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
  }, []);

  const sendMessage = () => {
    if (client && message) {
      client.publish({destination: "/app/send", body: JSON.stringify({message})});
      setMessage("");
    }
  };

  return (
    <Card>
      <Typography variant="h5" color="blue-gray" className="p-4">실시간 채팅</Typography>
      <div className="overflow-auto h-96">
        {messages.map((msg, index) => (
          <Typography key={index} className="p-2">{msg}</Typography>
        ))}
      </div>
      <div className="p-4 flex">
        <Input
          type="text"
          value={message}
          onChange={e => setMessage(e.target.value)}
          placeholder="메시지를 입력하세요..."
          className="mr-4"
        />
        <Button onClick={sendMessage} color="light-blue">보내기</Button>
      </div>
    </Card>
  );
}

export default Messages;
