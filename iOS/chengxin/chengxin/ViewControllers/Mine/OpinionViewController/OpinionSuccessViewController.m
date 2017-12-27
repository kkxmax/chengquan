//
//  OpinionSuccessViewController.m
//  chengxin
//
//  Created by common on 5/13/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "OpinionSuccessViewController.h"

@interface OpinionSuccessViewController ()

@end

@implementation OpinionSuccessViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.messageView.layer.cornerRadius = 10;
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [NSTimer scheduledTimerWithTimeInterval:2.0 target:self selector:@selector(closeView) userInfo:nil repeats:NO];
}
-(void)closeView
{
    
    [self dismissViewControllerAnimated:NO completion:nil];
    [self.navController popViewControllerAnimated:YES];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onEmpty:(id)sender
{
    [self dismissViewControllerAnimated:NO completion:nil];
}

@end
