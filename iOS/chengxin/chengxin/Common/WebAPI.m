//
//  WebAPI.m
//  chengxin
//
//  Created by seniorcoder on 11/6/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "WebAPI.h"
#import "Global.h"

@interface WebAPI ()<HttpAsyncRequestDelegate> {
    RequestComplitionBlock complitionBlock;
    HttpAsyncRequest *asyncRequest;
    int resCode;
}
@end

@implementation WebAPI

+ (instancetype)sharedInstance {
    //Singleton instance
    static WebAPI *kWebAPI;
    kWebAPI = [[self alloc] init];
    
    //Dispatching it once.
//    static dispatch_once_t onceToken;
//    dispatch_once(&onceToken, ^{
//        
//        //  Initializing web api.
//        kWebAPI = [[self alloc] init];
//    });
    
    //Returning kWebAPI.
    return kWebAPI;
}

- (id)init {
    self = [super init];
    asyncRequest = [[HttpAsyncRequest alloc]init];
    asyncRequest.delegate = self;
    return self;
}

-(void)sendPostRequest:(NSString *)actionName Parameters: (NSMutableDictionary*) params :(RequestComplitionBlock) reqBlock {
    
    Request *request = [[Request alloc]init];
    complitionBlock = reqBlock;
    
    for (NSString* key in [params allKeys]) {
        [request.dictPermValues setObject:params[key] forKey:key];
    }
    
    ZDebug(@"request.dictPermValues::%@", request.dictPermValues);
    
    [asyncRequest sendPostRequest:actionName :request];
}

-(void)sendPostRequestWithUpload:(NSString *)actionName Parameters: (NSMutableDictionary*) params UploadImages:(NSMutableDictionary *) images :(RequestComplitionBlock) reqBlock {
    
    Request *request = [[Request alloc]init];
    complitionBlock = reqBlock;

    for (NSString* key in [params allKeys]) {
        [request.dictPermValues setObject:params[key] forKey:key];
    }
    
    ZDebug(@"request.dictPermValues::%@", request.dictPermValues);

    [asyncRequest sendPostRequestWithUpload:actionName :request :images];
}


-(void)HttpAsyncRequestDelegate :(NSString *)action :(Response *)responseData {
    
    NSMutableDictionary *dictRes = responseData.jsonData;
    
    complitionBlock(dictRes);
}

@end
