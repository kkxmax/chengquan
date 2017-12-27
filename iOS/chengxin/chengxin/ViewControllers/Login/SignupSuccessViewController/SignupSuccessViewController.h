//
//  SignupSuccessViewController.h
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SignupSuccessViewController : UIViewController
@property (nonatomic, retain) IBOutlet UIButton* btnAuth;
@property (nonatomic, retain) IBOutlet UIButton* btnGoOver;
- (IBAction)onBack:(id)sender;
-(IBAction)onAuth:(id)sender;
-(IBAction)onGoOver:(id)sender;
@end
