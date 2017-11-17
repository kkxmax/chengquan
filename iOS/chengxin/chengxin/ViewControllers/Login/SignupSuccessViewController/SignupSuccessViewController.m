//
//  SignupSuccessViewController.m
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SignupSuccessViewController.h"

@interface SignupSuccessViewController ()

@end

@implementation SignupSuccessViewController

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
    [self.navigationController popToRootViewControllerAnimated:YES];
}
@end
