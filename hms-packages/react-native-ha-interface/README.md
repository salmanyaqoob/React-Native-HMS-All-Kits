## HiAnalyticsReactNativeSDK-tool


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction

    HiAnalyticsReactNativeSDK-tool is an SDK that React-Native projects use to access Analytics Kit in HUAWEI HMS Core. The SDK provides on-device functions such as collection, reporting, and analysis. For details, please visit:
    https://developer.huawei.com/consumer/en/hms/huawei-analyticskit
## Installation
    Copy the react-native-ha-interface folder to the node_modules folder in the root directory of the React-Native project, and run the `yarn add ./node_modules/react-native-ha-interface` command in the root directory to install the HiAnalyticsReactNativeSDK-tool SDK. Integrate the on-device dependency SDK. For details, please visit:
    https://developer.huawei.com/consumer/en/hms/huawei-analyticskit
    
## Supported Environments
	React-Native 0.59 or later is supported. The recommended version is React-Native 0.61.  
    The Android version must be later than 17.
	
## Sample Code
    1) Import the SDK object to the App.js file.
    ```JavaScript
    import haSDK from 'react-native-ha-interface';
    ```
    
    2) Report the custom event.
    ```JavaScript
    // Compile the haOnEvent function to call the onEvent API in the SDK.
    // eventObj is the parameter object contained in the custom event that you want to upload. The parameter can be of the string, number, and bool types.
    function haOnEvent(){
      const eventObj={
        testString:'StrContent',
        testInt:20,
        testDouble:2.2,
        testBoolean:false
      }
      haSDK.onEvent('newTestEvent',eventObj);
    }
    
    3) Report the preset event.
    ```JavaScript
    const eventObj={
      PRODUCTID:'item_ID',
      PRODUCTNAME:'name',
      CATEGORY:'category',
      QUANTITY:100,
      PRICE:10.01,
      REVENUE:10,
      CURRNAME:'currency',
      PLACEID:'location_ID'
    }
    haSDK.onEvent(hasdk.ADDPRODUCT2WISHLIST,eventObj);
    ```

##  License
    HiAnalyticsReactNativeSDK-tool is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

