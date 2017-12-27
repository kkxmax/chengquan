//
//  UpdateVersionViewController.m
//  chengxin
//
//  Created by common on 3/10/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "UpdateVersionViewController.h"
#import "Global.h"

@interface UpdateVersionViewController ()

@end

@implementation UpdateVersionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.contentView.layer.cornerRadius = 5;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onLater:(id)sender
{
    [self dismissViewControllerAnimated:NO completion:nil];
}
- (IBAction)onUpdate:(id)sender
{
    [appDelegate.window makeToast:@"Application Updated"
                duration:3.0
                position:CSToastPositionCenter
                   style:nil];
    
    [self dismissViewControllerAnimated:NO completion:nil];
}

@end
