'use strict';

var uniqueID = function() {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};

var username = 'Unknown';

var theMessage = function(name, message){
    return{
        user : name,
        text : message,
        id : uniqueID()
    };
};

var appState = {
    mainUrl : 'http://localhost:8080/chat',
    history:[],
    token : 'TE11EN'
};

function run(){
    var sendButton = document.getElementById('sendButton');
    var textBox = document.getElementById('textBox');
    var userName = document.getElementById('userName');

    username = userName.value;

    textBox.addEventListener('keypress', function(e){
        if(e.keyCode == 13){
          onSendButtonClick();
        }
        return false;
    });

    sendButton.addEventListener('click', onSendButtonClick);
    doPolling();
}

function onSendButtonClick(){
    var textBox = document.getElementById('textBox');
    var userName = document.getElementById('userName');
    var item = document.getElementById('newName');
    item.innerText = userName.value;
    if(textBox.value == '')
        return;
    var newMessage = theMessage(userName.value, textBox.value);
    
    sendMessage(newMessage, function(){
        console.log('Message sent ' + newMessage.text);
    });
}

function createMessage(message){

    var messages = theMessage(message.user, message.text);
    var history = appState.history;
    var textBox = document.getElementById('textBox');
    var userName = document.getElementById('userName');

    var divItem = document.createElement('div');
    var br = document.createElement('br');

    divItem.classList.add('new-msg-box');
    br.classList.add();
    divItem.appendChild(document.createTextNode(messages.user + ':'));
    divItem.appendChild(br);
    divItem.appendChild(document.createTextNode(messages.text));


    var msgBoxes = document.getElementsByClassName('msg-box')[0]; 
    msgBoxes.appendChild(divItem);

    history.push(messages);
    textBox.value = '';
}

function sendMessage(message, continueWith){
    post(appState.mainUrl, JSON.stringify(message), function(){
        continueWith && continueWith();
    });
}

function updateHistory(newMessages) {
    for(var i = 0; i < newMessages.length; i++)
        addMessageInternal(newMessages[i]);
}

function addMessageInternal(message) {
    var history = appState.history;

    history.push(message);
    createMessage(message);
}

function doPolling() {
    function loop() {
        var url = appState.mainUrl + '?token=' + appState.token;

        get(url, function(responseText) {
            var response = JSON.parse(responseText);

            appState.token = response.token;
            updateHistory(response.messages);
            setTimeout(loop, 1000);
        }, function(error) {
            defaultErrorHandler(error);
            setTimeout(loop, 1000);
        });
    }

    loop();
}

function defaultErrorHandler(message) {
    var divItem = document.createElement('div');
    divItem.classList.add('error');
    divItem.appendChild(document.createTextNode(message));
    var msgBoxes = document.getElementsByClassName('msg-box')[0]; 
    msgBoxes.appendChild(divItem);
}

function get(url, continueWith, continueWithError) {
    ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
    ajax('POST', url, data, continueWith, continueWithError);   
}

function isError(text) {
    if(text == "")
        return false;
    
    try {
        var obj = JSON.parse(text);
    } catch(ex) {
        return true;
    }

    return !!obj.error;
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if(isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
    };    

    xhr.ontimeout = function () {
        ontinueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
        var errMsg = 'Server connection error ' + appState.mainUrl + '\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

window.onerror = function(err) {
    defaultErrorHandler(err.toString());
}