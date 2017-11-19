//
//  HttpAsyncRequest.h
//
//
//  Created on 19/07/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Request.h"
#import "Response.h"

@protocol HttpAsyncRequestDelegate <NSObject>

-(void)HttpAsyncRequestDelegate :(NSString *)action :(Response *)responseData;
@end

@interface HttpAsyncRequest : NSObject {
	id<HttpAsyncRequestDelegate> delegate;
}

@property (nonatomic, retain) id<HttpAsyncRequestDelegate> delegate;

-(void)sendPostRequest :(NSString *)action :(Request *)requestData;
-(void)sendPostRequestWithUpload :(NSString *)action :(Request *)requestData;
@end

