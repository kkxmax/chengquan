//
//  WebAPI.h
//  chengxin
//
//  Created by seniorcoder on 11/6/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HttpAsyncRequest.h"
#import "Response.h"

typedef void (^RequestComplitionBlock)(NSObject *resObj);

@interface WebAPI : NSObject
{
}
+ (instancetype) sharedInstance;

-(void)sendPostRequest:(NSString *)actionName Parameters: (NSMutableDictionary*) params :(RequestComplitionBlock) reqBlock;
-(void)sendPostRequestWithUpload:(NSString *)actionName Parameters: (NSMutableDictionary*) params UploadImages:(NSMutableDictionary *) images :(RequestComplitionBlock) reqBlock;
@end
