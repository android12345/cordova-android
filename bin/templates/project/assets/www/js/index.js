/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

   function record() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "CaptureSound", "audioRecord", ["10"]);
            }

        
            function stop() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "CaptureSound", "audioStop", [""]);
            }

    
            function play() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "CaptureSound", "play", [""]);
            }

        
            function share() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "Share", "share", ["你好！","http://www.baidu.com/img/shouye_b5486898c692066bd2cbaeda86d74448.gif"]);

            }

        
            function registerwx() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "Share", "registerWeixin", ["wx781a582436c6b974"]);

            }
            
               function update() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "CheckVersion", "checkVersion", [""]);

            }
            
            function getuuid() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "GetUUID", "getUUID", [""]);

            }
                 function toast() {
                cordova.exec(function(winParam) {
                    alert(winParam);
                    //var i = winParam ;
                    //alert(i+"谢谢你") ;
                }, function(error) {
                    alert(error);
                }, "Messages", "showMsg", ["测试显示消息插件"]);

            }
