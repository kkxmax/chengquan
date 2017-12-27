//
//  HotEstimationViewController.h
//  chengxin
//
//  Created by common on 5/13/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HotObject.h"

@interface HotEstimationViewController : UIViewController <UITextViewDelegate>

@property (nonatomic, strong) IBOutlet UITextView *textView;
@property (nonatomic, strong) IBOutlet UILabel *lblCounter;
@property (nonatomic, retain) HotObject *hotData;

-(IBAction)onOK:(id)sender;
-(IBAction)onCancel:(id)sender;
@end
