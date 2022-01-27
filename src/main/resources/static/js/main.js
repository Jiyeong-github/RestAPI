'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event){
    username = document.querySelector("#name").value.trim();

    if(username){
        usernamePage.classList.add('hidden');
        chatPage.classList.remove("hidden");

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({},onConnected, onError);
    }
    event.preventDefault();
}
//Sockjs와 stomp client를 이용해 Spring Boot에서 구성한 /ws 엔드 포인트에 연결함.

function onConnected(){
    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.send("/app/chat.adduser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    connectingElement.classList.add('hidden');
}

function onError(error){
    connectingElement.textContent = '웹 소켓 서버에 연결되지 않았습니다. 다시 시도해주세요.';
    connectingElement.style.color = 'red';
}

function sendMsg(event){
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient){
        var chatMsg = {
            sender: username,
            content : messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMsg));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload){
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN'){
        messageElement.classList.add('event-message');
        message.content = message.sender + '입장';
    }else if(message.type === 'LEAVE'){
        messageElement.classList.add('event-message');
        message.content = message.sender+'나감';
    }else{
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageText.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender){
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMsg, true)