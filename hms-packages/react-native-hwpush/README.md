Huawei Reactive-Native SDK
Contents
Introduction
Installation Guide
Reactive-Native SDK API Method Definition
Configure description
Licensing and Terms

1. Introduction
The Reactive-Native SDK code encapsulates the Huawei push client interface. It provides many APIs for your reference or use.
The Reactive-Native SDK code package is described as follows:
Android: core layer, bridging PushSDK bottom-layer code;
src/RNMessageParser.js: parsing layer, which is used to parse the received Message message.
index.js: external interface definition layer, which is used to define interface classes or reference files.

2. Installation Guide
Before using Reactive-Native SDK code, ensure that the RN development environment has been installed.
Decompress the Reactive-Native SD compressed code package.
Copy the decompressed react-native-hwpush package to the same path as the local directory.

3. Reactive-Native SDK API method definition 
   
    HmsInstanceId   
        getAAID(Callback callBack):string    
        getId(Callback callBack): string 
        getCreationTime(Callback callBack): long 
        deleteAAID(Callback callBack):void   
        getToken(Callback callBack): String    
        deleteToken(Callback callBack): void  
        
    HmsMessaging    
        isAutoInitEnabled(): boolean    
        setAutoInitEnabled(boolean enable):void 
        subscribe(String topic):Task<void>  
        unsubscribe(String topic):Task<void>    
        HmsMessageService com.huawei.hms.push   
        onMessageReceived(RemoteMessage message):void   
        onNewToken(String token): void  
        onSendError(String msgId, Exception exception): void
        onMessageSent(String msgId):void
        onDeletedMessages(): void
        
    RNSendException
        ERROR_UNKNOWN   
        ERROR_INVALID_PARAMETERS    
        ERROR_SIZE: 
        ERROR_TTL_EXCEEDED  
        ERROR_TOO_MANY_MESSAGES 
        
    RNMessagePriority   
        PRIORITY_HIGH   
        PRIORITY_NORMAL  
        PRIORITY_UNKNOWN    
        
    RNRemoteMessage     
        getCollapseKey(): indicates the collapse key. Its value is a character string.  
        getDataOfMap:Map<String, String>    
        getData(): indicates the data type. Its value is a character string.    
        getToken(): indicates the token. Its value is a character string.   
        getFrom(): string   
        getMessageId(): indicates the message ID. Its value is a character string.  
        getMessageType(): indicates the message type. Its value is a character string.  
        getOriginalPriority():int   
        getPriority():int   
        getSentTime(): long 
        getTo(): string 
        getTtl(): int   
        getBody(): string   
        getBodyLocalizationArgs():String[]  
        getBodyLocalizationKey(): indicates the localization key. Its value is a character string.  
        getChannelId(): indicates the channel ID. Its value is a character string.  
        getClickAction(): indicates the action. Its value is a character string.    
        getIntentUri(): string  
        getColor(): string  
        getIcon(): indicates the ID of an Icon. Its value is a character string.    
        getImageUrl(): URI  
        getLink():Uri   
        getSound(): string  
        getTag(): indicates the tag type. Its value is a character string.  
        getTitle(): string  
        getTitleLocalizationArgs():String[] 
        getTitleLocalizationKey():String    
        getNotifyId(): int  
        getDefaultLightSettings(): boolean  
        getDefaultSound(): boolean  
        getDefaultVibrateSettings(): boolean    
        getEventTime(): Long    
        getLightSettings(): int[]   
        getLocalOnly(): boolean 
        getNotificationCount(): Integer 
        getNotificationPriority(): Integer  
        getSticky(): boolean    
        getTicker(): String 
        getVibrateTimings(): long[] 
        getVisibility(): Integer    
4. Configure parameters.    
No.
5. Licensing and Terms  
Huawei Reactive-Native SDK uses the Apache 2.0 license.
