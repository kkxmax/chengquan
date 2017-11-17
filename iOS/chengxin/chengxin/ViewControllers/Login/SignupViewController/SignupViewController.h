//
//  SignupViewController.h
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SignupViewController : UIViewController

@property (nonatomic, retain) IBOutlet UITextField* txtPhoneNumber;
@property (nonatomic, retain) IBOutlet UITextField* txtRequestCode;
@property (nonatomic, retain) IBOutlet UITextField* txtVerificationCode;
@property (nonatomic, retain) IBOutlet UITextField* txtNewPassword;
@property (nonatomic, retain) IBOutlet UITextField* txtConfirmPassword;
@property (nonatomic, retain) IBOutlet UIButton* btnAccept;

- (IBAction)onBack:(id)sender;
-(IBAction)onCompletion:(id)sender;
-(IBAction)getVerificationCode:(id)sender;
-(IBAction)onAcceptCheck:(id)sender;
@end
