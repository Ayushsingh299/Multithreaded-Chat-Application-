<%@ page session="true" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Chat - <%= username %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <style>
        .chat-container {
            max-width: 900px;
            margin: 20px auto;
            border: 1px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
        }
        .chat-header {
            background: #007bff;
            color: white;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        body[data-theme='dark'] .chat-header {
            background: #1a73e8;
        }
        .chat-messages {
            height: 400px;
            overflow-y: auto;
            padding: 15px;
            background: #f9f9f9;
        }
        body[data-theme='dark'] .chat-messages {
            background: #1e1e1e;
        }
        .message {
            margin: 10px 0;
            padding: 8px 12px;
            border-radius: 5px;
            background: #e3f2fd;
        }
        body[data-theme='dark'] .message {
            background: #2d2d2d;
        }
        .message.own {
            background: #c8e6c9;
            text-align: right;
        }
        body[data-theme='dark'] .message.own {
            background: #1b5e20;
        }
        .chat-input {
            display: flex;
            padding: 15px;
            border-top: 1px solid #ddd;
        }
        .chat-input input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-right: 10px;
        }
        .chat-input button {
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .chat-input button:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <button id="themeToggle" style="position:absolute;top:20px;right:20px;font-size:1.5em;cursor:pointer;border:none;background:none;">🌙</button>
    
    <div class="chat-container">
        <div class="chat-header">
            <h2>💬 Chat Room - Welcome <%= username %>!</h2>
            <a href="${pageContext.request.contextPath}/logout" style="color:white;text-decoration:none;">Logout</a>
        </div>
        
        <div class="chat-messages" id="chatMessages">
            <div class="message">
                <strong>System:</strong> Welcome to the chat! Start typing below.
            </div>
            <div class="message">
                <strong>System:</strong> Use /private username message for private messages
            </div>
        </div>
        
        <div class="chat-input">
            <input type="text" id="messageInput" placeholder="Type your message..." onkeypress="if(event.keyCode==13) sendMessage()">
            <button onclick="sendMessage()">Send</button>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    <script>
        let username = '<%= username %>';
        
        function sendMessage() {
            let input = document.getElementById('messageInput');
            let message = input.value.trim();
            
            if (message) {
                addMessage(username, message, true);
                input.value = '';
                
                // Simulate receiving messages (in real app, use WebSocket)
                setTimeout(() => {
                    addMessage('Bot', 'Message received!', false);
                }, 500);
            }
        }
        
        function addMessage(sender, text, isOwn) {
            let messagesDiv = document.getElementById('chatMessages');
            let messageDiv = document.createElement('div');
            messageDiv.className = 'message' + (isOwn ? ' own' : '');
            messageDiv.innerHTML = '<strong>' + sender + ':</strong> ' + text;
            messagesDiv.appendChild(messageDiv);
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }
    </script>
</body>
</html>
