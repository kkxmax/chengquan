//
//  HttpAsyncRequest.m
//
//
//  Created on 19/07/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "HttpAsyncRequest.h"
#import "AFNetworking.h"
#import "Global.h"
#import "CommonData.h"
#import "AppDelegate.h"
#import "UIView+Toast.h"

@interface HttpAsyncRequest (){
    NSString *strAction;
}
@end


@implementation HttpAsyncRequest

@synthesize delegate;

#pragma mark -
#pragma mark NSURLRequest


-(void)sendPostRequest :(NSString *)action :(Request *)requestData {
    
    strAction = action;
    NSString *strUrl = BASE_URL;
    
    ZDebug(@"%@, action = %@",strUrl, strAction);
    
    AFHTTPRequestOperationManager *operationmanager = [AFHTTPRequestOperationManager manager];
    
    operationmanager.responseSerializer =
    [AFJSONResponseSerializer serializerWithReadingOptions: NSJSONReadingMutableContainers];
    
    operationmanager.responseSerializer.acceptableContentTypes = [operationmanager.responseSerializer.acceptableContentTypes setByAddingObject:@"text/html"];
    [operationmanager POST:strUrl parameters:requestData.dictPermValues success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSDictionary *dicRes = responseObject;
         
         if (dicRes != nil ) {
             if ([dicRes[@"retCode"] intValue] == 297) {
                 [GeneralUtil hideProgress];
                 AppDelegate *app = (AppDelegate*)[UIApplication sharedApplication].delegate;
                 if(app.isAccountDuplicated)
                     return;
                 [GeneralUtil setUserPreference:@"phone" value:@""];
                 [appDelegate.window makeToast:@"该账号已在其他设备登录"
                                     duration:3.0
                                     position:CSToastPositionCenter
                                        style:nil];
                 app.isAccountDuplicated = true;
                 [app.navController popToRootViewControllerAnimated:YES];
                 //[GeneralUtil hideProgress];
                 return;
             }
         }
         NSDictionary *responsedictionary = (NSDictionary *)responseObject;
         ZDebug(@"sendPostRequest > response: %@", responsedictionary);
         Response *response = [[Response alloc] init];
         response.jsonData = responsedictionary;
         
         if([delegate respondsToSelector:@selector(HttpAsyncRequestDelegate::)])
             [delegate HttpAsyncRequestDelegate:strAction :response];
         
         
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
         NSLog(@"API call Error : - %@",error.description);
         [GeneralUtil hideProgress];
         
         if ([strAction isEqualToString:ACTION_LOGIN]) {
             if([delegate respondsToSelector:@selector(HttpAsyncRequestDelegate::)])
                 [delegate HttpAsyncRequestDelegate:strAction :nil];
         }
     }];
}


-(void)sendPostRequestWithUpload :(NSString *)action :(Request *)requestData :(NSMutableDictionary *)uploadImageData {
    
    strAction = action;
    NSString *strUrl=[NSString stringWithFormat:@"%@pAct=%@&token=%@", BASE_URL, action, [CommonData sharedInstance].tokenName];
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:strUrl parameters:requestData.dictPermValues constructingBodyWithBlock:^(id<AFMultipartFormData> formData){
        if([action isEqualToString:ACTION_ADDITEM] || [action isEqualToString:ACTION_ADDSERVICE] || [action isEqualToString:@"authPersonal"]) {
            if(uploadImageData && [uploadImageData.allKeys containsObject:@"logo"]) {
                NSData *imageData = uploadImageData[@"logo"];
                if(imageData) {
                    [formData appendPartWithFileData:imageData name:@"logo" fileName:@"logo.png"  mimeType:@"image/png"];
                }
            }
        }
        if([action isEqualToString:ACTION_MAKECORRECT] || [action isEqualToString:ACTION_ADDPRODUCT] || [action isEqualToString:ACTION_LEAVEESTIMATE]) {
            if(uploadImageData && [uploadImageData.allKeys containsObject:@"images"]) {
                NSArray *imageArray = uploadImageData[@"images"];
                if(imageArray) {
                    for (int i = 0; i < imageArray.count; i++) {
                        NSData* imageData = (NSData*)(imageArray[i]);
                        [formData appendPartWithFileData:imageData name:[NSString stringWithFormat:@"iOSImage%d", i] fileName:[NSString stringWithFormat:@"iOSImage%d.png", i]  mimeType:@"image/png"];
                    }
                }
            }
        }
        if([action isEqualToString:ACTION_AUTHPERSONAL]) {
            [formData appendPartWithFileData:uploadImageData[@"logo"] name:@"logo" fileName:@"logo.png" mimeType:@"image/png"];
            [formData appendPartWithFileData:uploadImageData[@"certImage"] name:@"certImage" fileName:@"certImage.png" mimeType:@"image/png"];
        }
        if([action isEqualToString:ACTION_AUTHENTER]) {
            [formData appendPartWithFileData:uploadImageData[@"logo"] name:@"logo" fileName:@"logo.png" mimeType:@"image/png"];
            [formData appendPartWithFileData:uploadImageData[@"enterCertImage"] name:@"enterCertImage" fileName:@"enterCertImage.png" mimeType:@"image/png"];
        }


        /*
        if([requestData.dictPermValues[@"pAct"] isEqualToString:@"authPersonal"])
        {
            NSData* logoImage = requestData.dictPermValues[@"logo"];
            [formData appendPartWithFormData:logoImage name:@"logo"];
            NSData* certImage = requestData.dictPermValues[@"certImage"];
            [formData appendPartWithFormData:certImage name:@"certImage"];
        }
         */
        /*
        NSString *ImagePath = [NSString stringWithFormat:@"%@/test.jpg",@"dirPath"];
        NSData *imageData = [NSData dataWithContentsOfFile:ImagePath ];
        if(imageData)
            [formData appendPartWithFileData:imageData name:@"user_photo" fileName:@"user_photo.jpg" mimeType:@"image/jpeg"];
        */
    } success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSLog(@"Success: %@", responseObject);
         NSDictionary *responsedictionary = (NSDictionary *)responseObject;
         
         Response *response = [[Response alloc] init];
         response.jsonData = responsedictionary;
         
         if([delegate respondsToSelector:@selector(HttpAsyncRequestDelegate::)])
             
             [delegate HttpAsyncRequestDelegate:strAction :response];
     }
     
          failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"Internal Error");
         
     }];
}

//- (void)uploadFailed:(ASIHTTPRequest *)theRequest {
//    [appDelegate.progressHUD hide:YES];
//    ZDebug(@"%@",[NSString stringWithFormat:@"Fail uploading %@ bytes of data",[[theRequest error] localizedDescription]]);
//}
//
//-(void)uploadFinished:(ASIHTTPRequest *)theRequest {
//    [appDelegate.progressHUD hide:YES];
//    ZDebug(@"%@",[NSString stringWithFormat:@"Finished uploading %llu bytes of data",[theRequest postLength]]);
//}

@end
