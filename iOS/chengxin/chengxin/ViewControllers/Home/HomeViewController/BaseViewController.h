//
//  BaseViewController.h
//  chengxin
//
//  Created by seniorcoder on 12/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BaseDelegate<NSObject>
@optional

- (void)finishedLoadingData:(NSInteger)loadingPage;
- (void)stopLoadingIndicator;
@end

@interface BaseViewController : UIViewController
{
    id<BaseDelegate> baseDelegate;
}
@property (nonatomic, retain) id<BaseDelegate> baseDelegate;

@end
