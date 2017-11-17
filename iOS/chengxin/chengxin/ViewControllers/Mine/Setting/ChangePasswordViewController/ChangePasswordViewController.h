//
//  ChangePasswordViewController.h
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChangePasswordViewController : UIViewController

@property (nonatomic, retain) IBOutlet UITextField* txtOldPassword;
@property (nonatomic, retain) IBOutlet UITextField* txtNewPassword;
@property (nonatomic, retain) IBOutlet UITextField* txtConfirmationPassword;

-(IBAction)onBack:(id)sender;
-(IBAction)onCompletion:(id)sender;
@end
