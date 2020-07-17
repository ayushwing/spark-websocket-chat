var webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/chat");
/*To run locally just change wss to ws to avoid https issue*/

webSocket.onmessage = function (msg) { 
    var data = JSON.parse(msg.data);
    insert("chat", data.response);

    id("userlist").innerHTML = "";
    
    data.activeUsers.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
};

webSocket.onclose = function () {  
    $('#sessionClose').modal('show');	 
};

id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

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

function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

function id(id) {
    return document.getElementById(id);
}

webSocket.onopen = function() {
        $('#nameModal').modal('show');
        $('#nameModal').on('shown.bs.modal', function() {
  $('#username').focus();
});
};