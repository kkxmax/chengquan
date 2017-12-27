//
//  OpinionViewController.h
//  chengxin
//
//  Created by common on 4/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OpinionViewController : UIViewController <UITextViewDelegate>

@property (nonatomic, retain) IBOutlet UITextView* textView;
@property (nonatomic, retain) IBOutlet UILabel* lblCounter;

-(IBAction)onSubmit:(id)sender;
-(IBAction)onBack:(id)sender;
@end
