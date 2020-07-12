var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");

/*TODO try deploying with wss it should work on both https and http*/

webSocket.onmessage = function (msg) { 
    var data = JSON.parse(msg.data);
    insert("chat", data.response);

    id("userlist").innerHTML = "";
    
    data.activeUsers.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
    insert("userlist", "<li><B>Connected:</B></li>");
};

webSocket.onclose = function () {  
    $('#sessionClose').modal('show');	 
};

id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

function openConnection(){
	var person = id('username');
	if (person.value != null  && person.value != '') {
    	webSocket.send(person.value + '@#@joined the chat..');
    } else {
    	webSocket.send('@#@joined the chat..');
    }
}

function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

function id(id) {
    return document.getElementById(id);
}

webSocket.onopen = function() {
        $('#nameModal').modal('show');
};
