//
//  LoginViewController.h
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController

@property (nonatomic, retain) IBOutlet UIButton* passwordEye;
@property (nonatomic, retain) IBOutlet UITextField* phoneNumberInput;
@property (nonatomic, retain) IBOutlet UITextField* passwordInput;
-(IBAction)onRegister:(id)sender;
-(IBAction)onSignup:(id)sender;
-(IBAction)onForgot:(id)sender;
-(IBAction)onSeePassword:(id)sender;
-(IBAction)onDeletePhoneNumber:(id)sender;
@end
