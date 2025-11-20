<!DOCTYPE html>
<html>
<head>
    <title>Register - Chat Application</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
    <button id="themeToggle" style="position:absolute;top:20px;right:20px;font-size:1.5em;cursor:pointer;border:none;background:none;">🌙</button>
    
    <div class="container">
        <h2>Create Account</h2>
        
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/register" method="post">
            <input type="text" name="username" placeholder="Username (min 3 chars)" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="text" name="fullName" placeholder="Full Name">
            <input type="password" name="password" placeholder="Password (min 6 chars)" required>
            <button type="submit">Register</button>
        </form>
        
        <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
