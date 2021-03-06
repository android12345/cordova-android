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
        var content = document.getElementById("content");
        content.innerHTML = "华为测试！huawei test";
        // 页面加载完成时，关闭 splash 界面
        navigator.splashscreen.hide();
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
    }, "Share", "share", ["你好！", "", ""]);

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

function registerum() {
    cordova.exec(function(winParam) {
        alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        alert(error);
    }, "Share", "registerUmeng", ["508a19595270157a6f00005e"]);

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

function toast(msg) {
    cordova.exec(function(winParam) {
        // alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        // alert(error);
    }, "Messages", "showMsg", [msg]);

}

function callphone() {
    cordova.exec(function(winParam) {
        alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        alert(error);
    }, "Redirect", "callNumber", ["123456789414"]);

}



function passuid() {
    cordova.exec(function(winParam) {
        alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        alert(error);
    }, "GetApplicationInfo", "getApplicationInfo", ["20"]);

}




function scan() {
    cordova.exec(function(winParam) {
        alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        alert(error);
    }, "ScanditSDK", "scan", ["71lJHNykEeKA7i/yJxRDDJxpnvykL5osh6zSD1MsrBA",
                              {"beep": true,
                              "1DScanning" : true,
                              "2DScanning" : true,
                              "scanningHotspot" : "0.5/0.5",
								"vibrate" : true,
								textForInitialScanScreenState: "将代码框对齐"}]);
						

}





			function addaralm() {
				cordova.exec(function(winParam) {
					alert(winParam);
					//var i = winParam ;
					//alert(i+"谢谢你") ;
				}, function(error) {
					alert(error);
				}, "AlarmClock", "addAlarmClock", ["1",  "这是一个内容", "1377677912","100","这是标题"]);
			}

		  
		
		
			function addaralm2() {
				cordova.exec(function(winParam) {
					alert("成功");
				}, function(error) {
					alert("失败");
				}, "AlarmClock", "addAlarmClock", ["2", "这是第二个","1364450940","2","这是第二个内容"]);

			}
	
			
			function addaralm3() {
				cordova.exec(function(winParam) {
					alert("成功");
				}, function(error) {
					alert("失败");
				}, "AlarmClock", "addAlarmClock", ["3", "这是第三个","1362366360","2","这是第二个内容"]);

			}
	
			
			function addaralm4() {
				cordova.exec(function(winParam) {
					alert("成功");
				}, function(error) {
					alert("失败");
				}, "AlarmClock", "addAlarmClock", ["4", "这是第四个","1362366360","2","这是第二个内容"]);

			}

			function delete1() {
				cordova.exec(function(winParam) {
					alert(winParam);
				}, function(error) {
					alert(error);
				}, "AlarmClock", "delete", ["1"]);

			}

			function update1() {
				cordova.exec(function(winParam) {
					alert(winParam);
					//var i = winParam ;
					//alert(i+"谢谢你") ;
				}, function(error) {
					alert(error);
				}, "AlarmClock", "update", ["1",  "我很好是一个内容", "1362463320","你好嘛"]);

			}

	
		
			
			function deleteArray() {
				cordova.exec(function(winParam) {
					alert(winParam);
					//var i = winParam ;
					//alert(i+"谢谢你") ;
				}, function(error) {
					alert(error);
				}, "AlarmClock", "deleteManyAlarmClock", ["1","2"]);

			}

	
		
			function searchall() {
				
			  	cordova.exec(function(winParam) {
					alert(winParam);
					//var i = winParam ;
					//alert(i+"谢谢你") ;
				}, function(error) {
					alert(error);
				}, "AlarmClock", "searchAlarmClock", []);
			}


function alipay(title, body, price, orderno) {
    
    cordova.exec(function(winParam) {
        toast("success = "+ winParam);
    }, function(error) {
        toast("error = "+ error);
    }, "Alipay", "alipay", [title, body, price, orderno]);
}

function tenpay(title, body, price, orderno) {
    
    cordova.exec(function(winParam) {
        alert(winParam);
        //var i = winParam ;
        //alert(i+"谢谢你") ;
    }, function(error) {
        alert(error);
    }, "Tenpay", "tenpay", [title, body, price, orderno]);
}
