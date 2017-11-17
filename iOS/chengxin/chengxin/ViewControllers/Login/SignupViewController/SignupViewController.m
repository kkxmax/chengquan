//
//  SignupViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SignupViewController.h"
#import "SignupSuccessViewController.h"

@interface SignupViewController ()

@end

@implementation SignupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self setNavigationBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark
// Navigation Set
-(void) setNavigationBar
{
    self.navigationController.navigationBar.hidden = YES;
    
}

- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
-(IBAction)onComplete:(id)sender
{
    SignupSuccessViewController *signupSuccVC = [[SignupSuccessViewController alloc] initWithNibName:@"SignupSuccessViewController" bundle:nil];
    [self.navigationController pushViewController:signupSuccVC animated:YES];
}
@end
