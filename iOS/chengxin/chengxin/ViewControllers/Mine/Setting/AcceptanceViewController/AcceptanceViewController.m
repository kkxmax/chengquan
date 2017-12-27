//
//  AcceptanceViewController.m
//  chengxin
//
//  Created by common on 4/18/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "AcceptanceViewController.h"

@interface AcceptanceViewController ()

@property (weak, nonatomic) IBOutlet UITextView *contentTextView;

@end

@implementation AcceptanceViewController
@synthesize contentTextView;

- (void)viewDidLoad {
    [super viewDidLoad];
    [contentTextView setHidden:YES];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
//    [contentTextView scrollRangeToVisible:NSMakeRange(0, 0)];
//    [contentTextView scrollRectToVisible:CGRectMake(0,0,1,1) animated:YES];
    [contentTextView setContentOffset:CGPointZero animated:NO];
    [contentTextView setHidden:NO];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
