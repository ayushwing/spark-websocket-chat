//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () {  location.reload();	 };

id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});


//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

webSocket.onopen = function() {
		$('#myModal').modal('show');
};

function submitName(){
	var person = id('user-id-name');
	if (person.value != null  && person.value != '') {
    	webSocket.send('webSocketAyushUserName###'+person.value);
    }else{
    	webSocket.send('###webSocketAnonymousUserName###');
    }
	$('#myModal').modal('hide');
	setInterval(function(){webSocket.send('');},30000);
}


function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
    insert("userlist", "<li><B>Connected Users:</B></li>");
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

function id(id) {
    return document.getElementById(id);
}