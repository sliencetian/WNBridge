/*

                       .::::.
                     .::::::::.
                    :::::::::::
                 ..:::::::::::'
              '::::::::::::'
                .::::::::::
           '::::::::::::::..
                ..::::::::::::.
              ``::::::::::::::::
               ::::``:::::::::'        .:::.
              ::::'   ':::::'       .::::::::.
            .::::'      ::::     .:::::::'::::.
           .:::'       :::::  .:::::::::' ':::::.
          .::'        :::::.:::::::::'      ':::::.
         .::'         ::::::::::::::'         ``::::.
     ...:::           ::::::::::::'              ``::.
    ````':.          ':::::::::'                  ::::..
                       '.:::::'                    ':'````..
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                        面向对象编程
*/

(function(context, publicName) {

	/**
	 * 全局对象
	 */
	var T = {};

    //注册回调native的方法
	if (typeof context[publicName] == "undefined" && !context[publicName]) {
        T["invoke"] = function(){
            var jsonRequest = JSON.stringify({
                    method: arguments[0],methodParams: arguments[1],
                    callbackFunction: arguments[2],transferParams: arguments[3]
                    })
        	//通过prompt调用Java对象方法
	        return prompt(jsonRequest);
        }
    	context[publicName] = T;
    }
    //当前注册已经完成，回调一下
	if(window.NativeHybridJsReady) {
	    window.NativeHybridJsReady();
	}
}(this, "NativeHybrid"));